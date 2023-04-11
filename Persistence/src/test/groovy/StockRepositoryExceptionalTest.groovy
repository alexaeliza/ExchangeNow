import org.alexaoanaeliza.Stock
import org.alexaoanaeliza.StockRepository
import org.alexaoanaeliza.exception.DatabaseException
import spock.lang.Specification

import java.time.LocalDate

class StockRepositoryExceptionalTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def stockRepository = new StockRepository(url, username, password)

    def 'given no tables created, when requesting add, then throw error'() {
        given:
        def stock = new Stock('name', 'companyName')

        when:
        stockRepository.add(stock)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getStockByName, then throw error'() {
        when:
        stockRepository.getStockByName('name')

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getStockByPurchase, then throw error'() {
        when:
        stockRepository.getStockByPurchase(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getStockBySale, then throw error'() {
        when:
        stockRepository.getStockBySale(1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getLastStockPriceByName, then throw error'() {
        when:
        stockRepository.getLastStockPriceByName('name')

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getStockPrices, then throw error'() {
        when:
        stockRepository.getStockPrices('name')

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting addPricesByStock, then throw error'() {
        given:
        def prices = new HashMap<LocalDate, Double>()
        prices.put(LocalDate.now(), 12D)

        when:
        stockRepository.addPricesByStock(prices, 1)

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getAll, then throw error'() {
        when:
        stockRepository.getAll()

        then:
        thrown(DatabaseException)
    }

    def 'given no tables created, when requesting getById, then throw error'() {
        when:
        stockRepository.getById(1)

        then:
        thrown(DatabaseException)
    }
}
