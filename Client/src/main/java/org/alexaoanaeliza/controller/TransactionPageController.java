package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.TransactionType;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.protocol.response.Response;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.time.LocalDateTime;

public class TransactionPageController {
    public Label transactionLabel;
    public TextField sumTextField;
    public Button transactionButton;
    public Label errorLabel;
    private BorderPane mainBorderPane;
    private User user;
    public ServiceInterface service;
    private Stock stock;
    private TransactionType transactionType;

    public void setData(ServiceInterface service, User user, BorderPane mainBorderPane, Stock stock, TransactionType transactionType) {
        this.mainBorderPane = mainBorderPane;
        this.user = user;
        this.service = service;
        this.transactionType = transactionType;
        this.stock = stock;
        setData();
    }

    private void setData() {
        transactionLabel.setText(transactionType + " " + stock.getName());
    }

    public void makeTransaction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("stockDataPage.fxml"));
        try {
            if (transactionType.equals(TransactionType.BUY))
                service.buyStock(user.getId(), stock.getId(), LocalDateTime.now(), Double.parseDouble(sumTextField.getText()));
            else
                service.sellStock(user.getId(), stock.getId(), LocalDateTime.now(), Double.parseDouble(sumTextField.getText()));

            Pane view = fxmlLoader.load();
            StockDataPageController stockDataPageController = fxmlLoader.getController();
            stockDataPageController.setData(service, user, mainBorderPane, stock.getName());
            mainBorderPane.setCenter(view);
        } catch (ServerException serverException) {
            errorLabel.setText(serverException.getMessage());
        }
    }
}
