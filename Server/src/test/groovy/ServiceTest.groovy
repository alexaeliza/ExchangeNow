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
import org.alexaoanaeliza.exception.DatabaseException
import org.alexaoanaeliza.exception.ServiceException
import org.alexaoanaeliza.server.Service
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class ServiceTest extends Specification {
    def userRepository = Mock(UserRepositoryInterface)
    def debitCardRepository = Mock(DebitCardRepositoryInterface)
    def stockRepository = Mock(StockRepositoryInterface)
    def saleRepository = Mock(SaleRepositoryInterface)
    def purchaseRepository = Mock(PurchaseRepositoryInterface)
    def service = new Service(userRepository, debitCardRepository, stockRepository, saleRepository, purchaseRepository)

    def 'when requesting getInstance, then get service instance'() {
        when:
        def service = Service.getInstance()

        then:
        service != null
    }

    def 'given error in UserRepository, when requesting loginUser, then throw error'() {
        given:
        userRepository.getByEmail("email") >> { throw new DatabaseException("Database exception") }

        when:
        service.loginUser("email", "password")

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

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

    def 'given error in UserRepository.getByEmail, when requesting getUserByEmail, then throw error'() {
        given:
        userRepository.getByEmail("email") >> { throw new DatabaseException("Database exception") }

        when:
        service.getUserByEmail("email")

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
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

    def 'given error in DebitCardRepository, when requesting getDebitCardById, then throw error'() {
        given:
        debitCardRepository.getById(1) >> { throw new DatabaseException("Database exception") }

        when:
        service.getDebitCardById(1)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
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

    def 'given error in DebitCardRepository, when requesting getDebitCardByData, then throw error'() {
        given:
        debitCardRepository.getByData(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now()) >> { throw new DatabaseException("Database exception") }

        when:
        service.getDebitCardByData("cardNumber", "cvv", LocalDate.now(), DebitCardType.VISA)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
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

    def 'given error in StockRepository, when requesting getStocks, then throw error'() {
        given:
        stockRepository.getAll() >> { throw new DatabaseException("Database exception") }

        when:
        service.getStocks()

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
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

    def 'given error in UserRepository, when requesting getTodaySoldByUser, then throw error'() {
        given:
        def user = new User(1, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getTodaySoldByUser(1) >> { throw new DatabaseException("Database exception") }

        when:
        service.getTodaySoldByUser(user)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
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

    def 'given non-existing user, when requesting getTodaySoldByUser, then get null'() {
        given:
        def user = new User(2, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getTodaySoldByUser(1) >> 100.0

        when:
        def todaySold = service.getTodaySoldByUser(user)

        then:
        todaySold == null
    }

    def 'given error in UserRepository, when requesting getReturnValueByUser, then throw error'() {
        given:
        def user = new User(1, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnValueByUser(1) >> { throw new DatabaseException("Database exception") }

        when:
        service.getReturnValueByUser(user)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
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

    def 'given non-existing user, when requesting getReturnValueByUser, then get null'() {
        given:
        def user = new User(2, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnValueByUser(1) >> 100.0

        when:
        def todaySold = service.getReturnValueByUser(user)

        then:
        todaySold == null
    }

    def 'given error in UserRepository, when requesting getReturnPercentageByUser, then throw error'() {
        given:
        def user = new User(1, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnPercentageByUser(1) >> { throw new DatabaseException("Database exception") }

        when:
        service.getReturnPercentageByUser(user)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given existing user, when requesting getReturnPercentageByUser, then get returnValue'() {
        given:
        def user = new User(1, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnPercentageByUser(1) >> 100.0

        when:
        def todaySold = service.getReturnPercentageByUser(user)

        then:
        todaySold == 100.0
    }

    def 'given non-existing user, when requesting getReturnPercentageByUser, then get null'() {
        given:
        def user = new User(2, "firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        userRepository.getReturnPercentageByUser(1) >> 100.0

        when:
        def todaySold = service.getReturnPercentageByUser(user)

        then:
        todaySold == null
    }

    def 'given error in StockRepository, when requesting getStockByName, then throw error'() {
        given:
        stockRepository.getStockByName("name") >> { throw new DatabaseException("Database exception") }

        when:
        service.getStockByName("name")

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
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

    def 'given non-existing stockName, when requesting getStockByName, then get null'() {
        given:
        def stock = new Stock("AAPL", "Apple Inc.")
        stockRepository.getStockByName("AAPL") >> stock

        when:
        def getStock = service.getStockByName("GOOGL")

        then:
        getStock == null
    }

    def 'given error in UserRepository, when requesting addUser, then throw error'() {
        given:
        userRepository.add(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.addUser("firstName", "lastName", "email", "password", "phoneNumber", "personalNumber", LocalDate.now(), Country.ALBANIA, "county", "city", "street", "number", "apartment")

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given error in DebitCardRepository, when requesting addDebitCard, then throw error'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        debitCardRepository.add(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.addDebitCard("cardNumber", "cvv", LocalDate.now(), DebitCardType.VISA, user)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given error in UserRepository, when requesting depositAmount, then throw error'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        userRepository.getById(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.depositAmount(12, debitCard)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given error in UserRepository, when requesting withdrawAmount, then throw error'() {
        given:
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        userRepository.getById(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.withdrawAmount(12, debitCard)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given error in UserRepository, when requesting depositAmount, then throw error'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        userRepository.getById(_) >> user
        userRepository.update(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.depositAmount(12, debitCard)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given error in UserRepository, when requesting withdrawAmount, then throw error'() {
        given:
        def user = new User("firstName", "lastName", "personalNumber", "phoneNumber", LocalDate.now(), "email", "password", Country.ALBANIA, "county", "city", "street", "number", "apartment")
        def debitCard = new DebitCard(DebitCardType.VISA, "cardNumber", "cvv", LocalDate.now(), 1)
        userRepository.getById(_) >> user
        userRepository.update(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.withdrawAmount(12, debitCard)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given error in SaleRepository, when requesting sellStock, then throw error'() {
        given:
        saleRepository.add(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.sellStock(1, 1, LocalDateTime.now(), 12)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }

    def 'given error in PurchaseRepository, when requesting buyStock, then throw error'() {
        given:
        purchaseRepository.add(_) >> { throw new DatabaseException("Database exception") }

        when:
        service.buyStock(1, 1, LocalDateTime.now(), 12)

        then:
        def error = thrown(ServiceException)
        error.message == "Database exception"
    }
}
