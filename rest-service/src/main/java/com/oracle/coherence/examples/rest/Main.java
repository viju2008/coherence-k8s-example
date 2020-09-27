package com.oracle.coherence.examples.rest;

import java.util.UUID;

import com.oracle.coherence.examples.domain.Address;
import com.oracle.coherence.examples.domain.Employee;
import com.oracle.coherence.examples.domain.EmployeeId;
import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * @author Jonathan Knight  2020.09.10
 */
public class Main {

    public static void main(String[] args) {
        Address address = new Address();
        address.setLineOne("Freeda Apartments");
        address.setLineTwo("Carter Road, Bandra West");
        address.setCity("Mumbai");
        address.setPostalCode("123456");
        address.setCountry("India");

        String roll = UUID.randomUUID().toString();

        StudentId id = new StudentId(roll);
        Student student = new Student(roll, "Aamir", "Khan", "drama", address);

        NamedCache<StudentId, Student> cache = CacheFactory.getCache("students");
        Student cached = cache.get(id);
        cache.put(id, student);
        System.out.println();
        
        String roll2 = UUID.randomUUID().toString();
        
        EmployeeId emp_id = new EmployeeId(roll2);
        Employee emp = new Employee(roll, "Aamir", "Khan", "drama", address);
        
        
        NamedCache<EmployeeId, Employee> cache_emp = CacheFactory.getCache("employees");
        Employee cached_emp = cache_emp.get(emp_id);
        cache_emp.put(emp_id, emp);
        System.out.println();
        
    }
}
