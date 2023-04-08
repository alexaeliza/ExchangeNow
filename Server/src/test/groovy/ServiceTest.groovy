import org.alexaoanaeliza.DebitCard
import org.alexaoanaeliza.Stock
import org.alexaoanaeliza.User
import org.alexaoanaeliza.asbtractRepository.DebitCardRepositoryInterface
import org.alexaoanaeliza.asbtractRepository.PurchaseRepositoryInterface
import org.alexaoanaeliza.asbtractRepository.SaleRepositoryInterface
import org.alexaoanaeliza.asbtractRepository.StockRepositoryInterface
import org.alexaoanaeliza.asbtractRepository.UserRepositoryInterface
import org.alexaoanaeliza.enums.Country
import org.alexaoanaeliza.enums.DebitCardType
import org.alexaoanaeliza.exception.ServiceException
import org.alexaoanaeliza.server.Service
import spock.lang.Specification

import java.time.LocalDate

class ServiceTest extends Specification {
    def userRepository = Mock(UserRepositoryInterface)
    def debitCardRepository = Mock(DebitCardRepositoryInterface)
    def stockRepository = Mock(StockRepositoryInterface)
    def saleRepository = Mock(SaleRepositoryInterface)
    def purchaseRepository = Mock(PurchaseRepositoryInterface)
    def service = new Service(userRepository, debitCardRepository, stockRepository, saleRepository, purchaseRepository)

    def 'given an existing email and matching password, when requesting loginUser, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getByEmail("email") >> user

        when:
        def loginUser = service.loginUser("email", "password")

        then:
        loginUser == user
    }

    def 'given existing email and non-matching password, when requesting loginUser, then throw error'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getByEmail("email") >> user

        when:
        service.loginUser("email", "newPassword")

        then:
        def error = thrown(ServiceException)
        error.message == 'The password does not match this account'
    }

    def 'given non-existing email, when requesting loginUser, then throw error'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getByEmail("email") >> user

        when:
        service.loginUser("newEmail", "password")

        then:
        def error = thrown(ServiceException)
        error.message == 'This email is not associated with any account'
    }

    def 'given existing email, when requesting getUserByEmail, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getByEmail("email") >> user

        when:
        def getUser = service.getUserByEmail("email")

        then:
        getUser == user
    }

    def 'given non-existing email, when requesting getUserByEmail, then get user'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getByEmail("email") >> user

        when:
        def getUser = service.getUserByEmail("newEmail")

        then:
        getUser == null
    }

    def 'given existing debitCard id, when requesting getDebitCardById, then get debitCard'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        debitCardRepository.getById(1) >> debitCard

        when:
        def getDebitCard = service.getDebitCardById(1)

        then:
        debitCard == getDebitCard
    }

    def 'given non-existing debitCard id, when requesting getDebitCardById, then get debitCard'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        debitCardRepository.getById(1) >> debitCard

        when:
        def getDebitCard = service.getDebitCardById(2)

        then:
        getDebitCard == null
    }

    def 'given existing cardNumber, cvv, expireData and cardType, when requesting getDebitCardByData, then get debitCard'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        debitCardRepository.getByData(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now()) >> debitCard

        when:
        def getDebitCard = service.getDebitCardByData("cardNumber", "cvv", LocalDate.now(), DebitCardType.VISA)

        then:
        getDebitCard == debitCard
    }

    def 'given non-existing cardNumber, cvv, expireData and cardType, when requesting getDebitCardByData, then get debitCard'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        debitCardRepository.getByData(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now()) >> debitCard

        when:
        def getDebitCard = service.getDebitCardByData("newCardNumber", "newCvv", LocalDate.now().minusDays(1), DebitCardType.MAESTRO)

        then:
        getDebitCard == null
    }

    def 'when requesting getStocks, then get list of stocks'() {
        given:
        def appleStock = new Stock("AAPL", "Apple Inc.")
        def googleStock = new Stock("GOOGL", "Google")
        stockRepository.getAll() >> [appleStock, googleStock]

        when:
        def stocks = service.getStocks()

        then:
        stocks.size() == 2
        stocks.contains(appleStock)
        stocks.contains(googleStock)
    }

    def 'given existing user, when requesting getTodaySoldByUser, then get todaySold'() {
        given:
        def user = new User(1, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getTodaySoldByUser(1) >> 100.0

        when:
        def todaySold = service.getTodaySoldByUser(user)

        then:
        todaySold == 100.0
    }

    def 'given non-existing user, when requesting getTodaySoldByUser, then get todaySold'() {
        given:
        def user = new User(2, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getTodaySoldByUser(1) >> 100.0

        when:
        def todaySold = service.getTodaySoldByUser(user)

        then:
        todaySold == null
    }

    def 'given existing user, when requesting getReturnValueByUser, then get returnValue'() {
        given:
        def user = new User(1, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnValueByUser(1) >> 100.0

        when:
        def todaySold = service.getReturnValueByUser(user)

        then:
        todaySold == 100.0
    }

    def 'given non-existing user, when requesting getReturnValueByUser, then get returnValue'() {
        given:
        def user = new User(2, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnValueByUser(1) >> 100.0

        when:
        def todaySold = service.getReturnValueByUser(user)

        then:
        todaySold == null
    }

    def 'given existing user, when requesting getReturnValueByUser, then get returnValue'() {
        given:
        def user = new User(1, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnPercentageByUser(1) >> 100.0

        when:
        def todaySold = service.getReturnPercentageByUser(user)

        then:
        todaySold == 100.0
    }

    def 'given non-existing user, when requesting getReturnValueByUser, then get returnValue'() {
        given:
        def user = new User(2, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnPercentageByUser(1) >> 100.0

        when:
        def todaySold = service.getReturnPercentageByUser(user)

        then:
        todaySold == null
    }

    def 'given existing stockName, when requesting getStockByName, then get stock'() {
        given:
        def stock = new Stock("AAPL", "Apple Inc.")
        stockRepository.getStockByName("AAPL") >> stock

        when:
        def getStock = service.getStockByName("AAPL")

        then:
        stock == getStock
    }

    def 'given non-existing stockName, when requesting getStockByName, then get stock'() {
        given:
        def stock = new Stock("AAPL", "Apple Inc.")
        stockRepository.getStockByName("AAPL") >> stock

        when:
        def getStock = service.getStockByName("GOOGL")

        then:
        getStock == null
    }
}
