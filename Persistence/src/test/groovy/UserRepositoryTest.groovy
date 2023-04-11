import org.alexaoanaeliza.DebitCard
import org.alexaoanaeliza.DebitCardRepository
import org.alexaoanaeliza.User
import org.alexaoanaeliza.UserRepository
import org.alexaoanaeliza.enums.Country
import org.alexaoanaeliza.enums.DebitCardType
import spock.lang.Specification
import groovy.sql.Sql

import java.time.LocalDate

class UserRepositoryTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def userRepository = new UserRepository(url, username, password)
    def debitRepository = new DebitCardRepository(url, username, password)

    def setup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptCreate.sql').text
        sql.execute(script)
    }

    def 'when requesting getInstance, then get UserRepository instance'() {
        when:
        def userRepository = UserRepository.getInstance()

        then:
        userRepository != null
    }

    def 'given one user, when requesting add, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")

        when:
        def addedUser = userRepository.add(user)

        then:
        user == addedUser
    }

    def 'given one user and matching id, when requesting getById, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)

        when:
        def getUser = userRepository.getById(addedUser.getId())

        then:
        user == getUser
    }

    def 'given one user and non-matching id, when requesting getById, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)

        when:
        def getUser = userRepository.getById(addedUser.getId() - 1)

        then:
        getUser == null
    }

    def 'given no user, when requesting getAll, then get empty set'() {
        when:
        def users = userRepository.getAll()

        then:
        users.isEmpty()
    }

    def 'given one user, when requesting getAll, then return users'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.add(user)

        when:
        def users = userRepository.getAll()

        then:
        users.size() == 1
        users.contains(user)
    }

    def 'given one user and matching email, when requesting getByEmail, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.add(user)

        when:
        def getUser = userRepository.getByEmail("email")

        then:
        getUser == user
    }

    def 'given one user and non-matching email, when requesting getByEmail, then get null'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.add(user)

        when:
        def getUser = userRepository.getByEmail("newEmail")

        then:
        getUser == null
    }

    def 'given user and matching id, when requesting getTodaySoldByUser, then get 0'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)

        when:
        def sold = userRepository.getTodaySoldByUser(addedUser.getId())

        then:
        sold == 0
    }

    def 'given user and matching id, when requesting getReturnValueByUser, then get 0'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)

        when:
        def sold = userRepository.getReturnValueByUser(addedUser.getId())

        then:
        sold == 0
    }

    def 'given user and matching id, when requesting getReturnPercentageByUser, then get 0'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)

        when:
        def sold = userRepository.getReturnPercentageByUser(addedUser.getId())

        then:
        sold == 0
    }

    def 'given user and modified user, when requesting update, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        addedUser.depositAmount(12)

        when:
        def updatedUser = userRepository.update(addedUser)

        then:
        updatedUser.getAvailableAmount() == 12
        updatedUser.getInvestedAmount() == 12
    }

    def 'given user and matching debit card, when requesting getOwnerByDebitCard, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())
        def addedDebitCard = debitRepository.add(debitCard)

        when:
        def getUser = userRepository.getOwnerByDebitCard(addedDebitCard.getId())

        then:
        user == getUser
        addedUser == getUser
    }

    def 'given user and non-matching debit card, when requesting getOwnerByDebitCard, then get null'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())
        def addedDebitCard = debitRepository.add(debitCard)

        when:
        def getUser = userRepository.getOwnerByDebitCard(addedDebitCard.getId() - 1)

        then:
        getUser == null
    }

    def cleanup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptDelete.sql').text
        sql.execute(script)
    }
}
