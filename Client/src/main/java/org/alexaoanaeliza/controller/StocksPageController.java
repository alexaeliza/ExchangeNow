package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.util.*;

public class StocksPageController {
    public ListView<TextArea> stocksListView;
    private BorderPane mainBorderPane;
    private User user;
    public ServiceInterface service;

    public void setData(ServiceInterface service, User user, BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
        this.user = user;
        this.service = service;
        setData();
    }

    private void setData() {
        Set<Stock> stocks = service.getStocks();
        List<TextArea> items = new ArrayList<>();

        stocks.forEach(stock -> {
            TextArea textArea = new TextArea(stock.getName() + "\n" + stock.getCompanyName());
            textArea.setEditable(false);
            textArea.setPrefHeight(70);
            textArea.setWrapText(true);
            items.add(textArea);
        });
        stocksListView.setItems(FXCollections.observableArrayList(items));
        stocksListView.getItems().forEach(textArea -> textArea.setOnMouseClicked(event -> {
            try {
                showStockData(textArea);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void showStockData(TextArea textArea) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("stockDataPage.fxml"));
        Pane view = fxmlLoader.load();
        StockDataPageController stockDataPageController = fxmlLoader.getController();
        stockDataPageController.setData(service, user, mainBorderPane, textArea.getText().split("\n")[0]);
        mainBorderPane.setCenter(view);
    }
}
