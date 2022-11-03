import org.alexaoanaeliza.Address;
import org.alexaoanaeliza.AddressRepository;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.UserRepository;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.DatabaseException;
import org.alexaoanaeliza.exception.FileException;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    @InjectMocks
    private UserRepository userRepository;
    @InjectMocks
    private AddressRepository addressRepository;

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
        ReflectionTestUtils.setField(userRepository, "url", url);
        ReflectionTestUtils.setField(userRepository, "password", password);
        ReflectionTestUtils.setField(userRepository, "username", username);
        ReflectionTestUtils.setField(userRepository, "addressRepository", addressRepository);
    }

    @Test
    public void addUserTest() {
        setUp();
        Address address = new Address(Country.BELGIUM, "County", "City", "Street", "Number", "Apartment");
        LocalDate birthday = LocalDate.now();
        User user = new User("FirstName", "LastName", "PersonalNumber", address, "PhoneNumber", birthday, "Email", "Password");
        try {
            User addedUser = userRepository.add(user);
            assertTrue(true);
            assertEquals(addedUser.getPassword(), "Password");
            assertEquals(addedUser.getAddress(), address);
            assertEquals(addedUser.getBankAccounts().size(), 0);
            assertEquals(addedUser.getBirthday(), birthday);
            assertEquals(addedUser.getDebitCards().size(), 0);
            assertEquals(addedUser.getFirstName(), "FirstName");
            assertEquals(addedUser.getLastName(), "LastName");
            assertEquals(addedUser.getPersonalNumber(), "PersonalNumber");
            assertEquals(addedUser.getEmail(), "Email");
            assertEquals(addedUser.getPassword(), "Password");
            assertEquals(addedUser.getPhoneNumber(), "PhoneNumber");
            assertEquals(addedUser, user);
        } catch (DatabaseException databaseException) {
            fail();
        }
    }

    @Test
    public void getAllUsersTest() {
        setUp();
        try {
            userRepository.getAll();
            assertTrue(true);
        } catch (DatabaseException databaseException) {
            fail();
        }
    }

    @Test
    public void getUserByIdTest() {
        setUp();
        try {
            User user = userRepository.getById(1L);
            assertEquals(user.getPassword(), "Password");
            assertEquals(user.getBankAccounts().size(), 0);
            assertEquals(user.getDebitCards().size(), 0);
            assertEquals(user.getFirstName(), "FirstName");
            assertEquals(user.getLastName(), "LastName");
            assertEquals(user.getPersonalNumber(), "PersonalNumber");
            assertEquals(user.getEmail(), "Email");
            assertEquals(user.getPassword(), "Password");
            assertEquals(user.getPhoneNumber(), "PhoneNumber");
        } catch (DatabaseException databaseException) {
            fail();
        }
        try {
            User user = userRepository.getById(0L);
            assertNull(user);
        } catch (DatabaseException databaseException) {
            fail();
        }
    }

    @Test
    public void getUserByEmail() {
        setUp();
        try {
            User user = userRepository.getByEmail("Email");
            assertEquals(user.getPassword(), "Password");
            assertEquals(user.getBankAccounts().size(), 0);
            assertEquals(user.getDebitCards().size(), 0);
            assertEquals(user.getFirstName(), "FirstName");
            assertEquals(user.getLastName(), "LastName");
            assertEquals(user.getPersonalNumber(), "PersonalNumber");
            assertEquals(user.getEmail(), "Email");
            assertEquals(user.getPassword(), "Password");
            assertEquals(user.getPhoneNumber(), "PhoneNumber");
        } catch (DatabaseException databaseException) {
            fail();
        }
        try {
            User user = userRepository.getByEmail("email");
            assertNull(user);
        } catch (DatabaseException databaseException) {
            fail();
        }
    }
}
