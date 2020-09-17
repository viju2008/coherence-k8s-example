package com.oracle.coherence.examples.rest;

import com.oracle.coherence.examples.domain.EmployeeId;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Jonathan Knight  2020.09.10
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException
    extends RuntimeException {

    public EmployeeNotFoundException(EmployeeId id) {
        this(id.getEmpId());
    }

    public EmployeeNotFoundException(String id) {
        super("Employee " + id + " does not exist");
    }
}
