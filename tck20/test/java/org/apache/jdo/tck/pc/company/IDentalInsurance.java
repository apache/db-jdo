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

package org.apache.jdo.tck.pc.company;

import java.math.BigDecimal;

/**
 * This interface represents the persistent state of DentalInsurance.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
public interface IDentalInsurance extends IInsurance {

    BigDecimal getLifetimeOrthoBenefit();
    void setLifetimeOrthoBenefit(BigDecimal lifetimeOrthoBenefit);
}
