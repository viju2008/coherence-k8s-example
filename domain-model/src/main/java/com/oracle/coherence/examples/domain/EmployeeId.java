package com.oracle.coherence.examples.domain;

import java.util.Objects;

import com.tangosol.io.pof.schema.annotation.Portable;
import com.tangosol.io.pof.schema.annotation.PortableType;

import static com.oracle.coherence.examples.domain.PofTypes.POF_TYPE_EMPLOYEE_ID;

/**
 * @author Jonathan Knight  2020.09.09
 */
@PortableType(id = POF_TYPE_EMPLOYEE_ID)
public class EmployeeId {

    @Portable
    String empId;

    public EmployeeId(String empId) {
        this.empId = empId;
    }

    public String getEmpId() {
        return empId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeId employeeId = (EmployeeId) o;
        return Objects.equals(empId, employeeId.empId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }
}
