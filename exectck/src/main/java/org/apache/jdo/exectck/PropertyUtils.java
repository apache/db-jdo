/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.jdo.exectck;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Helper class that sets properties required for running the JDO TCK.
 *
 */
public class PropertyUtils {

    /**
     * Separates white space separated items from a String into HashSet entries
     * Used to collect command line argument lists into a Collection
     *
     * @param names String of white space separated items
     * @param list  HashSet to contain String items
     */
    public static void string2Set(String names, HashSet<String> list) {
//        System.out.println("names are " + names);
        String[] items = names.split("[ \t\n]");
        for (String s : items) {
            list.add(s);
        }
//        System.out.println("List names are " + list.toString());
    }

    /**
     * Separates white space separated items from a String into HashSet entries
     * Used to collect command line argument lists into a Collection
     *
     * @param names String of white space separated items
     * @param list  HashSet to contain String items
     */
    public static void string2List(String names, ArrayList<String> list) {
//        System.out.println("names are " + names);
        String[] items = names.split("[ \t\n]");
        for (String s : items) {
            list.add(s);
        }
//        System.out.println("List names are " + list.toString());
    }

    /**
     * Parses a set of config files for the mapping entry and
     * provides the mapping values in a HashSet<String>.
     * @param cfglist config file names
     * @param confDir directory where config files are found
     * @param mappings object to containg mapping values
     */
    public static void mappingsSet(HashSet<String> cfglist, String confDir,
            HashSet<String> mappings) {

        for (String cfg : cfglist) {
            String mapping = "";
            String confName = confDir + "/" + cfg;

            Properties props = new Properties();
            FileInputStream fis = null;
//            System.out.println("confName is " + confName);
            try {
                fis = new FileInputStream(confName);
                props.load(fis);
                mapping = props.getProperty("jdo.tck.mapping");
//                System.out.println("Next mapping is " + mapping);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PropertyUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }

            mappings.add(mapping);
        }
    }

    /*
     * Open a properties file and return a Properties object
     */
    public static Properties getProperties(String fname){
//        System.out.println("Goal RunTCK, getProperties: parsing properties from "
//                + fname);
        Properties props = new Properties();
        FileInputStream fis = null;
            try {
                fis = new FileInputStream(new File(fname));
                props.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PropertyUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return props;
    }

    /*
     * Set the value of key if found in properties.
     */
    public static void setSysProperty(Properties properties, String key, String newkey) {
        String value = properties.getProperty("key");
        if (value != null) {
            System.setProperty(newkey, value);
        }
    }

    public static void setSysProperty(Properties properties, String key) {
        setSysProperty(properties, key, key);
    }
}
