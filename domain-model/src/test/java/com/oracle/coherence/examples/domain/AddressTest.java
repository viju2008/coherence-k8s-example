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
public class AddressTest {

    private final ConfigurablePofContext serializer = new ConfigurablePofContext("student-pof-config.xml");

    @Test
    public void shouldSerializeWithPOF() {
        Address address = new Address();
        address.setLineOne("Flat No. 100, Triveni Apartments");
        address.setLineTwo("Pitam Pura");
        address.setCity("NEW DELHI");
        address.setPostalCode("110034");
        address.setCountry("India");

        Binary binary = ExternalizableHelper.toBinary(address, serializer);
        Address result = ExternalizableHelper.fromBinary(binary, serializer);

        assertThat(result, is(notNullValue()));
        assertThat(result.getLineOne(), is(address.getLineOne()));
        assertThat(result.getLineTwo(), is(address.getLineTwo()));
        assertThat(result.getCity(), is(address.getCity()));
        assertThat(result.getPostalCode(), is(address.getPostalCode()));
        assertThat(result.getCountry(), is(address.getCountry()));
    }

}
