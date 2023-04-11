import org.alexaoanaeliza.DebitCard
import org.alexaoanaeliza.DebitCardRepository
import org.alexaoanaeliza.enums.DebitCardType
import org.alexaoanaeliza.exception.DatabaseException
import spock.lang.Specification

import java.time.LocalDate

class DebitCardRepositoryExceptionalTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def debitCardRepository = new DebitCardRepository(url, username, password)

    def 'given no tables created, when requesting add, then throw error'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)

        when:
        debitCardRepository.add(debitCard)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getById, then throw error'() {
        when:
        debitCardRepository.getById(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getByData, then throw error'() {
        when:
        debitCardRepository.getByData(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now())

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getDebitCardsByUser, then throw error'() {
        when:
        debitCardRepository.getDebitCardsByUser(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getAll, then throw error'() {
        when:
        debitCardRepository.getAll()

        then:
        thrown(DatabaseException)
    }
}
