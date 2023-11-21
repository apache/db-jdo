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
package org.apache.jdo.tck.api.exceptions;

import java.lang.reflect.Constructor;
import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Exception Constructor <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion ID:</B> <br>
 * <B>Assertion Description: </B>
 */
@SuppressWarnings("rawtypes")
public class ExceptionConstructor extends JDO_Test {

  protected static final Class<?>[] classArrayEmpty = new Class[] {};
  protected static final Class<?>[] classArrayString = new Class[] {String.class};
  protected static final Class<?>[] classArrayStringObject =
      new Class[] {String.class, Object.class};
  protected static final Class<?>[] classArrayStringThrowable =
      new Class[] {String.class, Throwable.class};
  protected static final Class<?>[] classArrayStringThrowableArray =
      new Class[] {String.class, Throwable[].class};
  protected static final Class<?>[] classArrayStringThrowableObject =
      new Class[] {String.class, Throwable.class, Object.class};
  protected static final Class<?>[] classArrayStringThrowableArrayObject =
      new Class[] {String.class, Throwable[].class, Object.class};
  protected static final String message = "Message";
  protected static final Object object = "Failed Object";
  protected static final Throwable throwable = new Throwable("Throwable");
  protected static final Object[] objectArrayEmpty = new Object[] {};
  protected static final Object[] objectArrayString = new Object[] {message};
  protected static final Object[] objectArrayStringObject = new Object[] {message, object};
  protected static final Object[] objectArrayStringThrowableArray =
      new Object[] {message, new Throwable[] {throwable}};
  protected static final Object[] objectArrayStringThrowable = new Object[] {message, throwable};
  protected static final Object[] objectArrayStringThrowableArrayObject =
      new Object[] {message, new Throwable[] {throwable}, object};
  protected static final Object[] objectArrayStringThrowableObject =
      new Object[] {message, throwable, object};

  /* test all constructors
   *
   */
  @Test
  public void testConstructors() {
    constructJDOCanRetryException();
    constructJDODataStoreException();
    constructJDODetachedFieldAccessException();
    constructJDOException();
    constructJDOFatalDataStoreException();
    constructJDOFatalException();
    constructJDOFatalInternalException();
    constructJDOFatalUserException();
    constructJDONullIdentityException();
    constructJDOObjectNotFoundException();
    constructJDOOptimisticVerificationException();
    constructJDOUnsupportedOptionException();
    constructJDOUserCallbackException();
    failOnError();
  }

  protected void constructJDOCanRetryException() {
    constructEmpty(javax.jdo.JDOCanRetryException.class);
    constructString(javax.jdo.JDOCanRetryException.class);
    constructStringThrowableArray(javax.jdo.JDOCanRetryException.class);
    constructStringThrowable(javax.jdo.JDOCanRetryException.class);
    constructStringObject(javax.jdo.JDOCanRetryException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOCanRetryException.class);
    constructStringThrowableObject(javax.jdo.JDOCanRetryException.class);
  }

  protected void constructJDODataStoreException() {
    constructEmpty(javax.jdo.JDODataStoreException.class);
    constructString(javax.jdo.JDODataStoreException.class);
    constructStringThrowableArray(javax.jdo.JDODataStoreException.class);
    constructStringThrowable(javax.jdo.JDODataStoreException.class);
    constructStringObject(javax.jdo.JDODataStoreException.class);
    constructStringThrowableArrayObject(javax.jdo.JDODataStoreException.class);
    constructStringThrowableObject(javax.jdo.JDODataStoreException.class);
  }

  protected void constructJDODetachedFieldAccessException() {
    constructEmpty(javax.jdo.JDODetachedFieldAccessException.class);
    constructString(javax.jdo.JDODetachedFieldAccessException.class);
    constructStringThrowableArray(javax.jdo.JDODetachedFieldAccessException.class);
    constructStringThrowable(javax.jdo.JDODetachedFieldAccessException.class);
    constructStringObject(javax.jdo.JDODetachedFieldAccessException.class);
  }

