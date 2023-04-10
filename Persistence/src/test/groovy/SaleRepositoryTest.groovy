import groovy.sql.Sql
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

class SaleRepositoryTest extends Specification {
    def url = 'jdbc:h2:mem:testing'
    def username = 'sa'
    def password = ''
    def userRepository = new UserRepository(url, username, password)
    def saleRepository = new SaleRepository(url, username, password)
    def stockRepository = new StockRepository(url, username, password)

    def setup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptCreate.sql').text
        sql.execute(script)
    }

    def 'when requesting getInstance, the get SaleRepository instance'() {
        when:
        def saleRepository = SaleRepository.getInstance()

        then:
        saleRepository != null
    }

    def 'given matching userId, when requesting getSalesByUser, then get sales'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock("name", "company")
        def addedStock = stockRepository.add(stock)
        def sale = new Sale(addedUser.getId(), addedStock.getId(), LocalDateTime.now(), 12)
        saleRepository.add(sale)

        when:
        def sales = saleRepository.getSalesByUser(addedUser.getId())

        then:
        sales.size() == 1
        sales.contains(sale)
    }

    def 'given non-matching userId, when requesting getSalesByUser, then get sales'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock("name", "company")
        def addedStock = stockRepository.add(stock)
        def sale = new Sale(addedUser.getId(), addedStock.getId(), LocalDateTime.now(), 12)
        saleRepository.add(sale)

        when:
        def sales = saleRepository.getSalesByUser(addedUser.getId() - 1)

        then:
        sales.isEmpty()
    }

    def 'given sale, when requesting add, then get sale'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def addedUser = userRepository.add(user)
        def stock = new Stock("name", "company")
        def addedStock = stockRepository.add(stock)
        def sale = new Sale(addedUser.getId(), addedStock.getId(), LocalDateTime.now(), 12)

        when:
        def addedSale = saleRepository.add(sale)

        then:
        addedSale == sale
    }

    def cleanup() {
        def sql = Sql.newInstance(url, username, password, 'org.h2.Driver')
        def script = new File('/Users/alexaoanaeliza/Desktop/ExchangeNow/Persistence/src/test/resources/scriptDelete.sql').text
        sql.execute(script)
    }
}
