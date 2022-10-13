/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.springframework.beans.propertyeditors.CustomDateEditor;

public class JDOCustomDateEditor extends CustomDateEditor {

  /** The format of date values in the xml representation */
  public static final String DATE_PATTERN = "d/MMM/yyyy";

  public JDOCustomDateEditor() {
    super(new SimpleDateFormat(DATE_PATTERN, Locale.US), true);
  }

  /**
   * Redturs a string representation of the specified date using DATE_PATTERN as date formatter
   * pattern.
   *
   * @param date the date
   * @return string representation of the specified date
   */
  public static String getDateRepr(Date date) {
    return date == null ? "null" : new SimpleDateFormat(DATE_PATTERN).format(date);
  }
}
