import groovy.sql.Sql
import org.alexaoanaeliza.Purchase
import org.alexaoanaeliza.PurchaseRepository
import org.alexaoanaeliza.Sale
import org.alexaoanaeliza.SaleRepository
import org.alexaoanaeliza.Stock
import org.alexaoanaeliza.StockRepository
import org.alexaoanaeliza.User
import org.alexaoanaeliza.UserRepository
import org.alexaoanaeliza.enums.Country
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class StockRepositoryTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def userRepository = new UserRepository(url, username, password)
    def saleRepository = new SaleRepository(url, username, password)
    def purchaseRepository = new PurchaseRepository(url, username, password)
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
        def stock = new Stock('name', 'company')
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
        def stock = new Stock('name', 'company')
        def addedStock = stockRepository.add(stock)
        def prices = new HashMap()
        prices.put(LocalDate.EPOCH, 12.0D)
        stockRepository.addPricesByStock(prices, addedStock.getId())

        when:
        def date = stockRepository.getLastStockPriceByName('newName')

        then:
        date == null
    }
    
    def 'given stock and matching sale, when requesting getStockBySale, then get stock'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock('name', 'compayName')
        def addedStock = stockRepository.add(stock)
        def sale = new Sale(addedUser.getId(), stock.getId(), LocalDateTime.MAX, 12)
        def addedSale = saleRepository.add(sale)
        
        when:
        def getStock = stockRepository.getStockBySale(addedSale.getId())
        
        then:
        getStock == stock
        getStock == addedStock
    }

    def 'given stock and non-matching sale, when requesting getStockBySale, then get null'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock('name', 'compayName')
        stockRepository.add(stock)
        def sale = new Sale(addedUser.getId(), stock.getId(), LocalDateTime.MAX, 12)
        def addedSale = saleRepository.add(sale)

        when:
        def getStock = stockRepository.getStockBySale(addedSale.getId() - 1)

        then:
        getStock == null
    }

    def 'given stock and matching purchase, when requesting getStockByPurchase, then get stock'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock('name', 'compayName')
        def addedStock = stockRepository.add(stock)
        def purchase = new Purchase(addedUser.getId(), stock.getId(), LocalDateTime.MAX, 12)
        def addedPurchase = purchaseRepository.add(purchase)

        when:
        def getStock = stockRepository.getStockByPurchase(addedPurchase.getId())

        then:
        getStock == stock
        getStock == addedStock
    }

    def 'given stock and non-matching purchase, when requesting getStockByPurchase, then get null'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock('name', 'compayName')
        stockRepository.add(stock)
        def purchase = new Purchase(addedUser.getId(), stock.getId(), LocalDateTime.MAX, 12)
        def addedPurchase = purchaseRepository.add(purchase)

        when:
        def getStock = stockRepository.getStockByPurchase(addedPurchase.getId() - 1)

        then:
        getStock == null
    }

    def cleanup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptDelete.sql').text
        sql.execute(script)
    }
}