  protected void constructJDOException() {
    constructEmpty(javax.jdo.JDOException.class);
    constructString(javax.jdo.JDOException.class);
    constructStringThrowableArray(javax.jdo.JDOException.class);
    constructStringThrowable(javax.jdo.JDOException.class);
    constructStringObject(javax.jdo.JDOException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOException.class);
    constructStringThrowableObject(javax.jdo.JDOException.class);
  }

  protected void constructJDOFatalDataStoreException() {
    constructEmpty(javax.jdo.JDOFatalDataStoreException.class);
    constructString(javax.jdo.JDOFatalDataStoreException.class);
    constructStringThrowableArray(javax.jdo.JDOFatalDataStoreException.class);
    constructStringThrowable(javax.jdo.JDOFatalDataStoreException.class);
    constructStringObject(javax.jdo.JDOFatalDataStoreException.class);
  }

  protected void constructJDOFatalException() {
    constructEmpty(javax.jdo.JDOFatalException.class);
    constructString(javax.jdo.JDOFatalException.class);
    constructStringThrowableArray(javax.jdo.JDOFatalException.class);
    constructStringThrowable(javax.jdo.JDOFatalException.class);
    constructStringObject(javax.jdo.JDOFatalException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOFatalException.class);
    constructStringThrowableObject(javax.jdo.JDOFatalException.class);
  }

  protected void constructJDOFatalInternalException() {
    constructEmpty(javax.jdo.JDOFatalInternalException.class);
    constructString(javax.jdo.JDOFatalInternalException.class);
    constructStringThrowableArray(javax.jdo.JDOFatalInternalException.class);
    constructStringThrowable(javax.jdo.JDOFatalInternalException.class);
    constructStringObject(javax.jdo.JDOFatalInternalException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOFatalInternalException.class);
    constructStringThrowableObject(javax.jdo.JDOFatalInternalException.class);
  }

  protected void constructJDOFatalUserException() {
    constructEmpty(javax.jdo.JDOFatalUserException.class);
    constructString(javax.jdo.JDOFatalUserException.class);
    constructStringThrowableArray(javax.jdo.JDOFatalUserException.class);
    constructStringThrowable(javax.jdo.JDOFatalUserException.class);
    constructStringObject(javax.jdo.JDOFatalUserException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOFatalUserException.class);
    constructStringThrowableObject(javax.jdo.JDOFatalUserException.class);
  }

  protected void constructJDONullIdentityException() {
    constructEmpty(javax.jdo.JDONullIdentityException.class);
    constructString(javax.jdo.JDONullIdentityException.class);
    constructStringThrowableArray(javax.jdo.JDONullIdentityException.class);
    constructStringThrowable(javax.jdo.JDONullIdentityException.class);
    constructStringObject(javax.jdo.JDONullIdentityException.class);
  }

  protected void constructJDOObjectNotFoundException() {
    constructEmpty(javax.jdo.JDOObjectNotFoundException.class);
    constructString(javax.jdo.JDOObjectNotFoundException.class);
    constructStringThrowableArray(javax.jdo.JDOObjectNotFoundException.class);
    constructStringObject(javax.jdo.JDOObjectNotFoundException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOObjectNotFoundException.class);
    constructStringThrowableObject(javax.jdo.JDOObjectNotFoundException.class);
  }

  protected void constructJDOOptimisticVerificationException() {
    constructEmpty(javax.jdo.JDOOptimisticVerificationException.class);
    constructString(javax.jdo.JDOOptimisticVerificationException.class);
    constructStringThrowableArray(javax.jdo.JDOOptimisticVerificationException.class);
    constructStringObject(javax.jdo.JDOOptimisticVerificationException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOOptimisticVerificationException.class);
    constructStringThrowableObject(javax.jdo.JDOOptimisticVerificationException.class);
  }

