package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.alexaoanaeliza.PredictionService;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

        try {
            PredictionService predictionService = new PredictionService();
            predictionService.read().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Exception Raised" + e);
        }

        stocks.forEach(stock -> {
            TextArea textArea = new TextArea(stock.getName() + "\n" + stock.getCompanyName());
            textArea.setEditable(false);
            textArea.setPrefHeight(70);
            textArea.setPrefWidth(600);
            textArea.setWrapText(true);
            items.add(textArea);
        });
        stocksListView.setItems(FXCollections.observableArrayList(items));
    }
}
