import groovy.sql.Sql
import org.alexaoanaeliza.Stock
import org.alexaoanaeliza.StockRepository
import org.alexaoanaeliza.UserRepository
import spock.lang.Specification

import java.time.LocalDate

class StockRepositoryTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def userRepository = new UserRepository(url, username, password)
    def stockRepository = new StockRepository(url, username, password)

    def setup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptCreate.sql').text
        sql.execute(script)
    }

    def 'when requesting getInstance, the get StockRepository instance'() {
        when:
        def stockRepository = StockRepository.getInstance()

        then:
        stockRepository != null
    }

    def 'given stock, when requesting add, then get stock'() {
        given:
        def stock = new Stock('name', 'company')

        when:
        def addedStock = stockRepository.add(stock)

        then:
        stock == addedStock
    }

    def 'given stock and matching id, when requesting getById, then get stock'() {
        given:
        def stock = new Stock('name', 'company')
        def addedStock = stockRepository.add(stock)

        when:
        def getStock = stockRepository.getById(addedStock.getId())

        then:
        getStock == addedStock
        getStock == stock
    }

    def 'given stock and matching name, when requesting getById, then get stock'() {
        given:
        def stock = new Stock('name', 'company')
        def addedStock = stockRepository.add(stock)

        when:
        def getStock = stockRepository.getStockByName(addedStock.getName())

        then:
        getStock == addedStock
        getStock == stock
    }

    def 'given stock and non-matching id, when requesting getById, then get null'() {
        given:
        def stock = new Stock('name', 'company')
        def addedStock = stockRepository.add(stock)

        when:
        def getStock = stockRepository.getById(addedStock.getId() - 1)

        then:
        getStock == null
    }

    def 'given stock and non-matching name, when requesting getById, then get stock'() {
        given:
        def stock = new Stock('name', 'company')
        def addedStock = stockRepository.add(stock)

        when:
        def getStock = stockRepository.getStockByName(addedStock.getName().substring(1))

        then:
        getStock == null
    }

    def 'given no stocks, when requesting getAll, then get empty set'() {
        when:
        def stocks = stockRepository.getAll()

        then:
        stocks.isEmpty()
    }

    def 'given one stock, when requesting getAll, then get stocks'() {
        given:
        def stock = new Stock('name', 'company')
        stockRepository.add(stock)

        when:
        def stocks = stockRepository.getAll()

        then:
        stocks.size() == 1
        stocks.contains(stock)
    }

    def 'given one price for stock and matching name, when requesting getStockPrices, then get price'() {
        given:
        def stock = new Stock('name', 'company')
        def addedStock = stockRepository.add(stock)
        def prices = new HashMap()
        prices.put(LocalDate.EPOCH, 12.0D)
        stockRepository.addPricesByStock(prices, addedStock.getId())

        when:
        def getPrices = stockRepository.getStockPrices('name')

        then:
        getPrices.size() == 1
        getPrices.get(LocalDate.EPOCH) == 12
    }

    def 'given one price for stock and non-matching name, when requesting getStockPrices, then get price'() {
        given:
        def stock = new Stock('name', 'company')
        stockRepository.add(stock)

        when:
        def prices = stockRepository.getStockPrices('newName')

        then:
        prices.isEmpty()
    }

    def 'given stock and matching name, when requesting getLastStockPriceByName, then get date'() {
        given:
        def stock = new Stock('name1', 'company')
        stockRepository.add(stock)
        def addedStock = stockRepository.add(stock)
        def prices = new HashMap()
        prices.put(LocalDate.EPOCH, 12.0D)
        stockRepository.addPricesByStock(prices, addedStock.getId())

        when:
        def date = stockRepository.getLastStockPriceByName('name')

        then:
        date == LocalDate.EPOCH
    }

    def 'given stock and non-matching name, when requesting getLastStockPriceByName, then get null'() {
        given:
        def stock = new Stock('name2', 'company')
        stockRepository.add(stock)
        def addedStock = stockRepository.add(stock)
        def prices = new HashMap()
        prices.put(LocalDate.EPOCH, 12.0D)
        stockRepository.addPricesByStock(prices, addedStock.getId())

        when:
        def date = stockRepository.getLastStockPriceByName('newName')

        then:
        date == null
    }

    def cleanup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptDelete.sql').text
        sql.execute(script)
    }
}
