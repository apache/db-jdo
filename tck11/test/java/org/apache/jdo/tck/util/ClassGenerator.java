/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 

//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:
//Company:      Your Company
//Description:  Your description


package org.apache.jdo.tck.util;

import java.io.*;

public class ClassGenerator {
  private final String [] fieldTypes       = {"boolean", "byte", "short", "int", "long",
                                              "char", "float", "double",
                                              "Boolean", "Character", "Byte", "Short", "Integer",
                                              "Long", "Float", "Double", "String",
                                              "Locale", "Date", "BigDecimal", "BigInteger",
                                              "Object", "SimpleClass", "SimpleInterface"
                                              };
  //includes a place holder for default package access
  private final String [] accessSpecifiers = {"private ", "public ", "protected ", "" };

  private final String [] fieldModifiers   = {"", "static ", "transient ", "final ", "volatile ",
                                              "static transient ", "static final ", "static volatile ",
                                              "transient final ", "transient volatile ",
                                              "static transient final ", "static transient volatile " };

  private final String [] xmlPersistenceModifiers = {"", "persistence-modifier=\"none\"",
                                                      "persistence-modifier=\"persistent\"",
                                                      "persistence-modifier=\"transactional\""};

  private final String [] xmlEmbeddedModifiers    = {"", "embedded=\"true\"", "embedded=\"false\""};

  private final String [] collectionTypes    = {"Collection", "Map", "Set", "List",
                                               "HashSet", "ArrayList", "HashMap", "Hashtable",
                                               "LinkedList", "TreeMap", "TreeSet", "Vector", "Array"};

  //Note: Any updates to elementtypes should include an update to elementsWithPackageInfo
  private final String [] elementTypes    = {"Object", "SimpleClass", "SimpleInterface", "String",
                                            "Date", "Locale", "BigDecimal", "BigInteger",
                                            "Byte", "Double", "Float", "Integer", "Long",
                                            "Short"};

  private final String [] elementsWithPackageInfo = {"java.lang.Object",
                                                    "org.apache.jdo.tck.pc.fieldtypes.SimpleClass",
                                                    "org.apache.jdo.tck.pc.fieldtypes.SimpleInterface",
                                                    "java.lang.String", "java.util.Date",
                                                    "java.util.Locale", "java.math.BigDecimal",
                                                    "java.math.BigInteger", "java.lang.Byte",
                                                    "java.lang.Double", "java.lang.Float",
                                                    "java.lang.Integer","java.lang.Long",
                                                    "java.lang.Short"};

  private StringBuffer fieldSpecs = new StringBuffer(2000);
  private StringBuffer isPersistent = new StringBuffer(200);
  private StringBuffer isStatic   = new StringBuffer(200);
  private StringBuffer isFinalArray = new StringBuffer(2000);
      
  // can accomodate only 4000 fields, have to increase the capacity if the fields exceed 4000
  private static boolean [] isFinal = new boolean[4000];

