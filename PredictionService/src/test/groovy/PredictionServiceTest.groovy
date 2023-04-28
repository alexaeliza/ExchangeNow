import org.alexaoanaeliza.PredictionService
import spock.lang.Specification

import java.time.LocalDate

class PredictionServiceTest extends Specification {
    def predictionService = new PredictionService('AAMC')

    def 'given no starting date, when requesting getStockData, then get non-empty stock data'() {
        when:
        def stockData = predictionService.getStockData()

        then:
        stockData.size() != 0
    }

    def 'given starting date, when requesting getStockData, then get non-empty stock data'() {
        given:
        def date = LocalDate.of(2023, 4, 10)

        when:
        def stockData = predictionService.getStockData(date)

        then:
        stockData.size() != 0
        stockData.get(date) != null
    }

    def 'given yesterday starting date, when requesting getStockData, then get one element'() {
        given:
        def date = LocalDate.now().minusDays(1)

        when:
        def stockData = predictionService.getStockData(date)

        then:
        stockData.size() == 1
        stockData.get(date) != null
    }
}
