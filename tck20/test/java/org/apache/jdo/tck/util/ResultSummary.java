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

package org.apache.jdo.tck.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import javax.jdo.JDOFatalException;

import junit.framework.TestResult;

/**
 * A serializable class used to store test results in a file.
 */
public class ResultSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /** The name of the file to store a serialized instance of this class. */
    private static final String FILE_NAME_OF_RESULT_SUMMARY = "ResultSummary.ser";
    
    /** The name of the TCK result file. */
    private static final String RESULT_FILE_NAME = "TCK-results.txt";
    
    /* The number of all configurations. */
    private int nrOfTotalConfigurations = 0;
    
    /* The number of failed configurations. */
    private int nrOfFailedConfigurations = 0;
    
    /**
     * Deserializes an instance and prints that instance to
     * {@link System#out} and the TCK result file.
     * Finally deletes the file of the serialized instance. 
     * @param args the first element contains the directory 
     * where the test result is stored.
     */
    public static void main(String[] args) {
        // print result summary
        
        String directory = args[0] + File.separator;
        ResultSummary resultSummary = ResultSummary.load(directory);
        String newLine = System.getProperty("line.separator");
        String resultMessage = "Result: " + resultSummary; 
        String message = "-------" + newLine + resultMessage;
        appendTCKResultMessage(directory, message);
        System.out.println(resultMessage);
        System.out.println("See file '"+ directory + RESULT_FILE_NAME + 
                "' for details.");
        
        // delete file
        String fileName = args[0] + File.separator + FILE_NAME_OF_RESULT_SUMMARY;
        File file = new File(fileName);
        file.delete();
    }
    
    /**
     * Appends the given message to the TCK result file in the given directory. 
     * @param directory the directory
     * @param message the message
     */
    static void appendTCKResultMessage(String directory, String message) {
        String fileName = directory + RESULT_FILE_NAME;
        PrintStream resultStream = null;
        try {
            resultStream = new PrintStream(
                    new FileOutputStream(fileName, true));
            resultStream.println(message);
        } catch (FileNotFoundException e) {
            throw new JDOFatalException("Cannot create file "+fileName, e);
        } finally {
            if (resultStream != null)
                resultStream.close();
        }
    }

    /**
     * Creates an instance for the given result object and
     * serializes that instance to a file in the geven directory.
     * @param directory the directory
     * @param result the result object
     */
    static void save(String directory, TestResult result) {
        ResultSummary resultSummary = load(directory);
        if (resultSummary == null) {
            resultSummary = new ResultSummary();
        }
        resultSummary.increment(result);
        resultSummary.save(directory);
    }
    
    /**
     * Returns a deserialized instance stored in the given direcotry.
     * @param directory the directory
     * @return the deserialized instance
     */
    private static ResultSummary load(String directory) {
        ResultSummary result;
        String fileName = directory + FILE_NAME_OF_RESULT_SUMMARY;
        ObjectInputStream ois = null;
        try {
            try {
                ois = new ObjectInputStream(new FileInputStream(fileName));
                result = (ResultSummary) ois.readObject();
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        } catch (FileNotFoundException e) {
            result = null;
        } catch (IOException e) {
            throw new JDOFatalException(
                    "Cannot deserialize result summary in file "
                    +fileName, e);
        } catch (ClassNotFoundException e) {
            throw new JDOFatalException(
                    "Cannot deserialize result summary in file "
                    +fileName, e);
        }
        return result;
    }
    
    /**
     * Increments fields of this instance based on the given result object.
     * @param result the result object
     */
    private void increment(TestResult result) {
        this.nrOfTotalConfigurations++;
        if (!result.wasSuccessful()) {
            this.nrOfFailedConfigurations++;
        }
    }
    
    /**
     * Serializes this instance to a file in the given directory.
     * @param directory the directory
     */
    private void save(String directory) {
        String fileName = directory + FILE_NAME_OF_RESULT_SUMMARY;
        ObjectOutputStream oos = null;
        try {
            try {
                oos = new ObjectOutputStream(new FileOutputStream(fileName));
                oos.writeObject(this);
            } finally {
                if (oos != null) {
                    oos.close();
                }
            }
        } catch (FileNotFoundException e) {
            throw new JDOFatalException(
                    "Cannot create file " + fileName, e);
        } catch (IOException e) {
            throw new JDOFatalException(
                    "Cannot serialize result summary to file "
                    + fileName, e);
        }
    }
    
    /**
     * @see Object#toString()
     */
    public String toString() {
        String result;
        if (this.nrOfFailedConfigurations==0) {
            result = "All (" + this.nrOfTotalConfigurations + ") configurations passed.";
        } else {
            result = String.valueOf(this.nrOfFailedConfigurations) + " of " +
                     this.nrOfTotalConfigurations + " configurations failed.";
        }
        return result;
    }
}