  void generate() throws Exception
  {
    final String twoSpaces = "  ";
    final String space = " ";

    for (int i=0; i < fieldTypes.length; i++)
    {
      String classFile = (isPrimitive(fieldTypes[i]) ? "FieldsOfPrimitive" : "FieldsOf") + fieldTypes[i];

      FileOutputStream xmlFout = new FileOutputStream(classFile + ".jdo.n");
      PrintWriter xmlPw = new PrintWriter(xmlFout);
      FileOutputStream xmlFout1 = new FileOutputStream(classFile + ".jdo.a");
      PrintWriter xmlPw1 = new PrintWriter(xmlFout1);
      FileOutputStream xmlFout2 = new FileOutputStream(classFile + ".jdo.d");
      PrintWriter xmlPw2 = new PrintWriter(xmlFout2);
      startXmlMetaData(xmlPw);
      startXmlMetaData(xmlPw1);
      startXmlMetaData(xmlPw2);

      startXmlClass(xmlPw,  classFile, 0);
      startXmlClass(xmlPw1, classFile, 1);
      startXmlClass(xmlPw2, classFile, 2);

      FileOutputStream fout = new FileOutputStream(classFile + ".java");
      PrintWriter pw = new PrintWriter(fout);
      startClass(pw, classFile);
      int fieldCounter = 0;
      for(int j=0; j < accessSpecifiers.length; j++)
      {
        for (int k=0; k < fieldModifiers.length; k++)
        {
          for(int l = 0; l < xmlPersistenceModifiers.length; l++)
          {
             // do not generate persistence modifiers (persistent or transactional or none)
             // for fields that cannot be persisted


         if( (fieldModifiers[k].indexOf("static") >= 0 || fieldModifiers[k].indexOf("final") >= 0 ) &&
            !xmlPersistenceModifiers[l].equals(""))
                continue;                
             
/*        original code
         if(!isPersistenceCapable(fieldModifiers[k])
                  && !xmlPersistenceModifiers[l].equals(""))
                continue;
*/
             for(int m=0; m < xmlEmbeddedModifiers.length; m++) {
                 // generate persistence modifiers (persistent or transactional or none)
                 // only for fields that can be persisted
                 // generate embedded modifiers only for persistent fields
                 
                 boolean fieldIsPersistent = !( fieldModifiers[k].indexOf("static") >= 0 ||
                                                fieldModifiers[k].indexOf("final")  >= 0 ||
                                                xmlPersistenceModifiers[l].indexOf("none") >= 0 ||
                                                xmlPersistenceModifiers[l].indexOf("transactional") >= 0 ||
                                                (fieldModifiers[k].indexOf("transient") >= 0 && xmlPersistenceModifiers[l].indexOf("persistent") == -1)
                                              ); 
                                                
                 if(!xmlEmbeddedModifiers[m].equals("") && !fieldIsPersistent )
                  continue;

                 StringBuffer sb = new StringBuffer();
                 sb.append(twoSpaces);
                 sb.append(accessSpecifiers[j]);
                 sb.append(fieldModifiers[k]);
                 sb.append(fieldTypes[i]);
                 sb.append(space);
                 String fieldName = (fieldTypes[i] + fieldCounter++);
//temporary fix to get around the bug in the enhancer code
if(!(xmlEmbeddedModifiers[m].equals("") &&  xmlPersistenceModifiers[l].equals("")))
{
                 printXmlField(xmlPw, "name=\"" + fieldName + "\" "
                               + xmlPersistenceModifiers[l]+ " "
                               + xmlEmbeddedModifiers[m]);
                 printXmlField(xmlPw1, "name=\"" + fieldName + "\" "
                               + xmlPersistenceModifiers[l]+ " "
                               + xmlEmbeddedModifiers[m]);
                 printXmlField(xmlPw2, "name=\"" + fieldName + "\" "
                               + xmlPersistenceModifiers[l]+ " "
                               + xmlEmbeddedModifiers[m]);
}//end temporary fix
                 sb.append(fieldName);
                 buildisPersistentArray(fieldIsPersistent); // add to isPersistentArray
                 buildisStaticArray(isStatic(fieldModifiers[k])); // add to isStaticArray
                 buildFieldSpecs(xmlPersistenceModifiers[l].replace('"',' ') + " " +
                                 xmlEmbeddedModifiers[m].replace('"',' ') +
                                 sb.toString()); // add to the field specs array
                 isFinal[fieldCounter-1] = fieldModifiers[k].indexOf("final") >= 0;
                 if(isFinal[fieldCounter-1])
                   sb.append(getInitializerForFinalTypes(fieldTypes[i]));
                 buildisFinalArray(isFinal[fieldCounter-1]);
                 sb.append(";");
                 pw.println(sb.toString());
             }
           }
         }
       }
       writeisPersistentArray(pw);
       writeisStaticArray(pw);
       writeisFinalArray(pw);
       writeFieldSpecs(pw);
       writeMethodGetLength(pw);
       writeMethodGet(pw, fieldTypes[i], fieldCounter);
       writeMethodSet(pw, fieldTypes[i], fieldCounter);
       endClass(pw);
       pw.close();
       fout.close();

       endXmlClass(xmlPw);
       endXmlClass(xmlPw1);
       endXmlClass(xmlPw2);
       endXmlMetaDeta(xmlPw);
       endXmlMetaDeta(xmlPw1);
       endXmlMetaDeta(xmlPw2);
       xmlPw.close();
       xmlFout.close();
       xmlPw1.close();
       xmlFout1.close();
       xmlPw2.close();
       xmlFout2.close();
     }
 }


