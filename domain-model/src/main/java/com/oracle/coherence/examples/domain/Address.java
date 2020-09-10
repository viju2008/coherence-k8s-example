package com.oracle.coherence.examples.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.tangosol.io.pof.schema.annotation.Portable;
import com.tangosol.io.pof.schema.annotation.PortableType;

import static com.oracle.coherence.examples.domain.PofTypes.POF_TYPE_ADDRESS;

/**
 * @author Jonathan Knight  2020.09.09
 */
@Entity
@PortableType(id = POF_TYPE_ADDRESS)
public class Address {

    @Id
    @Portable
    private String roll;

    @Portable
    private String lineOne;

    @Portable
    private String lineTwo;

    @Portable
    private String city;

    @Portable
    private String postalCode;

    @Portable
    private String country;

    public Address() {
    }

    String getRoll() {
        return roll;
    }

    void setRoll(String roll) {
        this.roll = roll;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "roll='" + roll + '\'' +
                "lineOne='" + lineOne + '\'' +
                ", lineTwo='" + lineTwo + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
