import org.alexaoanaeliza.Purchase
import org.alexaoanaeliza.PurchaseRepository
import org.alexaoanaeliza.exception.DatabaseException
import spock.lang.Specification

import java.time.LocalDateTime

class PurchaseRepositoryExceptionalTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def purchaseRepository = new PurchaseRepository(url, username, password)

    def 'given no tables created, when requesting add, then throw error'() {
        given:
        def purchase = new Purchase(1, 1, LocalDateTime.now(), 12)

        when:
        purchaseRepository.add(purchase)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getPurchasesByUser, then throw error'() {
        when:
        purchaseRepository.getPurchasesByUser(1)

        then:
        thrown(DatabaseException)
    }
}