  private void startClass(PrintWriter pw, String className)
  {
    pw.println("package org.apache.jdo.tck.pc.fieldtypes;");
    pw.println("");
    pw.println(getImportStatements(className));
    pw.println("public class " + className + " { " );
    pw.println("  public int identifier;");
  }

  private void endClass(PrintWriter pw)
  {
    pw.println("");
    pw.println("}");
  }

  private void startXmlMetaData(PrintWriter pw)
  {
    pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    pw.println("<!DOCTYPE jdo SYSTEM \"jdo.dtd\">");
    pw.println("<jdo>");
    pw.println("<package name=\"org.apache.jdo.tck.pc.fieldtypes\">");
  }

  private void endXmlMetaDeta(PrintWriter pw)
  {
    pw.println("</package>");
    pw.println("</jdo>");
  }

  private void startXmlClass(PrintWriter pw, String className, int type)
  {
    switch(type)
    {
      case(0):
        pw.println("<class name=\"" + className + "\">");
        break;
      case(1):
        pw.println("<class name=\"" + className + "\" identity-type=\"application\">");
        break;
      case(2):
        pw.println("<class name=\"" + className + "\" identity-type=\"datastore\">");
        break;
      default:
        System.out.println("Unsupported Type");
    }
  }

  private void endXmlClass(PrintWriter pw)
  {
    pw.println("</class>");
  }

  // need to check if we should support extents
  // fieldName includes modifiers
  private void printXmlField(PrintWriter pw, String fieldName)
  {
    pw.println("<field " + fieldName  + ">");
    pw.println("</field>");
  }

  // fieldname does not include any modifiers.
  // element type includes package informaiton.
  private void printXmlCollectionFieldWithEmbeddedElement(PrintWriter pw, String fieldName,
                                                          String elementType, String embeddedValue)
  {
    pw.println("<field name=\"" + fieldName + "\" " + ">");
    pw.print("<collection element-type=" + "\"" + elementType + "\"");
    if(!embeddedValue.equals(""))
      pw.print(" embedded-element=" + "\"" + embeddedValue + "\"");
    pw.println(">");
    pw.println("</collection>");
    pw.println("</field>");
  }

  private void printXmlArrayFieldWithEmbeddedElement(PrintWriter pw, String fieldName,
                                                     String embeddedValue)
  {
    pw.println("<field name=\"" + fieldName + "\" " + ">");
    pw.println("<array embedded-element=" +  "\"" + embeddedValue + "\"" + ">");
    pw.println("</array>");
    pw.println("</field>");
  }

  private void printXmlMapField(PrintWriter pw, String fieldName, String keyType,
                                String embeddedKey, String valueType, String embeddedValue)
  {
    pw.println("<field name=\"" + fieldName + "\" " + ">");
    pw.print("<map ");
    if(!keyType.equals(""))
      pw.print(" key-type=" + "\"" + keyType + "\"");
    if(!embeddedKey.equals(""))
      pw.print(" embedded-key=" + "\"" + embeddedKey + "\"");
    if(!valueType.equals(""))
      pw.print(" value-type=" + "\"" + valueType + "\"");
    if(!embeddedValue.equals(""))
      pw.print(" embedded-value=" + "\"" + embeddedValue + "\"");
    pw.println(">");
    pw.println("</map>");
    pw.println("</field>");
  }

  private String getInitializerForFinalTypes(String fieldType) throws Exception
  {
    if (fieldType.equals("char"))
      return " = 'a'"; //primitive character, return 'a';
    else if (fieldType.equals("boolean"))
      return " = false"; //primitive boolean, return 'a';
    else if (Character.isLowerCase(fieldType.charAt(0)))
      return " = 5"; // return 0 for all other primitive types
    else if (fieldType.equals("Byte"))
      return " = new Byte((byte)5)";
    else if (fieldType.equals("Boolean"))
      return " = new Boolean(false)";
    else if (fieldType.equals("Character"))
      return " = new Character('a')";
    else if (fieldType.equals("Short"))
      return " = new Short((short)5)";
    else if (fieldType.equals("Integer"))
      return " = new Integer((int)5)";
    else if (fieldType.equals("Long"))
      return " = new Long((long)5)";
    else if (fieldType.equals("Float"))
      return " = new Float((float)5)";
    else if (fieldType.equals("Double"))
      return " = new Double((double)5)";
    else if (fieldType.equals("String"))
      return " = new String(\"JDO TCK\")";
    else if (fieldType.equals("Locale"))
      return " = Locale.US";
    else if (fieldType.equals("BigDecimal"))
      return " = new BigDecimal(100.15)";
    else if (fieldType.equals("BigInteger"))
      return " = new BigInteger(\"100\")";
    else if (fieldType.equals("Collection"))
      return " = new HashSet()";
    else if (fieldType.equals("Set"))
      return " = new HashSet()";
    else if (fieldType.equals("HashSet"))
      return " = new HashSet()";
   else if (fieldType.equals("Object"))
      return " = new Object()";
    else if (fieldType.equals("SimpleClass"))
      return " = new SimpleClass()";
    else if (fieldType.equals("SimpleInterface"))
      return " = new SimpleClass()";
    else if (fieldType.equals("Date"))
      return " = new Date()";
    else
      throw new Exception("Unsupported FieldType " + fieldType);
  }

