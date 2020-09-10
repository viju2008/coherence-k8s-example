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

import static com.oracle.coherence.examples.domain.PofTypes.POF_TYPE_STUDENT;

/**
 * @author Jonathan Knight  2020.09.09
 */
@Entity
@PortableType(id = POF_TYPE_STUDENT)
public class Student {

    @Id
    @Portable
    private String roll;

    @Portable
    private String firstName;

    @Portable
    private String lastName;

    @Portable
    private String className;

    @OneToOne()
    @Cascade(CascadeType.ALL)
    @Portable
    private Address address;

    /**
     * Required by JPA.
     */
    public Student() {
    }

    public Student(String rollNumber, String firstName, String lastName,
                   String className, Address address) {
        this.roll = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.className = className;
        setAddress(address);
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

    public String getRollNumber() {
        return roll;
    }

    public void setRollNumber(String roll) {
        this.roll = roll;
        if (address != null) {
            address.setRoll(roll);
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (this.address != null) {
            this.address.setRoll(this.roll);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNumber='" + roll + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", className='" + className + '\'' +
                ", address=" + address +
                '}';
    }
}
