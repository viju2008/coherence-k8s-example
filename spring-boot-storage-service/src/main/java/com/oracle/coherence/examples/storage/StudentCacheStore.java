package com.oracle.coherence.examples.storage;

import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import com.tangosol.net.cache.CacheStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jonathan Knight  2020.09.09
 */
@Component
public class StudentCacheStore
        implements CacheStore<StudentId, Student> {

    @Autowired
    private StudentRepository repository;

    @Override
    public Student load(StudentId studentId) {
        return repository.findById(studentId.getRollNumber())
                .orElse(null);
    }

    @Override
    public void store(StudentId studentId, Student student) {
        student.setRollNumber(studentId.getRollNumber());
        repository.save(student);
    }

    @Override
    public void erase(StudentId studentId) {
        repository.deleteById(studentId.getRollNumber());
    }
}