  private String getImportStatements(String fieldType)
  {
    if (fieldType.equals("FieldsOfLocale"))
      return "import java.util.*;";
    if (fieldType.equals("FieldsOfDate"))
      return "import java.util.*;";
    else if (fieldType.equals("FieldsOfBigDecimal"))
      return "import java.math.*;";
    else if (fieldType.equals("FieldsOfBigInteger"))
      return "import java.math.*;";
    else if (fieldType.equals("FieldsOfCollection"))
      return "import java.util.*;";
    else if (fieldType.equals("FieldsOfSet"))
      return "import java.util.*;";
    else if (fieldType.equals("FieldsOfHashSet"))
      return "import java.util.*;";
    else if (fieldType.indexOf("Collections") >=0)
      return "import java.util.*;\r\nimport java.math.*;";
    else
      return "";
  }

  private boolean isPrimitive(String fieldType)
  {
     if (Character.isUpperCase(fieldType.charAt(0)))
      return false;
     else
      return true;
  }

  private boolean isPersistenceCapable(String fieldModifier)
  {
     if (fieldModifier.indexOf("static") >= 0
         || fieldModifier.indexOf("final") >= 0
         || fieldModifier.indexOf("transient") >= 0)
       return false;
     else
       return true;
  }

 private boolean isStatic(String fieldModifier)
  {
     if (fieldModifier.indexOf("static") >= 0)
       return true;
     else
       return false;
  }
  private boolean isFinal(String fieldModifier)
  {
      return fieldModifier.indexOf("final") >= 0;
  }
  private void buildisPersistentArray(boolean value)
  {
    if(isPersistent.length() != 0)
      isPersistent.append(",");
    if(value)
      isPersistent.append("true");
    else
      isPersistent.append("false");
  }

  private void buildisStaticArray(boolean value)
  {
    if(isStatic.length() != 0)
      isStatic.append(",");
    if(value)
      isStatic.append("true");
    else
      isStatic.append("false");
  }

  private void buildisFinalArray(boolean value)
  {
    if(isFinalArray.length() != 0)
      isFinalArray.append(",");
    if(value)
      isFinalArray.append("true");
    else
      isFinalArray.append("false");
  }


  private void writeisPersistentArray(PrintWriter pw)
  {
     // have to go through this hoopla because pw.println(fieldSpecs.toString()); outputs only
     // 1024 characters
     char [] charArray = new char[isPersistent.length()];
     isPersistent.getChars(0,isPersistent.length(),charArray,0);
     pw.println("");
     pw.println("public static final boolean [] isPersistent = { ");

     int fieldCounter=0;
     for(int i = 0; i < charArray.length; i++)
     {
        pw.print(charArray[i]);
        if(charArray[i] == ',')
        {
          fieldCounter++;
          if(fieldCounter == 10)
          {
            pw.println("");
            pw.flush();
            fieldCounter = 0;
          }
        }
     }
     pw.println("");
     pw.println(" };");
     isPersistent = new StringBuffer(2000);
   }


