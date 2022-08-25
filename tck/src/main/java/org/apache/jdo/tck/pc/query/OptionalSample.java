/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.query;

import java.util.Date;
import java.util.Optional;

public class OptionalSample {
  long id;

  Optional<OptionalSample> optionalPC;

  Optional<Date> optionalDate;
  Optional<Integer> optionalInteger;
  Optional<String> optionalString;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Optional<OptionalSample> getOptionalPC() {
    return optionalPC;
  }

  public void setOptionalPC(Optional<OptionalSample> opt) {
    this.optionalPC = opt;
  }

  public Optional<Date> getOptionalDate() {
    return optionalDate;
  }

  public void setOptionalDate(Optional<Date> opt) {
    this.optionalDate = opt;
  }

  public Optional<Integer> getOptionalInteger() {
    return optionalInteger;
  }

  public void setOptionalInteger(Optional<Integer> opt) {
    this.optionalInteger = opt;
  }

  public Optional<String> getOptionalString() {
    return optionalString;
  }

  public void setOptionalString(Optional<String> opt) {
    this.optionalString = opt;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((optionalPC == null) ? 0 : optionalPC.hashCode());
    result = prime * result + ((optionalString == null) ? 0 : optionalString.hashCode());
    result = prime * result + ((optionalInteger == null) ? 0 : optionalInteger.hashCode());
    result = prime * result + ((optionalDate == null) ? 0 : optionalDate.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    OptionalSample other = (OptionalSample) obj;
    if (id != other.id) return false;

    if (optionalPC == null) {
      if (other.optionalPC != null) return false;
    } else if (!optionalPC.equals(other.optionalPC)) return false;

    if (optionalDate == null) {
      if (other.optionalDate != null) return false;
    } else if (!optionalDate.equals(other.optionalDate)) return false;

    if (optionalInteger == null) {
      if (other.optionalInteger != null) return false;
    } else if (!optionalInteger.equals(other.optionalInteger)) return false;

    if (optionalString == null) {
      if (other.optionalString != null) return false;
    } else if (!optionalString.equals(other.optionalString)) return false;

    return true;
  }

  @Override
  public String toString() {
    return "id=" + id + " " + super.toString();
  }
}
