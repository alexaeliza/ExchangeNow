import org.alexaoanaeliza.Address;
import org.alexaoanaeliza.AddressRepository;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AddressRepositoryTest {
    @InjectMocks
    private AddressRepository addressRepository = AddressRepository.getInstance();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Properties properties = new Properties();
        try {
            properties.load(AddressRepository.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            throw new FileException(ioException.getMessage());
        }

        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        String url = properties.getProperty("urlTest");
        ReflectionTestUtils.setField(addressRepository, "url", url);
        ReflectionTestUtils.setField(addressRepository, "password", password);
        ReflectionTestUtils.setField(addressRepository, "username", username);
    }

    @Test
    public void addAddressTest() {
        setUp();
        Address address = new Address(Country.BELGIUM, "County", "City", "Street", "Number", "Apartment");
        try {
            Address addedAddress = addressRepository.add(address);
            assertTrue(true);
            assertEquals(addedAddress.getCountry(), Country.BELGIUM);
            assertEquals(addedAddress.getApartment(), "Apartment");
            assertEquals(addedAddress.getCity(), "City");
            assertEquals(addedAddress.getStreet(), "Street");
            assertEquals(addedAddress.getNumber(), "Number");
            assertEquals(addedAddress.getCounty(), "County");
            assertEquals(addedAddress, address);
        } catch (DatabaseException databaseException) {
            fail();
        }
    }

    @Test
    public void getAllAddressesTest() {
        setUp();
        try {
            addressRepository.getAll();
            assertTrue(true);
        } catch (DatabaseException databaseException) {
            fail();
        }
    }

    @Test
    public void getAddressByIdTest() {
        setUp();
        try {
            Address address = addressRepository.getById(1L);
            assertTrue(true);
            assertEquals(address.getCountry(), Country.BELGIUM);
            assertEquals(address.getApartment(), "Apartment");
            assertEquals(address.getCity(), "City");
            assertEquals(address.getStreet(), "Street");
            assertEquals(address.getNumber(), "Number");
            assertEquals(address.getCounty(), "County");
        } catch (DatabaseException databaseException) {
            fail();
        }
        try {
            Address address = addressRepository.getById(0L);
            assertNull(address);
        } catch (DatabaseException databaseException) {
            fail();
        }
    }
}