  private void writeisStaticArray(PrintWriter pw)
  {
     // have to go through this hoopla because pw.println(fieldSpecs.toString()); outputs only
     // 1024 characters
     char [] charArray = new char[isStatic.length()];
     isStatic.getChars(0,isStatic.length(),charArray,0);
     pw.println("");
     pw.println("public static final boolean [] isStatic = { ");

     int fieldCounter=0;
     for(int i = 0; i < charArray.length; i++)
     {
        pw.print(charArray[i]);
        if(charArray[i] == ',')
        {
          fieldCounter++;
          if(fieldCounter == 10)
          {
            pw.println("");
            pw.flush();
            fieldCounter = 0;
          }
        }
     }
     pw.println("");
     pw.println(" };");
     isStatic = new StringBuffer(2000);
   }
  private void writeisFinalArray(PrintWriter pw)
  {
     // have to go through this hoopla because pw.println(fieldSpecs.toString()); outputs only
     // 1024 characters
     char [] charArray = new char[isFinalArray.length()];
     isFinalArray.getChars(0,isFinalArray.length(),charArray,0);
     pw.println("");
     pw.println("public static final boolean [] isFinal = { ");

     int fieldCounter=0;
     for(int i = 0; i < charArray.length; i++)
     {
        pw.print(charArray[i]);
        if(charArray[i] == ',')
        {
          fieldCounter++;
          if(fieldCounter == 10)
          {
            pw.println("");
            pw.flush();
            fieldCounter = 0;
          }
        }
     }
     pw.println("");
     pw.println(" };");
     isFinalArray = new StringBuffer(2000);
   }

  private void buildFieldSpecs(String field)
  {
    if(fieldSpecs.length() != 0)
      fieldSpecs.append(",");

    fieldSpecs.append("\"");
    fieldSpecs.append(field.trim());
    fieldSpecs.append("\"");
  }


  private void writeFieldSpecs(PrintWriter pw)
  {
    // have to go through this hoopla because pw.println(fieldSpecs.toString()); outputs only
    // 1024 characters
    char [] charArray = new char[fieldSpecs.length()];
    fieldSpecs.getChars(0,fieldSpecs.length(),charArray,0);
    pw.println("");
    pw.println("  public static final String [] fieldSpecs = { ");

    pw.print("  ");
    for(int i = 0; i < charArray.length; i++)
    {
       pw.print(charArray[i]);
       if(charArray[i] == ',')
       {
         pw.println("");
         pw.print("  ");
         pw.flush();
       }
    }
    pw.println("");
    pw.println("  };");
    fieldSpecs = new StringBuffer(2000);
  }

  private void writeMethodGetLength(PrintWriter pw)
  {
     pw.println("  public int getLength()");
     pw.println("  {");
     pw.println("    return fieldSpecs.length;");
     pw.println("  }");
  }

  private void writeMethodGet(PrintWriter pw, String fieldType, int numberOfFields)
  {
     pw.println("  public " +fieldType+ " get(int index)");
     pw.println("  {");

     pw.println("    switch (index)");
     pw.println("    {");
     for(int i = 0; i < numberOfFields; i++)
     {
       pw.println("      case(" + i + "):");
       pw.println("        return " + fieldType + i +";");
     }
     pw.println("      default:");
     pw.println("        throw new IndexOutOfBoundsException();");
     pw.println("    }");
     pw.println("  }");
  }

  private void writeMethodGet(PrintWriter pw, String fieldType, String[] fieldNames, int numFields)
  {
     pw.println("  public " +fieldType+ " get(int index)");
     pw.println("  {");

     pw.println("    switch (index)");
     pw.println("    {");
     for(int i = 0; i < numFields; i++)
     {
       pw.println("      case(" + i + "):");
       pw.println("        return " + fieldNames[i] +";");
     }
     pw.println("      default:");
     pw.println("        throw new IndexOutOfBoundsException();");
     pw.println("    }");
     pw.println("  }");
  }
  private void writeMethodSet(PrintWriter pw, String fieldType, int numberOfFields)
  {
     pw.println("  public boolean set(int index," + fieldType + " value"+ ")");
     pw.println("  {");
     pw.println("    if(fieldSpecs[index].indexOf(\"final\") != -1)");
     pw.println("      return false;");

     pw.println("    switch (index)");
     pw.println("    {");
     for(int i = 0; i < numberOfFields; i++)
     {
       if(!isFinal[i])
       {
         pw.println("      case(" + i + "):");
         pw.println("        " + fieldType + i + "= value" + ";");
         pw.println("         break;" );
       }
     }
     pw.println("      default:");
     pw.println("        throw new IndexOutOfBoundsException();");
     pw.println("    }");
     pw.println("    return true;");
     pw.println("  }");
  }

