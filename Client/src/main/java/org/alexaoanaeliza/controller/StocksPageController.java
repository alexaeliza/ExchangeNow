package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
            textArea.setPrefHeight(50);
            textArea.setPrefWidth(600);
            textArea.setWrapText(true);
            items.add(textArea);
        });
        stocksListView.setItems(FXCollections.observableArrayList(items));
    }
}
