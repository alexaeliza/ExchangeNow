import org.alexaoanaeliza.User
import org.alexaoanaeliza.UserRepository
import org.alexaoanaeliza.enums.Country
import org.alexaoanaeliza.exception.DatabaseException
import spock.lang.Specification

import java.time.LocalDate

class UserRepositoryExceptionalTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def userRepository = new UserRepository(url, username, password)

    def 'given no tables created, when requesting add, then throw error'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")

        when:
        userRepository.add(user)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting update, then throw error'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")

        when:
        userRepository.update(user)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getAll, then throw error'() {
        when:
        userRepository.getAll()

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getById, then throw error'() {
        when:
        userRepository.getById(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getByEmail, then throw error'() {
        when:
        userRepository.getByEmail("email")

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getTodaySoldByUser, then throw error'() {
        when:
        userRepository.getTodaySoldByUser(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getReturnValueByUser, then throw error'() {
        when:
        userRepository.getReturnValueByUser(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getReturnPercentageByUser, then throw error'() {
        when:
        userRepository.getReturnPercentageByUser(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getOwnerByDebitCrd, then throw error'() {
        when:
        userRepository.getOwnerByDebitCard(1)

        then:
        thrown(DatabaseException)
    }
}