  protected void constructJDOUnsupportedOptionException() {
    constructEmpty(javax.jdo.JDOUnsupportedOptionException.class);
    constructString(javax.jdo.JDOUnsupportedOptionException.class);
    constructStringThrowableArray(javax.jdo.JDOUnsupportedOptionException.class);
    constructStringThrowable(javax.jdo.JDOUnsupportedOptionException.class);
  }

  protected void constructJDOUserCallbackException() {
    constructEmpty(javax.jdo.JDOUserCallbackException.class);
    constructString(javax.jdo.JDOUserCallbackException.class);
    constructStringThrowableArray(javax.jdo.JDOUserCallbackException.class);
    constructStringThrowable(javax.jdo.JDOUserCallbackException.class);
    constructStringObject(javax.jdo.JDOUserCallbackException.class);
    constructStringThrowableArrayObject(javax.jdo.JDOUserCallbackException.class);
    constructStringThrowableObject(javax.jdo.JDOUserCallbackException.class);
  }

  protected void constructEmpty(Class<?> clazz) {
    construct(clazz, classArrayEmpty, objectArrayEmpty, null, null, null);
  }

  protected void constructString(Class<?> clazz) {
    construct(clazz, classArrayString, objectArrayString, message, null, null);
  }

  protected void constructStringObject(Class<?> clazz) {
    construct(clazz, classArrayStringObject, objectArrayStringObject, message, object, null);
  }

  protected void constructStringThrowableArray(Class<?> clazz) {
    construct(
        clazz,
        classArrayStringThrowableArray,
        objectArrayStringThrowableArray,
        message,
        null,
        throwable);
  }

  protected void constructStringThrowable(Class<?> clazz) {
    construct(
        clazz, classArrayStringThrowable, objectArrayStringThrowable, message, null, throwable);
  }

  protected void constructStringThrowableArrayObject(Class<?> clazz) {
    construct(
        clazz,
        classArrayStringThrowableArrayObject,
        objectArrayStringThrowableArrayObject,
        message,
        object,
        throwable);
  }

  protected void constructStringThrowableObject(Class<?> clazz) {
    construct(
        clazz,
        classArrayStringThrowableObject,
        objectArrayStringThrowableObject,
        message,
        object,
        throwable);
  }

  protected void construct(
      Class<?> clazz,
      Class<?>[] formal,
      Object[] params,
      String expectedMessage,
      Object expectedObject,
      Throwable expectedCause) {
    Constructor<?> ctor = null;
    try {
      ctor = clazz.getConstructor(formal);
    } catch (Throwable ex) {
      appendMessage("Throwable caught during getConstructor. " + ex);
      return;
    }
    try {
      throw (Throwable) ctor.newInstance(params);
    } catch (javax.jdo.JDOException ex) {
      // good catch
      if (clazz.isInstance(ex)) {
        Object actualObject = ex.getFailedObject();
        if (expectedObject != null && !expectedObject.equals(actualObject)) {
          appendMessage(
              "Wrong failed object for exception of "
                  + clazz
                  + "; Expected: "
                  + expectedObject
                  + " Actual: "
                  + actualObject);
        }
        String actualMessage = ex.getMessage();
        if (expectedMessage != null && !expectedMessage.equals(actualMessage)) {
          appendMessage(
              "Wrong message for exception of "
                  + clazz
                  + "; Expected: "
                  + expectedMessage
                  + " Actual: "
                  + actualMessage);
        }
        Throwable actualCause = ex.getCause();
        if (expectedCause != null && !expectedCause.equals(actualCause)) {
          appendMessage(
              "Wrong cause for exception of "
                  + clazz
                  + "; Expected: "
                  + expectedCause
                  + " Actual: "
                  + actualCause);
        }
        return;
      } else {
        appendMessage(
            "Wrong Throwable type caught for "
                + clazz
                + "; Expected:"
                + clazz
                + " Actual: "
                + ex.getClass());
      }
    } catch (Throwable t) {
      appendMessage(
          "Wrong Throwable type caught for "
              + clazz
              + "; Expected:"
              + clazz
              + " Actual: "
              + t.getClass());
    }
  }
}
