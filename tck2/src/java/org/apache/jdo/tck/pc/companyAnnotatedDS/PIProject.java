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
package org.apache.jdo.tck.pc.companyAnnotatedDS;

import javax.jdo.annotations.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * This interface represents the persistent state of Project.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable
@Table(table="projects")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR")
public interface PIProject {

    @Field(primaryKey="true")
    @Column(name="PROJID")
    long getProjid();
    @Column(name="NAME")
    String getName();
    @Column(name="BUDGET", jdbcType="DECIMAL", length=11, scale=2)
    BigDecimal getBudget();
    @Field(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedDS.FCEmployee.class,
            column="REVIEWER")
    @Join(column="PROJID", table="proj_reviewer")
    //@Join(column="PROJID", foreignKey=@ForeignKey(name="PR_PROJ_FK"))
    Set getReviewers();
    @Field(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedDS.FCEmployee.class,
            column="MEMBER")
    //@Element(types=org.apache.jdo.tck.pc.companyAnnotatedDS.FCEmployee.class,
    //    foreignKey=@ForeignKey(name="PR_REV_FK"))
    @Join(column="PROJID", table="proj_member")
    Set getMembers();
    
    void setProjid(long projid);
    void setName(String name);
    void setBudget(BigDecimal budget);
    @Field(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedDS.PIEmployee.class)
    void setReviewers(Set reviewers);
    @Field(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedDS.PIEmployee.class)
    void setMembers(Set employees);
    
}
