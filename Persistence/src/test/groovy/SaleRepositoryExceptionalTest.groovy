import org.alexaoanaeliza.Sale
import org.alexaoanaeliza.SaleRepository
import org.alexaoanaeliza.exception.DatabaseException
import spock.lang.Specification

import java.time.LocalDateTime

class SaleRepositoryExceptionalTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def saleRepository = new SaleRepository(url, username, password)

    def 'given no tables created, when requesting add, then throw error'() {
        given:
        def sale = new Sale(1, 1, LocalDateTime.now(), 12)

        when:
        saleRepository.add(sale)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getSalesByUser, then throw error'() {
        when:
        saleRepository.getSalesByUser(1)

        then:
        thrown(DatabaseException)
    }
}
