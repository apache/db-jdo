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

package org.apache.jdo.tck.api.persistencemanagerfactory.metadata;

import javax.jdo.metadata.ColumnMetadata;
import javax.jdo.metadata.MemberMetadata;
import javax.jdo.metadata.Metadata;
import javax.jdo.metadata.PackageMetadata;
import javax.jdo.metadata.TypeMetadata;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Metadata Test <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A11.10-1 <br>
 * <B>Assertion Description: </B> Method to return the metadata object for the specified
 * class/interface, if there is metadata defined for that class/interface. If the parameter is null,
 * null is returned. If there is no metadata for the specified class/interface then null will be
 * returned.
 */
public class GetMetadataTest extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A11.10-1 (getMetadata) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetMetadataTest.class);
  }

  /** test getMetadata with null class name */
  public void testNullParameter() {
    pm = getPM();
    String className = null;
    TypeMetadata metadata = pmf.getMetadata(className);
    assertNull(ASSERTION_FAILED + "metadata should return null if null parameter", metadata);
  }

  /** test getMetadata with concrete class name */
  public void testConcreteClassName() {
    pm = getPM();
    String className = "org.apache.jdo.tck.pc.company.PartTimeEmployee";
    TypeMetadata metadata = pmf.getMetadata(className);
    assertNotNull(
        ASSERTION_FAILED + "metadata must not be null for abstract class " + className, metadata);
    Metadata parent = metadata.getParent();
    assertNotNull(
        ASSERTION_FAILED + "parent metadata must not be null for concrete class " + className,
        parent);
    String name = metadata.getName();
    String packageName = ((PackageMetadata) parent).getName();
    assertEquals(
        ASSERTION_FAILED + "metadata name and class name must match.",
        className,
        packageName + "." + name);
  }

  /** test getMetadata with abstract class name */
  public void testAbstractClassName() {
    pm = getPM();
    String className = "org.apache.jdo.tck.pc.company.Employee";
    TypeMetadata metadata = pmf.getMetadata(className);
    assertNotNull(
        ASSERTION_FAILED + "metadata must not be null for abstract class " + className, metadata);
    Metadata parent = metadata.getParent();
    assertNotNull(
        ASSERTION_FAILED + "parent metadata must not be null for abstract class " + className,
        parent);
    String name = metadata.getName();
    String packageName = ((PackageMetadata) parent).getName();
    assertEquals(
        ASSERTION_FAILED + "metadata name and class name must match.",
        className,
        packageName + "." + name);
  }

  /** test getMetadata with invalid class name */
  public void testInvalidName() {
    pm = getPM();
    String className = "org.apache.jdo.tck.pc.company.Bogus";
    TypeMetadata metadata = pmf.getMetadata(className);
    assertNull(ASSERTION_FAILED + "metadata should return null if unknown parameter", metadata);
  }

  /** dump the contents of this metadata */
  @SuppressWarnings("unused")
  private void dump(TypeMetadata typeMetadata) {
    StringBuilder buffer = new StringBuilder("Metadata for " + typeMetadata.getName());
    buffer.append("  identityType: " + typeMetadata.getIdentityType());
    MemberMetadata[] members = typeMetadata.getMembers();
    if (members != null) {
      for (MemberMetadata memberMetadata : members) {
        buffer.append(" member: " + memberMetadata.getName());
      }
    }
    ColumnMetadata[] columns = typeMetadata.getColumns();
    if (columns != null) {
      for (ColumnMetadata columnMetadata : columns) {
        buffer.append("ColumnMetadata for " + columnMetadata.getName());
      }
    }
    System.out.println(buffer);
  }
}
