package org.alexaoanaeliza.controller;

import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.time.LocalDate;
import java.util.Map;

public class StockDataPageController {
    public LineChart<String, Number> stockChart;
    private BorderPane mainBorderPane;
    private User user;
    private ServiceInterface service;
    private String stockId;

    public void setData(ServiceInterface service, User user, BorderPane mainBorderPane, String stockId) {
        this.mainBorderPane = mainBorderPane;
        this.user = user;
        this.service = service;
        this.stockId = stockId;
        setData();
    }

    private void setData() {
        Map<LocalDate, Double> stockData = service.getStockData(stockId);
        stockChart.setTitle(stockId);
        stockChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        stockData.forEach((date, price) -> series.getData().add(new XYChart.Data<>(date.toString(), price)));
        stockChart.getData().add(series);
    }
}
