import groovy.sql.Sql
import org.alexaoanaeliza.Purchase
import org.alexaoanaeliza.PurchaseRepository
import org.alexaoanaeliza.Stock
import org.alexaoanaeliza.StockRepository
import org.alexaoanaeliza.User
import org.alexaoanaeliza.UserRepository
import org.alexaoanaeliza.enums.Country
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class PurchaseRepositoryTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def userRepository = new UserRepository(url, username, password)
    def purchaseRepository = new PurchaseRepository(url, username, password)
    def stockRepository = new StockRepository(url, username, password)

    def setup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptCreate.sql').text
        sql.execute(script)
    }

    def 'when requesting getInstance, the get PurchaseRepository instance'() {
        when:
        def purchaseRepository = PurchaseRepository.getInstance()

        then:
        purchaseRepository != null
    }

    def 'given matching userId, when requesting getPurchasesByUser, then get purchases'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock("name", "company")
        def addedStock = stockRepository.add(stock)
        def purchase = new Purchase(addedUser.getId(), addedStock.getId(), LocalDateTime.now(), 12)
        purchaseRepository.add(purchase)

        when:
        def purchases = purchaseRepository.getPurchasesByUser(addedUser.getId())

        then:
        purchases.size() == 1
        purchases.contains(purchase)
    }

    def 'given non-matching userId, when requesting getPurchasesByUser, then get purchases'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock("name", "company")
        def addedStock = stockRepository.add(stock)
        def purchase = new Purchase(addedUser.getId(), addedStock.getId(), LocalDateTime.now(), 12)
        purchaseRepository.add(purchase)

        when:
        def purchases = purchaseRepository.getPurchasesByUser(addedUser.getId() - 1)

        then:
        purchases.isEmpty()
    }

    def 'given purchase, when requesting add, then get purchase'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock("name", "company")
        def addedStock = stockRepository.add(stock)
        def purchase = new Purchase(addedUser.getId(), addedStock.getId(), LocalDateTime.now(), 12)

        when:
        def addedPurchase = purchaseRepository.add(purchase)

        then:
        addedPurchase == purchase
    }

    def cleanup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptDelete.sql').text
        sql.execute(script)
    }
}
