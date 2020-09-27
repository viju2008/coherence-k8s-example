package com.oracle.coherence.examples.storage;

import com.oracle.coherence.examples.domain.Student;
import com.oracle.coherence.examples.domain.StudentId;

import com.tangosol.net.cache.CacheStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    
    @Override
    public Map<StudentId, Student> loadAll(Collection<? extends StudentId> colKeys) {
        Map<StudentId, Student> map = new HashMap<>();
        for (StudentId key : colKeys) {
            Student value = load(key);
            if (value != null) {
                map.put(key, value);
            }
        }
        return map;
    }

    @Override
    public void eraseAll(Collection<? extends StudentId> colKeys) {
        boolean fRemove = true;

        for (Iterator<? extends StudentId> iter = colKeys.iterator(); iter.hasNext(); ) {
            erase(iter.next());
            if (fRemove) {
                try {
                    iter.remove();
                }
                catch (UnsupportedOperationException e) {
                    fRemove = false;
                }
            }
        }
    }

    @Override
    public void storeAll(Map<? extends StudentId, ? extends Student> mapEntries) {
        boolean fRemove = true;

        for (Iterator<? extends Map.Entry<? extends StudentId, ? extends Student>> iter = mapEntries.entrySet().iterator();
                iter.hasNext(); ) {
            Map.Entry<? extends StudentId, ? extends Student> entry = iter.next();
            store(entry.getKey(), entry.getValue());
            if (fRemove) {
                try {
                    iter.remove();
                }
                catch (UnsupportedOperationException e) {
                    fRemove = false;
                }
            }
        }
    }
}
