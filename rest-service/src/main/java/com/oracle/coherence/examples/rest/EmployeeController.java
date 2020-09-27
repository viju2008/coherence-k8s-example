package com.oracle.coherence.examples.rest;

import com.oracle.coherence.examples.domain.Employee;
import com.oracle.coherence.examples.domain.EmployeeId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jonathan Knight  2020.09.10
 */
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/employee/{id}")
    public Employee getemployee(@PathVariable String id) {
        Employee employee = service.getEmployee(new EmployeeId(id));
        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }
        return employee;
    }
    
    /*
    @GetMapping("/employee/withFilter")
    public Employee getemployeeFilter(@PathVariable String m) {
        Employee employee = service.getEmployee(new EmployeeId(id));
        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }
        return employee;
    }*/

    @PostMapping("/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return service.create(employee);
    }

    @PutMapping("/employee/{id}")
    public Employee updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        return service.update(new EmployeeId(id), employee);
    }

    @DeleteMapping("/employee/{id}")
    public Void deleteemployee(@PathVariable String id) {
        if (service.delete(new EmployeeId(id))) {
            return null;
        }
        throw new EmployeeNotFoundException(id);
    }
}