  private void writeMethodSet(PrintWriter pw, String fieldType, String [] fieldNames, int numFields)
  {
     pw.println("  public boolean set(int index," + fieldType + " value"+ ")");
     pw.println("  {");
     pw.println("    if(fieldSpecs[index].indexOf(\"final\") != -1)");
     pw.println("      return false;");

     pw.println("    switch (index)");
     pw.println("    {");
     for(int i = 0; i < numFields; i++)
     {
//       if(!isFinal[i]) {
         pw.println("      case(" + i + "):");
         pw.println("        " + fieldNames[i] + "= value" + ";");
         pw.println("         break;" );
//       }
     }
     pw.println("      default:");
     pw.println("        throw new IndexOutOfBoundsException();");
     pw.println("    }");
     pw.println("    return true;");
     pw.println("  }");
  }


  private void writeMethodSetForArray(PrintWriter pw, String fieldType, String [] fieldNames, int numFields)
  {
     pw.println("  public boolean set(int index," + fieldType + " value"+ ")");
     pw.println("  {");
     pw.println("    if(fieldSpecs[index].indexOf(\"final\") != -1)");
     pw.println("      return false;");

     pw.println("    switch (index)");
     pw.println("    {");
     for(int i = 0; i < numFields; i++)
     {
//       if(!isFinal[i]) {
         String fieldName = fieldNames[i];
         String valueType;
         pw.println("      case(" + i + "):");
         int indexOfValueType = fieldName.indexOf("Of") + 2;
         String valueTypeWithNumber = fieldName.substring(indexOfValueType);
         int lastIndexOfValueType = 0;
         for (int j=valueTypeWithNumber.length() -1; j>=0; j--)
         {
           if (Character.isDigit(valueTypeWithNumber.charAt(j)))
           {
             continue;
           }else {
            lastIndexOfValueType = j;
            break;
           }
         }
         valueType =  valueTypeWithNumber.substring(0, lastIndexOfValueType+1);
         pw.println("        " + fieldNames[i] + "= (" +valueType + " []) value ;");
         pw.println("         break;" );
//       }
     }
     pw.println("      default:");
     pw.println("        throw new IndexOutOfBoundsException();");
     pw.println("    }");
     pw.println("    return true;");
     pw.println("  }");
  }

