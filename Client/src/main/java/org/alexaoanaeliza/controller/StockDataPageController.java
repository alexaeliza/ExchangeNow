package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.TransactionType;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StockDataPageController {
    public LineChart<String, Number> stockChart;
    public Button buyStockButton;
    public Button sellStockButton;
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
        stockChart.getData().removeAll();
        Map<LocalDate, Double> stockData = service.getStockData(stockId);
        stockChart.setTitle(stockId);
        stockChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        stockData.forEach((date, price) -> series.getData().add(new XYChart.Data<>(date.toString(), price)));
        stockChart.getData().add(series);
    }

    public void buyStock(ActionEvent actionEvent) throws IOException {
        Stock stock = service.getStockByName(stockId);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("transactionPage.fxml"));
        Pane view = fxmlLoader.load();
        TransactionPageController transactionPageController = fxmlLoader.getController();
        transactionPageController.setData(service, user, mainBorderPane, stock, TransactionType.BUY);
        mainBorderPane.setCenter(view);
    }

    public void sellStock(ActionEvent actionEvent) throws IOException {
        Stock stock = service.getStockByName(stockId);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("transactionPage.fxml"));
        Pane view = fxmlLoader.load();
        TransactionPageController transactionPageController = fxmlLoader.getController();
        transactionPageController.setData(service, user, mainBorderPane, stock, TransactionType.SELL);
        mainBorderPane.setCenter(view);
    }
}
