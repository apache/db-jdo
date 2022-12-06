/*
 * Copyright 2006-2018 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jdo.exectck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class to load SQL files via JDBC.
 *
 * <p>The class expects a comment on a single line having a connect statement defining url, user and
 * password to connect to the database e.g. -- connect 'jdbc:derby:jdotckdb;create=true' user
 * 'tckuser' password 'tckuser';
 */
public class SQLFileLoader {

  // the reader for the sql file
  private Reader reader;
  // holds the next character that is just read from the reader
  private int nextChar;
  // holds the current SQL statement
  private StringBuilder currentStmt = new StringBuilder();
  // flag indicating whether a SQL string literal is processed
  private boolean scanStringLiteral = false;
  // the list of SQL statements
  private List<String> statements = new ArrayList<>();
  // the JDBC connection url
  private String connect;
  // the JDBC connection user
  private String user;
  // the JDBC connection password
  private String password;

  /**
   * Constructor. Reads the SQL statements from the specified file. The internal state of the class
   * is updated with the contents of the file and may be accessed using the getter methods (see
   * below).
   *
   * @param filename the name of the sql to be loaded
   * @throws IOException If an I/O error occurs
   */
  public SQLFileLoader(String filename) throws IOException {
    try (Reader r = new BufferedReader(new FileReader(filename))) {
      // init reader instance variable
      this.reader = r;
      // initialize nextChar with the first character from the stream
      this.nextChar = this.reader.read();

      int currentChar = next();
      // read characters from the stream
      while (currentChar != -1) {
        accept(currentChar);
        currentChar = next();
      }
      this.reader = null;
    }
  }

  /**
   * Returns the list of SQL statements processed by this SQLFileLoader.
   *
   * @return list of SQL statements
   */
  public List<String> getStatements() {
    return this.statements;
  }

  /**
   * Returns the JDBC connection URL.
   *
   * @return the JDBC connection URL
   */
  public String getConnect() {
    return this.connect;
  }

  /**
   * Returns the JDBC connection user.
   *
   * @return the JDBC connection user.
   */
  public String getUser() {
    return this.user;
  }

  /**
   * Returns the JDBC connection password.
   *
   * @return the JDBC connection password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Checks the specified character whether it terminates a SQL Statement. If so the current SQL
   * statement is added to the list of processed SQL statements and a new SQL statement is
   * initialized. Otherwise the method appends the specified character to the current SQL statement.
   *
   * @param value character to be checked
   */
  private void accept(int value) {
    if (value == ';' && !this.scanStringLiteral) {
      // found statement end
      statements.add(currentStmt.toString());
      currentStmt = new StringBuilder();
    } else {
      currentStmt.append((char) value);
    }
  }

  /**
   * Returns the next character from the input that is not part of a comment. That means single line
   * comments and multi line comments are skipped. The method is able to handle SQL String literals.
   *
   * @return the next non comment character
   * @throws IOException If an I/O error occurs
   */
  private int next() throws IOException {
    int result = this.nextChar;
    switch (this.nextChar) {
      case '\'':
        this.scanStringLiteral = !this.scanStringLiteral;
        this.nextChar = this.reader.read();
        break;
      case '/':
        if (!this.scanStringLiteral) {
          int next = this.reader.read();
          if (next == '*') {
            this.nextChar = this.reader.read();
            skipToEndOfMLComment();
            skipWhitespace();
            return next();
          } else {
            this.nextChar = next;
          }
        } else {
          this.nextChar = this.reader.read();
        }
        break;
      case '-':
        if (!this.scanStringLiteral) {
          int next = this.reader.read();
          if (next == '-') {
            this.nextChar = reader.read();
            handleSingleLineComment();
            skipWhitespace();
            return next();
          } else {
            this.nextChar = next;
          }
        } else {
          this.nextChar = this.reader.read();
        }
        break;
      case ' ':
      case '\t':
      case '\n':
      case '\r':
        if (!this.scanStringLiteral) {
          skipWhitespace();
          result = ' ';
        } else {
          this.nextChar = this.reader.read();
        }
        break;
      default:
        this.nextChar = this.reader.read();
        break;
    }
    return result;
  }

  /**
   * Skips a single line comment. That means any character from -- to the end of the line are
   * akipped.
   *
   * @throws IOException If an I/O error occurs
   */
  private void handleSingleLineComment() throws IOException {
    Optional<String> optConnect = readIdentFollowedByLiteral("connect");
    optConnect.ifPresent(x -> this.connect = x);
    Optional<String> optUser = readIdentFollowedByLiteral("user");
    optUser.ifPresent(x -> this.user = x);
    Optional<String> optPassword = readIdentFollowedByLiteral("password");
    optPassword.ifPresent(x -> this.password = x);
    skipToEOL();
  }

