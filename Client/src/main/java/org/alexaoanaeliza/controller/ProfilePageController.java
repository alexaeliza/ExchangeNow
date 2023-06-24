package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;

public class ProfilePageController {
    public TextField name;
    public TextField email;
    public TextField invested;
    public TextField available;
    public TextField address;
    public TextField personalNumber;
    public PieChart portfolioChart;
    public Button depositAmount;
    public Button withdrawAmount;
    private BorderPane mainBorderPane;
    private User user;
    private ServiceInterface service;

    public void setData(ServiceInterface service, User user, BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
        this.service = service;
        this.user = service.getUserById(user.getId());
        setData();
    }

    private void setData() {
        name.setText(user.getFirstName() + " " + user.getLastName());
        email.setText(user.getEmail());
        personalNumber.setText(user.getPersonalNumber());
        address.setText(user.getCountry() + ", " + user.getCounty() + ", " + user.getCity() + ", " + user.getStreet() + ", " + user.getApartment());
        available.setText(user.getAvailableAmount().toString());
        invested.setText(user.getInvestedAmount().toString());
    }

    public void depositAmount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("depositAmountPage.fxml"));
        Pane view = fxmlLoader.load();
        DepositAmountPageController depositAmountPageController = fxmlLoader.getController();
        depositAmountPageController.setData(user, service, mainBorderPane);
        mainBorderPane.setCenter(view);
    }

    public void withdrawAmount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("withdrawAmountPage.fxml"));
        Pane view = fxmlLoader.load();
        WithdrawAmountPageController withdrawAmountPageController = fxmlLoader.getController();
        withdrawAmountPageController.setData(user, service, mainBorderPane);
        mainBorderPane.setCenter(view);
    }
}
