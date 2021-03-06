package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AddressRepositoryTests {

    @Autowired private AddressRepository sut;

    @Autowired private TestEntityManager entityManager;

    @Test
    public void testAddNew() {
        Integer customerId = 43;
        Integer countryId = 234;

        Address newAddress = new Address();
        newAddress.setCustomer(new Customer(customerId));
        newAddress.setCountry(new Country(countryId));
        newAddress.setFirstName("Tobie");
        newAddress.setLastName("Abel");
        newAddress.setPhoneNumber("19094644165");
        newAddress.setAddressLine1("4213 Gordon Street");
        newAddress.setAddressLine2("Novak Building");
        newAddress.setCity("Pasadena");
        newAddress.setState("California");
        newAddress.setPostalCode("91710");

        Address savedAddress = sut.save(newAddress);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 5;
        List<Address> listAddresses = sut.findByCustomer(new Customer(customerId));
        assertThat(listAddresses.size()).isGreaterThan(0);

        listAddresses.forEach(System.out::println);
    }

    @Test
    public void testFindByIdAndCustomer() {
        Integer addressId = 1;
        Integer customerId = 5;

        Address address = sut.findByIdAndCustomer(addressId, customerId);

        assertThat(address).isNotNull();
        System.out.println(address);
    }

    @Test
    public void testUpdate() {
        Integer addressId = 3;
//        String phoneNumber = "646-232-3932";

        Address address = sut.findById(addressId).get();
//        address.setPhoneNumber(phoneNumber);
        address.setDefaultForShipping(true);

        Address updatedAddress = sut.save(address);
//        assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    public void testDeleteByIdAndCustomer() {
        Integer addressId = 1;
        Integer customerId = 5;

        sut.deleteByIdAndCustomer(addressId, customerId);

        Address address = sut.findByIdAndCustomer(addressId, customerId);
        assertThat(address).isNull();
    }

    @Test
    public void testSetDefaultShippingAddress(){
        Integer addressId = 4;

        sut.setDefaultAddress(addressId);

        Address address = sut.findById(addressId).get();
        assertThat(address.isDefaultForShipping()).isTrue();
    }

    @Test
    public void testSetNonDefaultAddressess(){
        Integer addressId = 4;
        Integer customerId = 43;

        sut.setNonDefaultForOthers(addressId, customerId);
    }

    @Test
    public void testGetDefault(){
        Integer customerId = 43;
        Address address = sut.findDefaultByCustomer(43);

        assertThat(address).isNotNull();
        System.out.println("address = " + address);
    }
}
