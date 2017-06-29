package org.apache.jdo.tck.query.api;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.EqualityHelper;

import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *<B>Title:</B> SampleQueries
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion IDs:</B> 
 *<BR>
 *<B>Assertion Description: </B>
 * This test class runs the example queries from the JDO specification.
 */
public class SampleQueries extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = "Assertion (SampleQueries) failed: ";

    /** */
    private static final String SAMPLE_QUERIES_TEST_COMPANY_TESTDATA =
            "org/apache/jdo/tck/pc/company/companyForSampleQueriesTest.xml";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SampleQueries.class);
    }

    /**
     * Basic query.
     *
     * This query selects all Employee instances from the candidate collection where
     * the salary is greater than the constant 30000.
     * Note that the float value for salary is unwrapped for the comparison with the
     * literal int value, which is promoted to float using numeric promotion.
     * If the value for the salary field in a candidate instance isnull, then it cannot
     * be unwrapped for the comparison, and the candidate instance is rejected.
     */
    public void testQuery01a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where salary > 30000";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000");
            List<FullTimeEmployee> emps = (List<FullTimeEmployee>)q.execute();
            List<FullTimeEmployee> expected =
                    getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp2", "emp5"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Basic query.
     *
     * This query selects all Employee instances from the candidate collection where
     * the salary is greater than the constant 30000.
     * Note that the float value for salary is unwrapped for the comparison with the
     * literal int value, which is promoted to float using numeric promotion.
     * If the value for the salary field in a candidate instance isnull, then it cannot
     * be unwrapped for the comparison, and the candidate instance is rejected.
     */
    public void testQuery01b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where salary > 30000";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000");
            List<FullTimeEmployee> emps = q.executeList();
            List<FullTimeEmployee> expected =
                    getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp2", "emp5"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Basic query with ordering.
     *
     * This query selects all Employee instances from the candidate collection where the salary
     * is greater than the constant 30000, and returns a Collection ordered based on employee salary.
     */
    public void testQuery02a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where salary > 30000 order by salary ascending";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000");
            q.setOrdering ("salary ascending");
            List<FullTimeEmployee> emps = (List<FullTimeEmployee>)q.execute();
            List<FullTimeEmployee> expected =
                    getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp5", "emp2"});
            checkQueryResultWithOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Basic query with ordering.
     *
     * This query selects all Employee instances from the candidate collection where the salary
     * is greater than the constant 30000, and returns a Collection ordered based on employee salary.
     */
    public void testQuery02b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where salary > 30000 order by salary ascending";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > 30000");
            q.setOrdering ("salary ascending");
            List<FullTimeEmployee> emps = q.executeList();
            List<FullTimeEmployee> expected =
                    getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp5", "emp2"});
            checkQueryResultWithOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Parameter passing.
     *
     * This query selects all Employee instances from the candidate collection where the salary
     * is greater than the value passed as a parameter and the name starts with the value passed
     * as a second parameter.
     * If the value for the salary field in a candidate instance is null, then it cannot be
     * unwrapped for the comparison, and the candidate instance is rejected.
     */
    public void testQuery03a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where salary > :sal && firstname.startsWith(:begin)";
            Query<FullTimeEmployee> q =
                    pm.newQuery(FullTimeEmployee.class,"salary > sal && firstname.startsWith(begin)");
            q.declareParameters("Double sal, String begin");
            List<FullTimeEmployee> emps = (List<FullTimeEmployee>)q.execute(30000., "M");
            List<FullTimeEmployee> expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Parameter passing.
     *
     * This query selects all Employee instances from the candidate collection where the salary
     * is greater than the value passed as a parameter and the name starts with the value passed
     * as a second parameter.
     * If the value for the salary field in a candidate instance is null, then it cannot be
     * unwrapped for the comparison, and the candidate instance is rejected.
     */
    public void testQuery03b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where salary > :sal && firstname.startsWith(:begin)";
            Query<FullTimeEmployee> q =
                    pm.newQuery(FullTimeEmployee.class,"salary > sal && firstname.startsWith(begin)");
            q.declareParameters("Double sal, String begin");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("sal", 30000.);
            paramValues.put("begin", "M");
            q.setNamedParameters(paramValues);
            List<FullTimeEmployee> emps = q.executeList();
            List<FullTimeEmployee> expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * Parameter passing.
     *
     * This query selects all Employee instances from the candidate collection where the salary
     * is greater than the value passed as a parameter and the name starts with the value passed
     * as a second parameter.
     * If the value for the salary field in a candidate instance is null, then it cannot be
     * unwrapped for the comparison, and the candidate instance is rejected.
     */
    public void testQuery03c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where salary > :sal && firstname.startsWith(:begin)";
            Query<FullTimeEmployee> q =
                    pm.newQuery(FullTimeEmployee.class,"salary > sal && firstname.startsWith(begin)");
            q.declareParameters("Double sal, String begin");
            q.setParameters(30000., "M");
            List<FullTimeEmployee> emps = q.executeList();
            List<FullTimeEmployee> expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Navigation through single-valued field.
     *
     * This query selects all Employee instances from the candidate collection where the value
     * of the name field in the Department instance associated with the Employee instance
     * is equal to the value passed as a parameter.
     * If the value for the dept field in a candidate instance is null, then it cannot be
     * navigated for the comparison, and the candidate instance is rejected.
     */
    public void testQuery04a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where department.name == :dep";
            Query<Employee> q = pm.newQuery(Employee.class, "department.name == dep");
            q.declareParameters("String dep");
            List<Employee> emps = (List<Employee>)q.execute ("R&D");
            List<Employee> expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp2", "emp3"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Navigation through single-valued field.
     *
     * This query selects all Employee instances from the candidate collection where the value
     * of the name field in the Department instance associated with the Employee instance
     * is equal to the value passed as a parameter.
     * If the value for the dept field in a candidate instance is null, then it cannot be
     * navigated for the comparison, and the candidate instance is rejected.
     */
    public void testQuery04b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where department.name == :dep";
            Query<Employee> q = pm.newQuery (Employee.class, "department.name == dep");
            q.declareParameters ("String dep");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("dep", "R&D");
            q.setNamedParameters(paramValues);
            List<Employee> emps = q.executeList();
            List<Employee> expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp2", "emp3"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Navigation through single-valued field.
     *
     * This query selects all Employee instances from the candidate collection where the value
     * of the name field in the Department instance associated with the Employee instance
     * is equal to the value passed as a parameter.
     * If the value for the dept field in a candidate instance is null, then it cannot be
     * navigated for the comparison, and the candidate instance is rejected.
     */
    public void testQuery04c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where department.name == :dep";
            Query<Employee> q = pm.newQuery (Employee.class, "department.name == dep");
            q.declareParameters ("String dep");
            q.setParameters("R&D");
            List<Employee> emps = q.executeList();
            List<Employee> expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp2", "emp3"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Navigation through multi-valued field.
     *
     * This query selects all Department instances from the candidate collection where
     * the collection of Employee instances contains at least one Employee instance
     * having a salary greater than the value passed as a parameter.
     */
    public void testQuery05a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where employees.contains(e) && e.weeklyhours > :hours";
            String filter = "employees.contains (emp) && emp.weeklyhours > hours";
            Query<Department> q = pm.newQuery(Department.class, filter);
            q.declareVariables("Employee emp");
            q.declareParameters("double hours");
            List<Department> deps = (List<Department>)q.execute (30.);
            List expected = getTransientCompanyModelInstancesAsList(new String[] {"dept1"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, deps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Navigation through multi-valued field.
     *
     * This query selects all Department instances from the candidate collection where
     * the collection of Employee instances contains at least one Employee instance
     * having a salary greater than the value passed as a parameter.
     */
    public void testQuery05b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where employees.contains(e) && e.weeklyhours > :hours";
            String filter = "employees.contains (emp) && emp.weeklyhours > hours";
            Query<Department> q = pm.newQuery(Department.class, filter);
            q.declareVariables("Employee emp");
            q.declareParameters("double hours");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("hours", 30.);
            q.setNamedParameters(paramValues);
            List<Department> deps = q.executeList();
            List<Department> expected = getTransientCompanyModelInstancesAsList(new String[] {"dept1"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, deps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Navigation through multi-valued field.
     *
     * This query selects all Department instances from the candidate collection where
     * the collection of Employee instances contains at least one Employee instance
     * having a salary greater than the value passed as a parameter.
     */
    public void testQuery05c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where employees.contains(e) && e.weeklyhours > :hours";
            String filter = "employees.contains (emp) && emp.weeklyhours > hours";
            Query<Department> q = pm.newQuery(Department.class, filter);
            q.declareVariables("Employee emp");
            q.declareParameters("double hours");
            q.setParameters(30.);
            List<Department> deps = q.executeList();
            List<Department> expected = getTransientCompanyModelInstancesAsList(new String[] {"dept1"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, deps, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Membership in a collection.
     *
     * This query selects all Department instances where the name field is contained in
     * a parameter collection, which in this example consists of three department names.
     */
    public void testQuery06a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where :depts.contains(name)";
            String filter = "depts.contains(name)";
            Query<Department> q = pm.newQuery(Department.class, filter);
            q.declareParameters("java.util.Collection depts");
            List<String> deptNames = Arrays.asList("R&D", "Sales", "Marketing");
            List<Department> result = (List<Department>)q.execute(deptNames);
            List<Department> expected =
                    getTransientCompanyModelInstancesAsList(new String[] {"dept1", "dept2", "dept3"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, result, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Membership in a collection.
     *
     * This query selects all Department instances where the name field is contained in
     * a parameter collection, which in this example consists of three department names.
     */
    public void testQuery06b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where :depts.contains(name)";
            String filter = "depts.contains(name)";
            Query<Department> q = pm.newQuery(Department.class, filter);
            q.declareParameters("java.util.Collection depts");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("depts", Arrays.asList("R&D", "Sales", "Marketing"));
            q.setNamedParameters(paramValues);
            List<Department> result = q.executeList();
            List<Department> expected =
                    getTransientCompanyModelInstancesAsList(new String[] {"dept1", "dept2", "dept3"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, result, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Membership in a collection.
     *
     * This query selects all Department instances where the name field is contained in
     * a parameter collection, which in this example consists of three department names.
     */
    public void testQuery06c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select where :depts.contains(name)";
            String filter = "depts.contains(name)";
            Query<Department> q = pm.newQuery(Department.class, filter);
            q.declareParameters("java.util.Collection depts");
            q.setParameters(Arrays.asList("R&D", "Sales", "Marketing"));
            List<Department> result = q.executeList();
            List<Department> expected =
                    getTransientCompanyModelInstancesAsList(new String[] {"dept1", "dept2", "dept3"});
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, result, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of a Single Field.
     *
     * This query selects names of all Employees who work in the parameter department.
     */
    public void testQuery07a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select firstname where department.name == :deptName";
            Query<Employee> q = pm.newQuery(Employee.class, "department.name == deptName");
            q.setResult("firstname");
            q.declareParameters("String deptName");
            List<String> names = (List<String>)q.execute("R&D");
            List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of a Single Field.
     *
     * This query selects names of all Employees who work in the parameter department.
     */
    public void testQuery07b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select firstname where department.name == :deptName";
            Query<Employee> q = pm.newQuery(Employee.class, "department.name == deptName");
            q.setResult("firstname");
            q.declareParameters("String deptName");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("deptName", "R&D");
            q.setNamedParameters(paramValues);
            List<String> names = q.executeResultList(String.class);
            List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of a Single Field.
     *
     * This query selects names of all Employees who work in the parameter department.
     */
    public void testQuery07c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select firstname where department.name == :deptName";
            Query<Employee> q = pm.newQuery(Employee.class, "department.name == deptName");
            q.setResult("firstname");
            q.declareParameters("String deptName");
            q.setParameters("R&D");
            List<String> names = q.executeResultList(String.class);
            List<String> expected = Arrays.asList("Joe", "Craig", "Michael");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of Multiple Fields and Expressions.
     *
     * This query selects names, salaries, and bosses of Employees who work in the parameter department.
     */
    public void testQuery08a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select firstname, salary, manager as reportsTo " +
                    "into org.apache.jdo.tck.query.api.SampleQueries$Info where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("firstname, salary, manager as reportsTo");
            q.setResultClass(Info.class);
            q.declareParameters("String deptName");
            List<Info> infos = (List<Info>) q.execute("R&D");
            
            Info info1 = new Info();
            info1.firstname = "Michael";
            info1.salary = 40000.;
            info1.reportsTo = (Employee)getTransientCompanyModelInstance("emp2");
            Info info2 = new Info();
            info2.firstname = "Craig";
            info2.salary = 50000.;
            info2.reportsTo = null;
            List expected = Arrays.asList(info1, info2);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of Multiple Fields and Expressions.
     *
     * This query selects names, salaries, and bosses of Employees who work in the parameter department.
     */
    public void testQuery08b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select firstname, salary, manager as reportsTo " +
                            "into org.apache.jdo.tck.query.api.SampleQueries$Info where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("firstname, salary, manager as reportsTo");
            q.setResultClass(Info.class);
            q.declareParameters("String deptName");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("deptName", "R&D");
            q.setNamedParameters(paramValues);
            List<Info> infos = q.executeResultList(Info.class);

            Info info1 = new Info();
            info1.firstname = "Michael";
            info1.salary = 40000.;
            info1.reportsTo = (Employee)getTransientCompanyModelInstance("emp2");
            Info info2 = new Info();
            info2.firstname = "Craig";
            info2.salary = 50000.;
            info2.reportsTo = null;
            List expected = Arrays.asList(info1, info2);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of Multiple Fields and Expressions.
     *
     * This query selects names, salaries, and bosses of Employees who work in the parameter department.
     */
    public void testQuery08c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select firstname, salary, manager as reportsTo " +
                            "into org.apache.jdo.tck.query.api.SampleQueries$Info where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("firstname, salary, manager as reportsTo");
            q.setResultClass(Info.class);
            q.declareParameters("String deptName");
            q.setParameters("R&D");
            List<Info> infos = q.executeResultList(Info.class);

            Info info1 = new Info();
            info1.firstname = "Michael";
            info1.salary = 40000.;
            info1.reportsTo = (Employee)getTransientCompanyModelInstance("emp2");
            Info info2 = new Info();
            info2.firstname = "Craig";
            info2.salary = 50000.;
            info2.reportsTo = null;
            List expected = Arrays.asList(info1, info2);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * Projection of Multiple Fields and Expressions into a Constructed instance.
     *
     * This query selects names, salaries, and bosses of Employees who work in the parameter department,
     * and uses the constructor for the result class.
     */
    public void testQuery09a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select new org.apache.jdo.tck.query.api.SampleQueries$Info (firstname, salary, manager) " +
                    "where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("new org.apache.jdo.tck.query.api.SampleQueries$Info(firstname, salary, manager)");
            q.declareParameters("String deptName");
            List<Info> infos = (List<Info>)q.execute("R&D");

            List<Info> expected = Arrays.asList(
                    new Info("Michael", 40000., (Employee)getTransientCompanyModelInstance("emp2")),
                    new Info("Craig", 50000., null)
            );
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of Multiple Fields and Expressions into a Constructed instance.
     *
     * This query selects names, salaries, and bosses of Employees who work in the parameter department,
     * and uses the constructor for the result class.
     */
    public void testQuery09b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select new org.apache.jdo.tck.query.api.SampleQueries$Info (firstname, salary, manager) " +
                            "where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("new org.apache.jdo.tck.query.api.SampleQueries$Info(firstname, salary, manager)");
            q.declareParameters("String deptName");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("deptName", "R&D");
            q.setNamedParameters(paramValues);
            List<Info> infos = q.executeResultList(Info.class);

            List<Info> expected = Arrays.asList(
                    new Info("Michael", 40000., (Employee)getTransientCompanyModelInstance("emp2")),
                    new Info("Craig", 50000., null)
            );
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of Multiple Fields and Expressions into a Constructed instance.
     *
     * This query selects names, salaries, and bosses of Employees who work in the parameter department,
     * and uses the constructor for the result class.
     */
    public void testQuery09c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select new org.apache.jdo.tck.query.api.SampleQueries$Info (firstname, salary, manager) " +
                            "where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("new org.apache.jdo.tck.query.api.SampleQueries$Info(firstname, salary, manager)");
            q.declareParameters("String deptName");
            q.setParameters("R&D");
            List<Info> infos = q.executeResultList(Info.class);

            List<Info> expected = Arrays.asList(
                    new Info("Michael", 40000., (Employee)getTransientCompanyModelInstance("emp2")),
                    new Info("Craig", 50000., null)
            );
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of a single Field.
     *
     * This query averages the salaries of Employees who work in the parameter department
     * and returns a single value.
     */
    public void testQuery10a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select avg(salary) where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("avg(salary)");
            q.declareParameters("String deptName");
            Double avgSalary = (Double) q.execute("R&D");

            Double expected = 45000.;
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, avgSalary, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of a single Field.
     *
     * This query averages the salaries of Employees who work in the parameter department
     * and returns a single value.
     */
    public void testQuery10b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select avg(salary) where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("avg(salary)");
            q.declareParameters("String deptName");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("deptName", "R&D");
            q.setNamedParameters(paramValues);
            Double avgSalary = q.executeResultUnique(Double.class);

            Double expected = 45000.;
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, avgSalary, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of a single Field.
     *
     * This query averages the salaries of Employees who work in the parameter department
     * and returns a single value.
     */
    public void testQuery10c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select avg(salary) where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("avg(salary)");
            q.declareParameters("String deptName");
            q.setParameters("R&D");
            Double avgSalary = q.executeResultUnique(Double.class);

            Double expected = 45000.;
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, avgSalary, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of Multiple Fields and Expressions.
     *
     * This query averages and sums the salaries of Employees who work in the parameter department.
     */
    public void testQuery11a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select avg(salary), sum(salary) where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("avg(salary), sum(salary)");
            q.declareParameters("String deptName");
            Object[] avgSum = (Object[]) q.execute("R&D");

            Double[] expected = new Double[] {45000., 90000.};
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, avgSum, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of Multiple Fields and Expressions.
     *
     * This query averages and sums the salaries of Employees who work in the parameter department.
     */
    public void testQuery11b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select avg(salary), sum(salary) where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("avg(salary), sum(salary)");
            q.declareParameters("String deptName");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("deptName", "R&D");
            q.setNamedParameters(paramValues);
            Object[] avgSum = (Object[])q.executeResultUnique();

            Double[] expected = new Double[] {45000., 90000.};
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, avgSum, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of Multiple Fields and Expressions.
     *
     * This query averages and sums the salaries of Employees who work in the parameter department.
     */
    public void testQuery11c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select avg(salary), sum(salary) where department.name == :deptName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "department.name == deptName");
            q.setResult("avg(salary), sum(salary)");
            q.declareParameters("String deptName");
            q.setParameters("R&D");
            Object[] avgSum = (Object[])q.executeResultUnique();

            Double[] expected = new Double[] {45000., 90000.};
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, avgSum, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of Multiple fields with Grouping.
     *
     * This query averages and sums the salaries of Employees who work in all departments having
     * more than one employee and aggregates by department name.
     */
    public void testQuery12a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select avg(salary), sum(salary), department.name " +
                    "from org.apache.jdo.tck.pc.company.FullTimeEmployee " +
                    "where department.name == :deptName " +
                    "group by department.name having count(department.name)";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class);
            q.setResult("avg(salary), sum(salary), department.name");
            q.setGrouping("department.name having count(department.name) > 1");
            List<Object[]> results = (List<Object[]>)q.execute();
            if (results.size() != 1) {
                fail(ASSERTION_FAILED,
                        "Query result has size " + results.size() + ", expected query result of size 1");
            }
            Object[] row = results.get(0);
            Object[] expectedRow = new Object[]{45000., 90000., "R&D"};
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, row, expectedRow);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Aggregation of Multiple fields with Grouping.
     *
     * This query averages and sums the salaries of Employees who work in all departments having
     * more than one employee and aggregates by department name.
     */
    public void testQuery12b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select avg(salary), sum(salary), department.name " +
                            "from org.apache.jdo.tck.pc.company.FullTimeEmployee " +
                            "where department.name == :deptName " +
                            "group by department.name having count(department.name)";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class);
            q.setResult("avg(salary), sum(salary), department.name");
            q.setGrouping("department.name having count(department.name) > 1");
            List<Object[]> results = q.executeResultList(Object[].class);
            if (results.size() != 1) {
                fail(ASSERTION_FAILED,
                        "Query result has size " + results.size() + ", expected query result of size 1");
            }
            Object[] row = results.get(0);
            Object[] expectedRow = new Object[]{45000., 90000., "R&D"};
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, row, expectedRow);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * Selection of a Single Instance.
     *
     * This query returns a single instance of Employee.
     */
    public void testQuery13a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select unique this where firstname == :empName";
            Query<Employee> q = pm.newQuery(Employee.class, "firstname == empName");
            q.setUnique(true);
            q.declareParameters ("String empName");
            Employee emp = (Employee)q.execute("Michael");
            Employee expectedEmp = (Employee)getTransientCompanyModelInstance("emp1");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emp, expectedEmp);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Selection of a Single Instance.
     *
     * This query returns a single instance of Employee.
     */
    public void testQuery13b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select unique this where firstname == :empName";
            Query<Employee> q = pm.newQuery (Employee.class, "firstname == empName");
            q.setUnique(true);
            q.declareParameters ("String empName");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("empName", "Michael");
            q.setNamedParameters(paramValues);
            Employee emp = q.executeUnique();
            Employee expectedEmp = (Employee)getTransientCompanyModelInstance("emp1");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emp, expectedEmp);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Selection of a Single Instance.
     *
     * This query returns a single instance of Employee.
     */
    public void testQuery13c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select unique this where firstname == :empName";
            Query<Employee> q = pm.newQuery (Employee.class, "firstname == empName");
            q.setUnique(true);
            q.declareParameters ("String empName");
            q.setParameters("Michael");
            Employee emp = q.executeUnique();
            Employee expectedEmp = (Employee)getTransientCompanyModelInstance("emp1");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, emp, expectedEmp);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Selection of a Single Field.
     * 
     * This query returns a single field of a single Employee.
     */
    public void testQuery14a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select unique new Double(salary) where firstname == :empName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "firstname == empName");
            q.setResult("salary");
            q.setResultClass(Double.class);
            q.setUnique(true);
            q.declareParameters("String empName");
            Double salary = (Double) q.execute ("Michael");
            Double expectedSalary = 40000.;
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, salary, expectedSalary);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Selection of a Single Field.
     *
     * This query returns a single field of a single Employee.
     */
    public void testQuery14b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select unique new Double(salary) where firstname == :empName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "firstname == empName");
            q.setResult("salary");
            q.setResultClass(Double.class);
            q.declareParameters("String empName");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("empName", "Michael");
            q.setNamedParameters(paramValues);
            Double salary = q.executeResultUnique(Double.class);
            Double expectedSalary = 40000.;
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, salary, expectedSalary);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Selection of a Single Field.
     *
     * This query returns a single field of a single Employee.
     */
    public void testQuery14c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select unique new Double(salary) where firstname == :empName";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "firstname == empName");
            q.setResult("salary");
            q.setResultClass(Double.class);
            q.declareParameters("String empName");
            q.setParameters("Michael");
            Double salary = q.executeResultUnique(Double.class);
            Double expectedSalary = 40000.;
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, salary, expectedSalary);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of "this" to User-defined Result Class with Matching Field.
     *
     * This query selects instances of Employee who make more than the parameter salary and
     * stores the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
     * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
     */
    public void testQuery15a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select into org.apache.jdo.tck.query.api.SampleQueries$EmpWrapper where salary > sal";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
            // ToDo: the following line should no be necessary
            // org.datanucleus.exceptions.NucleusUserException:
            // Query needs to return objects of type "org.apache.jdo.tck.query.api.SampleQueries$EmpWrapper"
            // but it was impossible to set the field "birthdate" type "java.util.Date". The field should
            // have either a public set/put method, or be public.
            //q.setResult("distinct this as FullTimeEmployee");
            q.setResultClass(EmpWrapper.class);

            q.declareParameters("Double sal");
            List<EmpWrapper> infos = (List<EmpWrapper>)q.execute(30000.);

            EmpWrapper wrapper1 = new EmpWrapper();
            wrapper1.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp1");
            EmpWrapper wrapper2 = new EmpWrapper();
            wrapper2.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp2");
            EmpWrapper wrapper3 = new EmpWrapper();
            wrapper3.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp5");
            List<EmpWrapper> expected = Arrays.asList(wrapper1, wrapper2, wrapper3);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of "this" to User-defined Result Class with Matching Field.
     *
     * This query selects instances of Employee who make more than the parameter salary and
     * stores the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
     * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
     */
    public void testQuery15b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select into org.apache.jdo.tck.query.api.SampleQueries$EmpWrapper where salary > sal";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
            // ToDo: the following line should no be necessary
            // org.datanucleus.exceptions.NucleusUserException:
            // Query needs to return objects of type "org.apache.jdo.tck.query.api.SampleQueries$EmpWrapper"
            // but it was impossible to set the field "birthdate" type "java.util.Date". The field should
            // have either a public set/put method, or be public.
            //q.setResult("distinct this as FullTimeEmployee");
            q.setResultClass(EmpWrapper.class);

            q.declareParameters ("Double sal");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("sal", 30000.);
            q.setNamedParameters(paramValues);
            List<EmpWrapper> infos = q.executeResultList(EmpWrapper.class);

            EmpWrapper wrapper1 = new EmpWrapper();
            wrapper1.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp1");
            EmpWrapper wrapper2 = new EmpWrapper();
            wrapper2.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp2");
            EmpWrapper wrapper3 = new EmpWrapper();
            wrapper3.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp5");
            List<EmpWrapper> expected = Arrays.asList(wrapper1, wrapper2, wrapper3);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of "this" to User-defined Result Class with Matching Field.
     *
     * This query selects instances of Employee who make more than the parameter salary and
     * stores the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
     * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
     */
    public void testQuery15c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select into org.apache.jdo.tck.query.api.SampleQueries$EmpWrapper where salary > sal";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
            // ToDo: the following line should no be necessary
            // org.datanucleus.exceptions.NucleusUserException:
            // Query needs to return objects of type "org.apache.jdo.tck.query.api.SampleQueries$EmpWrapper"
            // but it was impossible to set the field "birthdate" type "java.util.Date". The field should
            // have either a public set/put method, or be public.
            //q.setResult("distinct this as FullTimeEmployee");
            q.setResultClass(EmpWrapper.class);

            q.declareParameters ("Double sal");
            q.setParameters(30000.);
            List<EmpWrapper> infos = q.executeResultList(EmpWrapper.class);

            EmpWrapper wrapper1 = new EmpWrapper();
            wrapper1.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp1");
            EmpWrapper wrapper2 = new EmpWrapper();
            wrapper2.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp2");
            EmpWrapper wrapper3 = new EmpWrapper();
            wrapper3.FullTimeEmployee = (FullTimeEmployee)getTransientCompanyModelInstance("emp5");
            List<EmpWrapper> expected = Arrays.asList(wrapper1, wrapper2, wrapper3);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of "this" to User-defined Result Class with Matching Method
     *
     * This query selects instances of FullTimeEmployee who make more than the parameter salary and
     * stores the result in a user-defined class.
     */
    public void testQuery16a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select into org.apache.jdo.tck.query.api.SampleQueries$EmpInfo where salary > sal";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
            // ToDo: the following line should no be necessary
            // org.datanucleus.exceptions.NucleusUserException:
            // Query needs to return objects of type "org.apache.jdo.tck.query.api.SampleQueries$EmpInfo"
            // but it was impossible to set the field "birthdate" type "java.util.Date". The field should
            // have either a public set/put method, or be public.
            //q.setResult("distinct this as FullTimeEmployee");
            q.setResultClass(EmpInfo.class);

            q.declareParameters("Double sal");
            List<EmpInfo> infos = (List<EmpInfo>)q.execute(30000.);

            EmpInfo info1 = new EmpInfo();
            info1.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp1"));
            EmpInfo info2 = new EmpInfo();
            info2.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp2"));
            EmpInfo info3 = new EmpInfo();
            info3.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp5"));
            List<EmpInfo> expected = Arrays.asList(info1, info2, info3);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of "this" to User-defined Result Class with Matching Method
     *
     * This query selects instances of FullTimeEmployee who make more than the parameter salary and
     * stores the result in a user-defined class.
     */
    public void testQuery16b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select into org.apache.jdo.tck.query.api.SampleQueries$EmpInfo where salary > sal";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
            // ToDo: the following line should no be necessary
            // org.datanucleus.exceptions.NucleusUserException:
            // Query needs to return objects of type "org.apache.jdo.tck.query.api.SampleQueries$EmpInfo"
            // but it was impossible to set the field "birthdate" type "java.util.Date". The field should
            // have either a public set/put method, or be public.
            //q.setResult("distinct this as FullTimeEmployee");
            q.setResultClass(EmpInfo.class);

            q.declareParameters("Double sal");
            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("sal", 30000.);
            q.setNamedParameters(paramValues);
            List<EmpInfo> infos = q.executeResultList(EmpInfo.class);

            EmpInfo info1 = new EmpInfo();
            info1.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp1"));
            EmpInfo info2 = new EmpInfo();
            info2.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp2"));
            EmpInfo info3 = new EmpInfo();
            info3.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp5"));
            List<EmpInfo> expected = Arrays.asList(info1, info2, info3);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of "this" to User-defined Result Class with Matching Method
     *
     * This query selects instances of FullTimeEmployee who make more than the parameter salary and
     * stores the result in a user-defined class.
     */
    public void testQuery16c() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select into org.apache.jdo.tck.query.api.SampleQueries$EmpInfo where salary > sal";
            Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
            // ToDo: the following line should no be necessary
            // org.datanucleus.exceptions.NucleusUserException:
            // Query needs to return objects of type "org.apache.jdo.tck.query.api.SampleQueries$EmpInfo"
            // but it was impossible to set the field "birthdate" type "java.util.Date". The field should
            // have either a public set/put method, or be public.
            //q.setResult("distinct this as FullTimeEmployee");
            q.setResultClass(EmpInfo.class);

            q.declareParameters("Double sal");
            q.setParameters(30000.);
            List<EmpInfo> infos = q.executeResultList(EmpInfo.class);

            EmpInfo info1 = new EmpInfo();
            info1.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp1"));
            EmpInfo info2 = new EmpInfo();
            info2.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp2"));
            EmpInfo info3 = new EmpInfo();
            info3.setFullTimeEmployee((FullTimeEmployee)getTransientCompanyModelInstance("emp5"));
            List<EmpInfo> expected = Arrays.asList(info1, info2, info3);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, infos, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of variables.
     *
     * This query returns the names of all Employees of all "Research" departments.
     */
    public void testQuery17a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select e.firstname where name.startsWith('R&D') " +
                    "&& employees.contains((org.apache.jdo.tck.pc.company.Employee) e)";
            Query<Department> q = pm.newQuery(Department.class);
            q.declareVariables("org.apache.jdo.tck.pc.company.Employee e");
            q.setFilter("name.startsWith('R&D') && employees.contains(e)");
            q.setResult("e.firstname");
            List<String> names = (List<String>) q.execute();
            List<String> expected = Arrays.asList("Michael", "Craig", "Joe");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Projection of variables.
     *
     * This query returns the names of all Employees of all "Research" departments.
     */
    public void testQuery17b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery = "select e.firstname where name.startsWith('R&D') " +
                    "&& employees.contains((org.apache.jdo.tck.pc.company.Employee) e)";
            Query<Department> q = pm.newQuery(Department.class);
            q.declareVariables("org.apache.jdo.tck.pc.company.Employee e");
            q.setFilter("name.startsWith('R&D') && employees.contains(e)");
            q.setResult("e.firstname");
            List<String> names = q.executeResultList(String.class);
            List<String> expected = Arrays.asList("Michael", "Craig", "Joe");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * Non-correlated subquery
     * 
     * This query returns names of employees who work more than the average of all employees.
     */
    public void testQuery18a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select firstname from org.apache.jdo.tck.pc.company.Employee " +
                    "where this.weeklyhours > " +
                    " (select avg(e.weeklyhours) from org.apache.jdo.tck.pc.company.Employee e)";
            Query<Employee> subq = pm.newQuery(Employee.class);
            subq.setResult("avg(weeklyhours)");
            Query<Employee> q = pm.newQuery(Employee.class);
            q.setFilter("this.weeklyhours > average_hours");
            q.setResult("this.firstname");
            q.addSubquery(subq, "double average_hours", null);
            List<String> names = (List<String>)q.execute();
            List<String> expected = Arrays.asList("Michael", "Craig");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Non-correlated subquery
     *
     * This query returns names of employees who work more than the average of all employees.
     */
    public void testQuery18b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select firstname from org.apache.jdo.tck.pc.company.Employee " +
                            "where this.weeklyhours > " +
                            " (select avg(e.weeklyhours) from org.apache.jdo.tck.pc.company.Employee e)";
            Query<Employee> subq = pm.newQuery(Employee.class);
            subq.setResult("avg(weeklyhours)");
            Query<Employee> q = pm.newQuery(Employee.class);
            q.setFilter("this.weeklyhours > average_hours");
            q.setResult("this.firstname");
            q.addSubquery(subq, "double average_hours", null);
            List<String> names = q.executeResultList(String.class);
            List<String> expected = Arrays.asList("Michael", "Craig");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    /**
     * Correlated subquery.
     * 
     * This query returns names of employees who work more than the average of employees
     * in the same department having the same manager. The candidate collection of the
     * subquery is the collection of employees in the department of the candidate employee
     * and the parameter passed to the subquery is the manager of the candidate employee.
     */
    public void testQuery19a() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select firstname from org.apache.jdo.tck.pc.company.Employee " +
                    "where this.weeklyhours > " +
                    "(select AVG(e.weeklyhours) from this.department.employees e " +
                    " where e.manager == this.manager)";
            Query<Employee> subq = pm.newQuery(Employee.class);
            subq.setFilter("this.manager == :manager");
            subq.setResult("avg(weeklyhours)");
            Query<Employee> q = pm.newQuery(Employee.class);
            q.setFilter("this.weeklyhours > average_hours");
            q.setResult("firstname");
            q.addSubquery(subq, "double average_hours","this.department.employees",
                    "this.manager");
            List<String> names = (List<String>)q.execute();
            List<String> expected = Arrays.asList("Michael");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Correlated subquery.
     *
     * This query returns names of employees who work more than the average of employees
     * in the same department having the same manager. The candidate collection of the
     * subquery is the collection of employees in the department of the candidate employee
     * and the parameter passed to the subquery is the manager of the candidate employee.
     */
    public void testQuery19b() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            String singleStringQuery =
                    "select firstname from org.apache.jdo.tck.pc.company.Employee " +
                            "where this.weeklyhours > " +
                            "(select AVG(e.weeklyhours) from this.department.employees e " +
                            " where e.manager == this.manager)";
            Query<Employee> subq = pm.newQuery(Employee.class);
            subq.setFilter("this.manager == :manager");
            subq.setResult("avg(weeklyhours)");
            Query<Employee> q = pm.newQuery(Employee.class);
            q.setFilter("this.weeklyhours > average_hours");
            q.setResult("firstname");
            q.addSubquery(subq, "double average_hours","this.department.employees",
                    "this.manager");
            List<String> names = q.executeResultList(String.class);
            List<String> expected = Arrays.asList("Michael");
            checkQueryResultWithoutOrder(ASSERTION_FAILED, singleStringQuery, names, expected);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Deleting Multiple Instances.
     * 
     * This query deletes all Employees who make more than the parameter salary.
     */
    public void testQuery20() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Query<FullTimeEmployee> empQuery = pm.newQuery (FullTimeEmployee.class, "personid == 5");
            empQuery.setUnique(true);
            FullTimeEmployee emp5 = empQuery.executeUnique();
            Object emp5Oid = pm.getObjectId(emp5);
            Query q = pm.newQuery (FullTimeEmployee.class, "salary > sal");
            q.declareParameters ("Double sal");
            q.deletePersistentAll(30000.);
            tx.commit();

            tx.begin();
            Query allQuery = pm.newQuery(FullTimeEmployee.class);
            List<FullTimeEmployee> allFTE = allQuery.executeList();
            if (!allFTE.isEmpty()) {
                fail(ASSERTION_FAILED, "All FullTimeEmployee instances should have been deleted," +
                " there are still " + allFTE.size() + " instances left.");
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public static class Info {
        public String firstname;
        public Double salary;
        public Person reportsTo;
        public Info () {}
        public Info (String firstname, Double salary, Person reportsTo) {
            this.firstname = firstname;
            this.salary = salary;
            this.reportsTo = reportsTo;
        }
        public boolean equals(Object obj) {
            if (!(obj instanceof Info)) {
                return false;
            }
            Info other = (Info)obj;
            if (!EqualityHelper.equals(firstname, other.firstname)) {
                return false;
            }
            if (!EqualityHelper.equals(salary, other.salary)) {
                return false;
            }
            if (!EqualityHelper.equals(reportsTo, other.reportsTo)) {
                return false;
            }
            return true;
        }
        public int hashCode () {
            int hashCode = 0;
            hashCode += firstname == null ? 0 : firstname.hashCode();
            hashCode += salary == null ? 0 : salary.hashCode();
            hashCode += reportsTo == null ? 0 : reportsTo.hashCode();
            return hashCode;
        }
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Info(");
            builder.append("firstname:").append(firstname);
            builder.append(", salary:").append(salary);
            builder.append(", reportsTo:").append(reportsTo == null ? "null" : reportsTo.getFirstname());
            builder.append(")");
            return builder.toString();
        }
    }

    public static class EmpWrapper {
        public FullTimeEmployee FullTimeEmployee;
        public EmpWrapper () {}
        // Need constructor to prevent
        // java.lang.NullPointerException
        // at org.datanucleus.query.QueryUtils.createResultObjectUsingDefaultConstructorAndSetters(QueryUtils.java:293)
        public EmpWrapper (FullTimeEmployee FullTimeEmployee) {
            this.FullTimeEmployee = FullTimeEmployee;
        }
        public boolean equals(Object obj) {
            if (!(obj instanceof EmpWrapper)) {
                return false;
            }
            EmpWrapper other = (EmpWrapper)obj;
            if (!EqualityHelper.equals(FullTimeEmployee, other.FullTimeEmployee)) {
                return false;
            }
            return true;
        }
        public int hashCode () {
            int hashCode = 0;
            hashCode += FullTimeEmployee == null ? 0 : FullTimeEmployee.hashCode();
            return hashCode;
        }
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("EmpWrapper(");
            builder.append("FullTimeEmployee:").append(FullTimeEmployee == null ? "null" : FullTimeEmployee.getFirstname());
            builder.append(")");
            return builder.toString();
        }
    }

    public static class EmpInfo {
        private FullTimeEmployee worker;
        public EmpInfo () {}
        // Need constructor to prevent
        // java.lang.NullPointerException
        // at org.datanucleus.query.QueryUtils.createResultObjectUsingDefaultConstructorAndSetters(QueryUtils.java:293)
        public EmpInfo (FullTimeEmployee worker) {
            this.worker = worker;
        }
        public FullTimeEmployee getWorker() {return worker;}
        public void setFullTimeEmployee(FullTimeEmployee e) {
            worker = e;
        }
        public boolean equals(Object obj) {
            if (!(obj instanceof EmpInfo)) {
                return false;
            }
            EmpInfo other = (EmpInfo)obj;
            if (!EqualityHelper.equals(worker, other.worker)) {
                return false;
            }
            return true;
        }
        public int hashCode () {
            int hashCode = 0;
            hashCode += worker == null ? 0 : worker.hashCode();
            return hashCode;
        }
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("EmpInfo(");
            builder.append(", worker:").append(worker == null ? "null" : worker.getFirstname());
            builder.append(")");
            return builder.toString();
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }

    /**
     * Returns the name of the company test data resource.
     * @return name of the company test data resource.
     */
    protected String getCompanyTestDataResource() {
        return SAMPLE_QUERIES_TEST_COMPANY_TESTDATA;
    }
}
