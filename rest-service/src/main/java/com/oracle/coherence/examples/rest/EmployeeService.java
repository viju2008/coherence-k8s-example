package com.oracle.coherence.examples.rest;

import java.util.UUID;

import com.oracle.coherence.examples.domain.Address;
import com.oracle.coherence.examples.domain.Employee;
import com.oracle.coherence.examples.domain.EmployeeId;

import com.tangosol.net.NamedCache;
import com.tangosol.net.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jonathan Knight  2020.09.10
 */
@Service
public class EmployeeService {

    public static final String CACHE_NAME = "employees";

    @Autowired
    private Session session;

    /**
     * Get the specified student.
     *
     * @param id the ID of the student to get
     * @return the specified student or {@code null} if the student
     *         does not exist
     */
    public Employee getEmployee(EmployeeId id) {
        return getCache().get(id);
    }

    /**
     * Create a new student.
     *
     * @param student the student details
     * @return the details of the new student
     */
    public Employee create(Employee employee) {
        String empId = UUID.randomUUID().toString();
        employee.setEmpId(empId);
        getCache().put(new EmployeeId(empId), employee);
        return employee;
    }

    /**
     * Update an student.
     *
     * @param id       the ID of the student to update
     * @param student  the student details to update
     *
     * @return the updated student
     *
     * @throws StudentNotFoundException if the student fdoes not exist
     */
    public Employee update(EmployeeId id, Employee employee) {
        // ensure that the Student roll number matches the Id.
        employee.setEmpId(id.getEmpId());

        Employee cached = getCache().computeIfPresent(id, (k, v) -> employee);
        if (cached == null) {
            throw new EmployeeNotFoundException(id);
        }
        return cached;
    }

    /**
     * Delete the specified student.
     *
     * @param id the ID of the student to delete
     * @return {@code true} if the student was deleted or {@code false}
     *         if the student did not exist
     */
    public boolean delete(EmployeeId id) {
        Employee removed = getCache().remove(id);
        return removed != null;
    }
    
   /* public void getWithFilter()
    {
    getCache().
    }*/

    private NamedCache<EmployeeId, Employee> getCache() {
        return session.getCache(CACHE_NAME);
    }
}
