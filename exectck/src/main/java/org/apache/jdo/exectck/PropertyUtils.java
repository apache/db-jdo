/*
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/** Helper class that sets properties required for running the JDO TCK. */
public class PropertyUtils {

  private PropertyUtils() {
    // This method is deliberately left empty.
  }

  /**
   * Separates white space separated items from a String into a Set.
   *
   * <p>Used to collect command line arguments.
   *
   * @param names String of white space separated items
   * @param col collection to contain String items
   */
  public static void string2Collection(String names, Collection<String> col) {
    String[] items = names.split(Utilities.DELIMITER_REGEX);
    col.addAll(Arrays.asList(items));
  }

  /**
   * Parses a set of config files for the mapping entry and provides the mapping values in a
   * Collection.
   *
   * @param cfglist config file names
   * @param confDir directory where config files are found
   * @param mappings object to containg mapping values
   */
  public static void mappingsSet(
      Collection<String> cfglist, String confDir, Collection<String> mappings) {

    for (String cfg : cfglist) {
      String mapping = "";
      String confName = confDir + File.separator + cfg;
      if (!new File(confName).exists()) {
        // Conf file nor found => continue
        System.out.println("ERROR: Configuration file " + confName + " not found.");
        continue;
      }

      Properties props = new Properties();
      try (FileInputStream fis = new FileInputStream(confName)) {
        props.load(fis);
        mapping = props.getProperty("jdo.tck.mapping");
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      mappings.add(mapping);
    }
  }

  /*
   * Open a properties file and return a Properties object
   */
  public static Properties getProperties(String fname) {
    Properties props = new Properties();
    try (FileInputStream fis = new FileInputStream(fname)) {
      props.load(fis);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return props;
  }
}
