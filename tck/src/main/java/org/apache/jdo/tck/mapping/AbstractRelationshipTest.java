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

package org.apache.jdo.tck.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.jdo.tck.AbstractReaderTest;
import org.apache.jdo.tck.pc.company.CompanyFactoryRegistry;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;

/*
 * Abstract class for managed relationship tests
 */
public class AbstractRelationshipTest extends AbstractReaderTest {

  protected CompanyModelReader reader = null;

  /** */
  protected final boolean isTestToBePerformed = isTestToBePerformed();

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    if (isTestToBePerformed()) {
      getPM();
      CompanyFactoryRegistry.registerFactory(pm);
      reader = new CompanyModelReader(inputFilename);
      addTearDownClass(reader.getTearDownClassesFromFactory());
      // persist test data
      pm.currentTransaction().begin();
      List rootList = getRootList(reader);
      pm.makePersistentAll(rootList);
      rootOids = new ArrayList();
      for (Iterator i = rootList.iterator(); i.hasNext(); ) {
        Object pc = i.next();
        rootOids.add(pm.getObjectId(pc));
      }
      // DO THIS
      // in xmlBeanFactory String[] getBeanDefinitionNames()
      oidMap.put("emp1", pm.getObjectId(reader.getEmployee("emp1")));
      oidMap.put("emp2", pm.getObjectId(reader.getEmployee("emp2")));
      oidMap.put("emp4", pm.getObjectId(reader.getEmployee("emp4")));
      oidMap.put("medicalIns1", pm.getObjectId(reader.getMedicalInsurance("medicalIns1")));
      oidMap.put("medicalIns2", pm.getObjectId(reader.getMedicalInsurance("medicalIns2")));
      oidMap.put("dept1", pm.getObjectId(reader.getDepartment("dept1")));
      oidMap.put("dept2", pm.getObjectId(reader.getDepartment("dept2")));
      oidMap.put("proj1", pm.getObjectId(reader.getProject("proj1")));
      oidMap.put("proj2", pm.getObjectId(reader.getProject("proj2")));

      pm.currentTransaction().commit();
      cleanupPM();
    }
  }

  protected boolean containsEmployee(Collection<Employee> employees, long empid) {
    for (Employee emp : employees) {
      if (emp.getPersonid() == empid) {
        return true;
      }
    }
    return false;
  }

  protected boolean containsProject(Collection<Project> projects, long projid) {
    for (Project project : projects) {
      if (project.getProjid() == projid) {
        return true;
      }
    }
    return false;
  }
}
