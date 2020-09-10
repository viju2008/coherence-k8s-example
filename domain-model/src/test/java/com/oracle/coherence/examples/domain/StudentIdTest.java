package com.oracle.coherence.examples.domain;

import com.tangosol.io.pof.ConfigurablePofContext;
import com.tangosol.util.Binary;
import com.tangosol.util.ExternalizableHelper;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class StudentIdTest {

    private final ConfigurablePofContext serializer = new ConfigurablePofContext("student-pof-config.xml");

    @Test
    public void shouldSerializeUsingPOF() {
        StudentId id = new StudentId("abcd-1234");
        Binary binary = ExternalizableHelper.toBinary(id, serializer);
        StudentId result = ExternalizableHelper.fromBinary(binary, serializer);

        assertThat(result, is(notNullValue()));
        assertThat(result.getRollNumber(), is(id.getRollNumber()));
    }

    @Test
    public void shouldBeEqual() {
        StudentId idOne = new StudentId("abcd-1234");
        StudentId idTwo = new StudentId("abcd-1234");

        assertThat(idOne.equals(idTwo), is(true));
        assertThat(idTwo.equals(idOne), is(true));
    }

    @Test
    public void shouldNotBeEqual() {
        StudentId idOne = new StudentId("abcd-1234");
        StudentId idTwo = new StudentId("abcd-1234");

        assertThat(idOne.equals(idTwo), is(true));
        assertThat(idTwo.equals(idOne), is(true));
    }

    @Test
    public void shouldHaveSameHash() {
        StudentId idOne = new StudentId("abcd-1234");
        StudentId idTwo = new StudentId("abcd-1234");

        assertThat(idOne.hashCode(), is(idTwo.hashCode()));
    }
}
