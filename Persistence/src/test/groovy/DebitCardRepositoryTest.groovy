import groovy.sql.Sql
import org.alexaoanaeliza.DebitCard
import org.alexaoanaeliza.DebitCardRepository
import org.alexaoanaeliza.User
import org.alexaoanaeliza.UserRepository
import org.alexaoanaeliza.enums.Country
import org.alexaoanaeliza.enums.DebitCardType
import org.alexaoanaeliza.exception.DatabaseException
import spock.lang.Specification

import java.time.LocalDate

class DebitCardRepositoryTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def userRepository = new UserRepository(url, username, password)
    def debitCardRepository = new DebitCardRepository(url, username, password)

    def setup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptCreate.sql').text
        sql.execute(script)
    }

    def 'when requesting getInstance, then get DebitCardRepository instance'() {
        when:
        def debitCardRepository = DebitCardRepository.getInstance()

        then:
        debitCardRepository != null
    }

    def 'given a debit card an existing user, when requesting add, then get debit card'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())

        when:
        def addedDebitCard = debitCardRepository.add(debitCard)

        then:
        addedDebitCard == debitCard
    }

    def 'given a debit card and non-existing user, when requesting add, then throw error'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)

        when:
        debitCardRepository.add(debitCard)

        then:
        thrown(DatabaseException)
    }

    def 'given no debit cards for user, when requesting getDebitCardsByUser, then get empty set'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)

        when:
        def debitCards = debitCardRepository.getDebitCardsByUser(addedUser.getId())

        then:
        debitCards.isEmpty()
    }

    def 'given one debit card for user, when requesting getDebitCardsByUser, then get empty set'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())
        debitCardRepository.add(debitCard)

        when:
        def debitCards = debitCardRepository.getDebitCardsByUser(addedUser.getId())

        then:
        debitCards.size() == 1
        debitCards.contains(debitCard)
    }

    def 'given no user, when requesting getDebitCardsByUser, then get empty set'() {
        when:
        def debitCards = debitCardRepository.getDebitCardsByUser(1)

        then:
        debitCards.isEmpty()
    }

    def 'given matching data, when requesting getByData, then get debitCard'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())
        debitCardRepository.add(debitCard)

        when:
        def getDebitCard = debitCardRepository.getByData(DebitCardType.VISA, "cardNumber", "cvv", debitCard.getExpireDate())

        then:
        getDebitCard == debitCard
    }

    def 'given non-matching data, when requesting getByData, then get null'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())
        debitCardRepository.add(debitCard)

        when:
        def getDebitCard = debitCardRepository.getByData(DebitCardType.VISA, "newCardNumber", "cvv", debitCard.getExpireDate())

        then:
        getDebitCard == null
    }

    def 'given no debit cards, when requesting getAll, then get empty set'() {
        when:
        def debitCards = debitCardRepository.getAll()

        then:
        debitCards.isEmpty()
    }

    def 'given one debit card, when requesting getAll, get debitCards'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())
        debitCardRepository.add(debitCard)

        when:
        def debitCards = debitCardRepository.getAll()

        then:
        debitCards.size() == 1
        debitCards.contains(debitCard)
    }

    def 'given matching id, when requesting getById, then get debitCard'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), addedUser.getId())
        def addedDebitCard = debitCardRepository.add(debitCard)

        when:
        def getDebitCard = debitCardRepository.getById(addedDebitCard.getId())

        then:
        getDebitCard == debitCard
        getDebitCard == addedDebitCard
    }

    def 'given non-matching id, when requesting getById, then get null'() {
        when:
        def getDebitCard = debitCardRepository.getById(1)

        then:
        getDebitCard == null
    }

    def cleanup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptDelete.sql').text
        sql.execute(script)
    }
}
