package com.oracle.coherence.examples.storage;

import com.oracle.coherence.examples.domain.Employee;
import com.oracle.coherence.examples.domain.EmployeeId;

import com.tangosol.net.cache.CacheStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jonathan Knight  2020.09.09
 */
@Component
public class EmployeeCacheStore
        implements CacheStore<EmployeeId, Employee> {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public Employee load(EmployeeId EmployeeId) {
        return repository.findById(EmployeeId.getEmpId())
                .orElse(null);
    }

    @Override
    public void store(EmployeeId EmployeeId, Employee Employee) {
        Employee.setEmpId(EmployeeId.getEmpId());
        repository.save(Employee);
    }

    @Override
    public void erase(EmployeeId EmployeeId) {
        repository.deleteById(EmployeeId.getEmpId());
    }
}
