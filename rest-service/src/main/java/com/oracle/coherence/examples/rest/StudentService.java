package com.oracle.coherence.examples.rest;

import java.util.UUID;

import com.oracle.coherence.examples.domain.Address;
import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import com.tangosol.net.NamedCache;
import com.tangosol.net.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jonathan Knight  2020.09.10
 */
@Service
public class StudentService {

    public static final String CACHE_NAME = "students";

    @Autowired
    private Session session;

    /**
     * Get the specified student.
     *
     * @param id the ID of the student to get
     * @return the specified student or {@code null} if the student
     *         does not exist
     */
    public Student getStudent(StudentId id) {
        return getCache().get(id);
    }

    /**
     * Create a new student.
     *
     * @param student the student details
     * @return the details of the new student
     */
    public Student create(Student student) {
        String roll = UUID.randomUUID().toString();
        student.setRollNumber(roll);
        getCache().put(new StudentId(roll), student);
        return student;
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
    public Student update(StudentId id, Student student) {
        // ensure that the Student roll number matches the Id.
        student.setRollNumber(id.getRollNumber());

        Student cached = getCache().computeIfPresent(id, (k, v) -> student);
        if (cached == null) {
            throw new StudentNotFoundException(id);
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
    public boolean delete(StudentId id) {
        Student removed = getCache().remove(id);
        return removed != null;
    }

    private NamedCache<StudentId, Student> getCache() {
        return session.getCache(CACHE_NAME);
    }
}
