package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.util.Set;

public class StocksPageController {
    public TableView<Stock> stocksTableView;
    public TableColumn<Stock, String> nameTableColumn;
    public TableColumn<Stock, String> companyNameTableColumn;
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
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        companyNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        stocksTableView.setItems(FXCollections.observableArrayList(stocks));
    }
}
