package com.oracle.coherence.examples.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tangosol.io.pof.schema.annotation.Portable;
import com.tangosol.io.pof.schema.annotation.PortableType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import static com.oracle.coherence.examples.domain.PofTypes.POF_TYPE_EMPLOYEE;

/**
 * @author Jonathan Knight  2020.09.09
 */
@Entity
@PortableType(id = POF_TYPE_EMPLOYEE)
public class Employee {

    @Id
    @Portable
    private String empId;

    @Portable
    private String firstName;

    @Portable
    private String lastName;

    @Portable
    private String deptName;

 

    /**
     * Required by JPA.
     */
    public Employee() {
    }

    public Employee(String empIdNumber, String firstName, String lastName,
                   String deptName) {
        this.empId = empIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deptName = deptName;
       
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
       
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

  

    @Override
    public String toString() {
        return "Employee{" +
                "empIdNumber='" + empId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", deptName='" + deptName + '\'' + '}';
    }
}
