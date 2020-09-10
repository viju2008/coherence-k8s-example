package com.oracle.coherence.examples.rest;

import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

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
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/student/{id}")
    public Student getStudent(@PathVariable String id) {
        Student student = service.getStudent(new StudentId(id));
        if (student == null) {
            throw new StudentNotFoundException(id);
        }
        return student;
    }

    @PostMapping("/student")
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student) {
        return service.create(student);
    }

    @PutMapping("/student/{id}")
    public Student updateStudent(@PathVariable String id, @RequestBody Student student) {
        return service.update(new StudentId(id), student);
    }

    @DeleteMapping("/student/{id}")
    public Void deleteStudent(@PathVariable String id) {
        if (service.delete(new StudentId(id))) {
            return null;
        }
        throw new StudentNotFoundException(id);
    }
}