  // generates Collection files for the different collectionTypes
  // also, updates the corresponding xml files
  // called by generate, after it is done dealing with the generic field types
  void generateCollections() throws Exception
  {
    final String [] embeddedElements = {"", "true", "false"};
    final String [] embeddedElementsForFieldSpec = {"", "embedded-element=true", "embedded-element=false"};
    for(int i=0; i < collectionTypes.length; i++)
    {
          // Map has a lot of combinations, generate it separately
      if(collectionTypes[i].indexOf("Map") >= 0 || collectionTypes[i].equals("Hashtable"))
      {
        generateMapCollection(collectionTypes[i]);
      }
      else // Array and the other collections
      {
        String classFile = collectionTypes[i] + "Collections";
        FileOutputStream fout = new FileOutputStream(classFile + ".java");
        PrintWriter pw = new PrintWriter(fout);
        FileOutputStream xmlFout = new FileOutputStream(classFile + ".jdo.n");
        PrintWriter xmlPw = new PrintWriter(xmlFout);
        FileOutputStream xmlFout1 = new FileOutputStream(classFile + ".jdo.a");
        PrintWriter xmlPw1 = new PrintWriter(xmlFout1);
        FileOutputStream xmlFout2 = new FileOutputStream(classFile + ".jdo.d");
        PrintWriter xmlPw2 = new PrintWriter(xmlFout2);

        startClass(pw, classFile);
        startXmlMetaData(xmlPw);
        startXmlMetaData(xmlPw1);
        startXmlMetaData(xmlPw2);

        startXmlClass(xmlPw,  classFile, 0);
        startXmlClass(xmlPw1, classFile, 1);
        startXmlClass(xmlPw2, classFile, 2);

        int fieldCounter=0;
        String fieldNames [] = new String [elementTypes.length * embeddedElements.length];
        for(int j=0; j < elementTypes.length; j++)
        {
            if( elementTypes[j].equals("Locale") && collectionTypes[i].equals("TreeSet") )
                continue;
          for(int k=0; k < embeddedElements.length; k++)
          {
            if(collectionTypes[i].equals("Array"))
            {
              if(!embeddedElements[k].equals(""))
              {
                fieldNames[fieldCounter] = collectionTypes[i] + "Of" + elementTypes[j] + fieldCounter;
//                pw.println("  public " + "Object [] "+ fieldNames[fieldCounter] +";");
                pw.println("  public " + elementTypes[j] +" [] " + fieldNames[fieldCounter] +";");
                printXmlArrayFieldWithEmbeddedElement(xmlPw,  fieldNames[fieldCounter], embeddedElements[k]);
                printXmlArrayFieldWithEmbeddedElement(xmlPw1, fieldNames[fieldCounter], embeddedElements[k]);
                printXmlArrayFieldWithEmbeddedElement(xmlPw2, fieldNames[fieldCounter], embeddedElements[k]);
                buildFieldSpecs(embeddedElementsForFieldSpec[k] + " " +
                                "public " + elementTypes[j] +" [] " + fieldNames[fieldCounter]); // add to the field specs array*/

                fieldCounter++;
              }
            }
            else // Collection
            {
              fieldNames[fieldCounter] = collectionTypes[i] + "Of" + elementTypes[j] + fieldCounter;
              pw.println("  public " + collectionTypes[i] +" "+ fieldNames[fieldCounter] +";");
              printXmlCollectionFieldWithEmbeddedElement(xmlPw, fieldNames[fieldCounter], elementsWithPackageInfo[j],
                                                         embeddedElements[k]);
              printXmlCollectionFieldWithEmbeddedElement(xmlPw1, fieldNames[fieldCounter], elementsWithPackageInfo[j],
                                                         embeddedElements[k]);
              printXmlCollectionFieldWithEmbeddedElement(xmlPw2, fieldNames[fieldCounter], elementsWithPackageInfo[j],
                                                         embeddedElements[k]);
              buildFieldSpecs(embeddedElementsForFieldSpec[k] + " " +
                              "public " + collectionTypes[i] +" "+ fieldNames[fieldCounter]); // add to the field specs array*/
              fieldCounter++;

            }
          }
        }
        writeFieldSpecs(pw);
        writeMethodGetLength(pw);
        writeMethodGet(pw, collectionTypes[i].equals("Array")? "Object [] " : collectionTypes[i], fieldNames, fieldCounter);
        if(collectionTypes[i].equals("Array"))
          writeMethodSetForArray(pw, "Object [] ", fieldNames, fieldCounter);
        else
         writeMethodSet(pw, collectionTypes[i], fieldNames, fieldCounter);

        endClass(pw);
        pw.close();
        fout.close();
        endXmlClass(xmlPw);
        endXmlClass(xmlPw1);
        endXmlClass(xmlPw2);
        endXmlMetaDeta(xmlPw);
        endXmlMetaDeta(xmlPw1);
        endXmlMetaDeta(xmlPw2);
        xmlPw.close();
        xmlFout.close();
        xmlPw1.close();
        xmlFout1.close();
        xmlPw2.close();
        xmlFout2.close();
      }
    }
  }

