/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.jdo.util.web;

import java.io.InputStream;
import java.io.IOException;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.PersistenceManager;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * This implementation of the servlet Filter interface creates a JDO 
 * PersistenceManager, stores it as a request attribute and as a ThreadLocal. 
 * It closes the PersistenceManager after the filter chain has returned.
 * The idea for this class is taken from the JavaOne 2003 presentation 
 * "Using Struts with Java Data Objects" by Craig Russell, Craig McClanahan
 * and Amy Roh.
 * <p>
 * To setup the filter add the following to your deployment descriptor:
 * <pre>
 * &lt;filter&gt;
 *     &lt;filter-name&gt;JDOFilter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;org.apache.jdo.util.web.JDOFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;JDOFilter&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * The JDOFilter supports two filter initialization paramters:
 * <ul>
 * <li><code>pmfPropsResource</code>: the name of the PersistenceManagerFactory
 * properties resource. Default is <code>/WEB-INF/pmf.properties</code>.</li>
 * <li><code>pmRequestAttrName</code>: the name of the request attribute used to
 * store the PersistenceManager instance. Default is <code>jdoPM</code>.
 * </ul>
 * This is an sample filter definition using initialization parameter:
 * <pre>
 * &lt;filter&gt;
 *     &lt;filter-name&gt;JDOFilter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;org.apache.jdo.util.web.JDOFilter&lt;/filter-class&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;pmfPropsResource&lt;/param-name&gt;
 *         &lt;param-value&gt;/WEB-INF/pmf.properties&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;pmRequestAttrName&lt;/param-name&gt;
 *         &lt;param-value&gt;jdoPM&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * It is possible to define multiple filters in the deployment descriptor, all 
 * using the JDOFilter class. In this case it is important to specify the name 
 * of the PersistenceManager request attribute in the filter configuration by 
 * setting the pmRequestAttrName initialization paramter. Otherwise, the 
 * different filter instances would try to use the same request attribute. 
 * Please note, in case of multiple JDOFilter instances, only the first filter 
 * stores its PersistenceManager as a ThreadLocal. 
 * <p>
 * The static method {@link #getThreadLocalPM()} allows retrieving the 
 * PersistenceManager instance bound to the current thread.
 */
public class JDOFilter
    implements Filter  {

    /** The name of the JDOFilter initialization parameter allowing to specify 
     * the name of the pmf properties resource. */
    public static final String PMF_PROPS_RESOURCE_PARAM = "pmfPropsResource";

    /** The default PMF properties resource. */
    public static final String PMF_PROPS_RESOURCE_DEFAULT = 
        "/WEB-INF/pmf.properties";
    
    /** The name of the JDOFilter initialization parameter allowing to specify 
     * the name of the pm request attribute.  */
    public static final String PM_REQUEST_ATTR_NAME_PARAM = "pmRequestAttrName";
    
    /** The name of the request attribute storing the PersistenceManager. */
    public static final String PM_REQUEST_ATTR_NAME_DEFAULT = "jdoPM";

    /** The ThreadLocal storing the PersistenceManager. */
    private static ThreadLocal pmThreadLocal = new ThreadLocal();
    
    /** Logging support. */
    private static final Log logger = LogFactory.getLog(JDOFilter.class);
        
    /** The PMF instance created by the init method. */
    private PersistenceManagerFactory pmf;
    
    /** The name of the request attribute holding the PersistenceManager. */
    private String pmRequestAttrName;
        
    // ===== javax.servlet.Filter methods =====

    /** Called by the web container to indicate to a filter that it is being 
     * placed into service. 
     * <p>
     * This implementation creates a JDO PersistenceManagerFactory instance 
     * using a properties resource specified by an initialization parameter 
     * called {@link #PMF_PROPS_RESOURCE_PARAM} or defaulted to 
     * {@link #PMF_PROPS_RESOURCE_DEFAULT}. The method checks for another 
     * initialization parameter {@link #PM_REQUEST_ATTR_NAME_PARAM} that may be
     * used to specify the name of the request attribute holding the 
     * PersistenceManager instance. The name defaults to 
     * {@link #PM_REQUEST_ATTR_NAME_DEFAULT} if there is no such initialization
     * parameter.
     * @param filterConfig the filter configuration object. 
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        String pmfPropsResource = 
            filterConfig.getInitParameter(PMF_PROPS_RESOURCE_PARAM);
        boolean usingPMFPropsResourceDefault = false;
        if ((pmfPropsResource == null) || pmfPropsResource.length() == 0) {
            pmfPropsResource = PMF_PROPS_RESOURCE_DEFAULT;
            usingPMFPropsResourceDefault = true;
        }
        ServletContext context = filterConfig.getServletContext();
        InputStream stream = context.getResourceAsStream(pmfPropsResource);
        if (stream == null) {
            ServletException ex = new ServletException(
                "Error during initialization of JDOFilter: " + 
                "Cannot find JDO PMF properties resource " +
                (usingPMFPropsResourceDefault ? "defaulted to " : "") +
                pmfPropsResource);
            throw ex;
        }
        pmf = JDOHelper.getPersistenceManagerFactory(stream);
        if (logger.isDebugEnabled()) {
            logger.debug(
                "Created PMF " + pmf +
                "\nPMF properties: " +
                "\n\t driver         = " + pmf.getConnectionDriverName() + 
                "\n\t URL            = " + pmf.getConnectionURL() + 
                "\n\t userName       = " + pmf.getConnectionUserName() + 
                "\n\t mapping        = " + pmf.getMapping() +
                "\n\t optimistic     = " + pmf.getOptimistic() +
                "\n\t retainValues   = " + pmf.getRetainValues() +
                "\n\t restoreValues  = " + pmf.getRestoreValues() +
                "\n\t nonTxRead      = " + pmf.getNontransactionalRead() +
                "\n\t nonTxWrite     = " + pmf.getNontransactionalWrite() +
                "\n\t ignoreCache    = " + pmf.getIgnoreCache() +
                "\n\t detachOnCommit = " + pmf.getDetachAllOnCommit() +
                "\n\t properties     = " + pmf.getProperties());
        }

        // get the name of the request attribute holding the PersistenceManager
        pmRequestAttrName = 
            filterConfig.getInitParameter(PM_REQUEST_ATTR_NAME_PARAM);
        if ((pmRequestAttrName == null) || pmRequestAttrName.length() == 0) {
            pmRequestAttrName = PM_REQUEST_ATTR_NAME_DEFAULT;
        }
    }

    /** The doFilter method of the Filter is called by the container each time a
     * request/response pair is passed through the chain due to a client request
     * for a resource at the end of the chain. This implementation creates a 
     * PersistenceManager, stores it as a request attribute and in a ThreadLocal
     * and then calls the filter chain. It closes the PersistenceManager after 
     * the chain returns.
     * @param request the resquest
     * @param response the response 
     * @param chain the filter chain   
     */
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) 
        throws IOException, ServletException {
        PersistenceManager pm = null;
        try {
            pm = initializePM(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Created PM " + pm);
            }
            chain.doFilter(request, response);
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Close PM " + pm);
            }
            finalizePM(request, pm);
        }
    }

    /** Called by the web container to indicate to a filter that it is being 
     * taken out of service. This implementation closes the 
     * PersistenceManagerFactory. 
     */
    public void destroy() {
        if (pmf != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Close PMF " + pmf);
            }
            pmf.close();
            pmf = null;
        }
    }
    
    // ===== Public methods not defined in Filter =====
    
    /** Returns the PersistenceManager instance bound to the current thread 
     * using a ThreadLocal. 
     * @return the PersistenceManager bound to the current thread.
     */
    public static PersistenceManager getThreadLocalPM() {
         return (PersistenceManager)pmThreadLocal.get();
    }
    
    // ===== Internal helper methods =====

    /** Helper method to create and store a new PersistenceManager. The method 
     * stores the PersistenceManager as value of the request attribute denoted 
     * by {@link #pmRequestAttrName} and in a ThreadLocal. Use static method
     * {@link #getThreadLocalPM()} to retrieve the PersistenceManager instance 
     * bound to the current thread.
     * @param request the request to store in the PersistenceManager as 
     * attribute.
     * @return a new PersistenceManager
     */
    private PersistenceManager initializePM(ServletRequest request) 
        throws ServletException {
        PersistenceManager pm = pmf.getPersistenceManager();
        if (request.getAttribute(pmRequestAttrName) != null) {
            ServletException ex = new ServletException(
                "JDOFilter: error during PM intialization, request attribute " +
                pmRequestAttrName + " already defined as " + 
                 request.getAttribute(pmRequestAttrName));
            throw ex;
        }
        request.setAttribute(pmRequestAttrName, pm);
        if (pmThreadLocal.get() == null) {
            pmThreadLocal.set(pm);
        }
        return pm;
    }    

    /** Helper method to finalize a PersistenceManager. The method closes the 
     * specified PersistenceManager. If there is an active transaction it is 
     * rolled back before the PersistenceManager is closed. The method also 
     * removes the request attribute holding a PersistenceManager and nullifies
     * the ThreadLocal value, but only if it is the specified 
     * PersistenceManager.
     * @param request the request holding the PersistenceManager as attribute.
     * @param the PersistenceManager
     */
    private void finalizePM(ServletRequest request, PersistenceManager pm) {
        if (pm == null) {
            return;
        }
        if (!pm.isClosed()) {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
            pm.close();
        }
        // Remove request attribute if it is the specified pm
        if (request.getAttribute(pmRequestAttrName) == pm) {
            request.removeAttribute(pmRequestAttrName);
        }
        // Nullify the thread local if it is the specified pm
        if (pmThreadLocal.get() == pm) {
            pmThreadLocal.set(null);
        }
    }

}
