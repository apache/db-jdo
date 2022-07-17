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
package org.apache.jdo.tck.pc.query;

import java.io.Serializable;
import java.math.BigDecimal;

public class MathSample implements Serializable {
	private static final long serialVersionUID = 1L;

	long id;

	/** Angle in radians. */
	BigDecimal angle;

    BigDecimal trigValue;
    Double doubleValue;
    Float floatValue;
    Integer intValue;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAngle() {
		return angle;
	}
	public void setAngle(BigDecimal angle) {
		this.angle = angle;
	}

	public BigDecimal getTrigValue() {
		return trigValue;
	}
	public void setTrigValue(BigDecimal val) {
		this.trigValue = val;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double val) {
		this.doubleValue = val;
	}

	public Float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(Float val) {
		this.floatValue = val;
	}

	public Integer getIntValue() {
		return intValue;
	}
	public void setIntValue(Integer val) {
		this.intValue = val;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intValue == null) ? 0 : intValue.hashCode());
		result = prime * result + ((floatValue == null) ? 0 : floatValue.hashCode());
		result = prime * result + ((doubleValue == null) ? 0 : doubleValue.hashCode());
		result = prime * result + ((trigValue == null) ? 0 : trigValue.hashCode());
		result = prime * result + ((angle == null) ? 0 : angle.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MathSample other = (MathSample) obj;

		if (intValue == null) {
			if (other.intValue != null)
				return false;
		} else if (!intValue.equals(other.intValue))
			return false;

		if (floatValue == null) {
			if (other.floatValue != null)
				return false;
		} else if (!floatValue.equals(other.floatValue))
			return false;

		if (doubleValue == null) {
			if (other.doubleValue != null)
				return false;
		} else if (!doubleValue.equals(other.doubleValue))
			return false;

		if (trigValue == null) {
			if (other.trigValue != null)
				return false;
		} else if (!trigValue.equals(other.trigValue))
			return false;

		if (angle == null) {
			if (other.angle != null)
				return false;
		} else if (!angle.equals(other.angle))
			return false;

		if (id != other.id)
			return false;
		return true;
	}
}