  private void generateMapCollection(String mapName) throws Exception
  {
    final String [] keyTypes1 = {"String"};
    final String [] valueTypes1 = {"Object", "SimpleClass", "SimpleInterface", "String",
                                "Date", "Locale", "BigDecimal", "BigInteger",
                                "Byte", "Double", "Float", "Integer", "Long",
                                "Short"};
    final String [] keyTypes2 = {"Object", "SimpleClass", "SimpleInterface", "String",
                                "Date", "BigDecimal", "BigInteger",
                                "Byte", "Double", "Float", "Integer", "Long",
                                "Short"};
    final String [] valueTypes2 = {"String"};

    final String [][] keyTypes = {keyTypes1, keyTypes2};
    final String [][] valueTypes = {valueTypes1, valueTypes2};

    String [] classNameArray =   {mapName + "StringKey" + "Collections",
                                  mapName + "StringValue" + "Collections"};

    for(int i=0; i < classNameArray.length; i++)
    {
      String classFile = classNameArray[i];
      FileOutputStream fout = new FileOutputStream(classFile + ".java");
      PrintWriter pw = new PrintWriter(fout);
      FileOutputStream xmlFout = new FileOutputStream(classFile + ".jdo.n");
      PrintWriter xmlPw = new PrintWriter(xmlFout);
      FileOutputStream xmlFout1 = new FileOutputStream(classFile + ".jdo.a");
      PrintWriter xmlPw1 = new PrintWriter(xmlFout1);
      FileOutputStream xmlFout2 = new FileOutputStream(classFile + ".jdo.d");
      PrintWriter xmlPw2 = new PrintWriter(xmlFout2);

      startClass(pw, classFile);
      startXmlMetaData(xmlPw);
      startXmlMetaData(xmlPw1);
      startXmlMetaData(xmlPw2);

      startXmlClass(xmlPw,  classFile, 0);
      startXmlClass(xmlPw1, classFile, 1);
      startXmlClass(xmlPw2, classFile, 2);

      fillMapCollection(keyTypes[i], valueTypes[i],mapName, pw, xmlPw, xmlPw1, xmlPw2);

      endClass(pw);
      pw.close();
      fout.close();
      endXmlClass(xmlPw);
      endXmlClass(xmlPw1);
      endXmlClass(xmlPw2);
      endXmlMetaDeta(xmlPw);
      endXmlMetaDeta(xmlPw1);
      endXmlMetaDeta(xmlPw2);
      xmlPw.close();
      xmlFout.close();
      xmlPw1.close();
      xmlFout1.close();
      xmlPw2.close();
      xmlFout2.close();
    }
  }

  private void fillMapCollection(String [] keyTypes, String [] valueTypes,
                                 String mapName, PrintWriter pw, PrintWriter xmlPw,
                                 PrintWriter xmlPw1, PrintWriter xmlPw2)
  {
    final String [] embeddedKeys = {"", "true", "false"};
    final String [] embeddedValues = embeddedKeys;
    final String [] embeddedKeyForFieldSpec = {"", "embedded-key=true", "embedded-key=false"};
    final String [] embeddedValueForFieldSpec = {"", "embedded-value=true", "embedded-value=false"};

    int fieldCounter=0;
    String fieldNames [] = new String [keyTypes.length * embeddedKeys.length
                                       * valueTypes.length * embeddedValues.length];
    for(int i = 0; i < keyTypes.length; i++)
    {
      for(int j = 0; j < embeddedKeys.length; j++)
      {
        for(int k = 0; k < valueTypes.length; k++)
        {
          for(int l = 0; l < embeddedValues.length; l++)
          {
            if( keyTypes[i].equals("") && embeddedKeys[j].equals("")
              && valueTypes[k].equals("") && embeddedValues[l].equals(""))
              continue;
            fieldNames[fieldCounter] = mapName+ "Of" + keyTypes[i] +"_"+valueTypes[k] + fieldCounter;
            pw.println("  public " + mapName +" " + fieldNames[fieldCounter] +";");
            printXmlMapField(xmlPw, fieldNames[fieldCounter], keyTypes[i], embeddedKeys[j],
                             valueTypes[k], embeddedValues[l]);
            printXmlMapField(xmlPw1, fieldNames[fieldCounter], keyTypes[i], embeddedKeys[j],
                             valueTypes[k], embeddedValues[l]);
            printXmlMapField(xmlPw2, fieldNames[fieldCounter], keyTypes[i], embeddedKeys[j],
                             valueTypes[k], embeddedValues[l]);
            buildFieldSpecs(embeddedKeyForFieldSpec[j] + " " +
                            embeddedValueForFieldSpec[l] + " " +
                            "public " + mapName +" "+ fieldNames[fieldCounter]); // add to the field specs array*/
            fieldCounter++;

           }
        }
      }
    }
    writeFieldSpecs(pw);
    writeMethodGetLength(pw);
    writeMethodGet(pw, mapName, fieldNames, fieldCounter);
    writeMethodSet(pw, mapName, fieldNames, fieldCounter);
  }

  public static void main(String[] args)
  {
    ClassGenerator classGenerator = new ClassGenerator();
    try
    {
        classGenerator.generate();
        classGenerator.generateCollections();
    }
    catch (Exception e)
    {
      System.out.println(e);
      e.printStackTrace();
    }
  }
}