  /**
   * Tries to read an Java identifier with the expected name followed by a SQL string literal.
   *
   * @param expected the expected Java identifier
   * @return an Optional with the text of SQL String literal if present; otherwise an empty Optional
   * @throws IOException If an I/O error occurs
   */
  private Optional<String> readIdentFollowedByLiteral(String expected) throws IOException {
    Optional<String> optIdent = readIdentifier();
    if (optIdent.isPresent()) {
      String ident = optIdent.get();
      if (ident.equalsIgnoreCase(expected)) {
        Optional<String> optLiteral = readSQLStringLiteral();
        if (optLiteral.isPresent()) {
          return optLiteral;
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Tries to read a Java identifier.
   *
   * @return an Optional with the text of the identifier if present; otherwise an empty Optional
   * @throws IOException If an I/O error occurs
   */
  private Optional<String> readIdentifier() throws IOException {
    skipBlanksAndTabs();
    if (Character.isJavaIdentifierStart(this.nextChar)) {
      StringBuilder ident = new StringBuilder();
      ident.append((char) this.nextChar);
      this.nextChar = this.reader.read();
      while (Character.isJavaIdentifierPart(this.nextChar)) {
        ident.append((char) this.nextChar);
        this.nextChar = reader.read();
      }
      return Optional.of(ident.toString());
    }
    return Optional.empty();
  }

  /**
   * Tries to read a SQL String literal.
   *
   * @return an Optional with the text of the SQL String literal if present; otherwise an empty
   *     Optional
   * @throws IOException If an I/O error occurs
   */
  private Optional<String> readSQLStringLiteral() throws IOException {
    skipBlanksAndTabs();
    if (this.nextChar == '\'') {
      StringBuilder literal = new StringBuilder();
      this.nextChar = this.reader.read();
      while (this.nextChar != -1) {
        // possible end of SQL String literal
        if (this.nextChar == '\'') {
          this.nextChar = this.reader.read();
          // are there two single quotes?
          if (this.nextChar == '\'') {
            // yes -> append one ' and continue
            literal.append('\'');
          } else {
            // found end of literal
            break;
          }
        } else {
          literal.append((char) this.nextChar);
          this.nextChar = this.reader.read();
        }
      }
      return Optional.of(literal.toString());
    }
    return Optional.empty();
  }

  /**
   * Skip any whitespace characters. The variable nextChar holds the first non whitespace char.
   *
   * @throws IOException If an I/O error occurs
   */
  private void skipWhitespace() throws IOException {
    while (Character.isWhitespace(this.nextChar)) {
      this.nextChar = this.reader.read();
    }
  }

  /**
   * Skips any blank and tab characters. The variable nextChar holds the first non blank or non tab
   * char.
   *
   * @throws IOException If an I/O error occurs
   */
  private void skipBlanksAndTabs() throws IOException {
    while (this.nextChar == ' ' || this.nextChar == '\t') {
      this.nextChar = this.reader.read();
    }
  }

  /**
   * Skips to the end of the current line. The newline char is consumed. The variable nextChar holds
   * the first char of the next line.
   *
   * @throws IOException If an I/O error occurs
   */
  private void skipToEOL() throws IOException {
    while (this.nextChar != -1) {
      if (this.nextChar == '\n') {
        break;
      }
      this.nextChar = this.reader.read();
    }
    this.nextChar = this.reader.read();
  }

  /**
   * Skips to the end of a multi line comment. Any characters up to * followed by / are consumed.
   * The variable nextChar holds the first char after the comment.
   *
   * @throws IOException If an I/O error occurs
   */
  private void skipToEndOfMLComment() throws IOException {
    while (this.nextChar != -1) {
      if (this.nextChar == '*') {
        this.nextChar = this.reader.read();
        if (this.nextChar == '/') {
          break;
        }
      }
      this.nextChar = this.reader.read();
    }
    this.nextChar = this.reader.read();
  }

  /**
   * main method For testing; prints the internal state to System.out.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    for (String arg : args) {
      try {
        SQLFileLoader loader = new SQLFileLoader(arg);
        List<String> stmts = loader.getStatements();
        System.out.println(loader.getConnect());
        System.out.println(loader.getUser());
        System.out.println(loader.getPassword());
        stmts.forEach(System.out::println);
      } catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
}
