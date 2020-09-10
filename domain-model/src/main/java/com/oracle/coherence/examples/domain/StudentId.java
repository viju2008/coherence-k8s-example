package com.oracle.coherence.examples.domain;

import java.util.Objects;

import com.tangosol.io.pof.schema.annotation.Portable;
import com.tangosol.io.pof.schema.annotation.PortableType;

import static com.oracle.coherence.examples.domain.PofTypes.POF_TYPE_STUDENT_ID;

/**
 * @author Jonathan Knight  2020.09.09
 */
@PortableType(id = POF_TYPE_STUDENT_ID)
public class StudentId {

    @Portable
    String rollNumber;

    public StudentId(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentId studentId = (StudentId) o;
        return Objects.equals(rollNumber, studentId.rollNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rollNumber);
    }
}
