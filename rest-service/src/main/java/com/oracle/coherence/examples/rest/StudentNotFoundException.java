package com.oracle.coherence.examples.rest;

import com.oracle.coherence.examples.domain.StudentId;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Jonathan Knight  2020.09.10
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StudentNotFoundException
    extends RuntimeException {

    public StudentNotFoundException(StudentId id) {
        this(id.getRollNumber());
    }

    public StudentNotFoundException(String id) {
        super("Student " + id + " does not exist");
    }
}
