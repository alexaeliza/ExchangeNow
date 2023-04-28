package org.alexaoanaeliza.controller;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import org.alexaoanaeliza.service.ServiceInterface;

import java.time.LocalDate;
import java.util.Map;

public class StockPredictionsPageController {
    public LineChart<String, Number> predictionsChart;
    public NumberAxis priceAxis;
    private BorderPane mainBorderPane;
    private ServiceInterface service;
    private String stockId;

    public void setData(ServiceInterface service, BorderPane mainBorderPane, String stockId) {
        this.mainBorderPane = mainBorderPane;
        this.service = service;
        this.stockId = stockId;
        setData();
    }

    private void setData() {
        Map<LocalDate, Double> predictions = service.getStockPredictions(stockId, LocalDate.now().minusYears(8), LocalDate.now().plusYears(2));

        System.out.println(stockId);
        predictionsChart.getData().removeAll();
        predictionsChart.setTitle(stockId);
        predictionsChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        predictions.forEach((date, price) -> series.getData().add(new XYChart.Data<>(date.toString(), price)));
        predictionsChart.getData().add(series);

        priceAxis.setAutoRanging(true);
        priceAxis.setForceZeroInRange(false);
        predictionsChart.autosize();
    }
}
