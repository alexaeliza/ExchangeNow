package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alexaoanaeliza.DebitCard;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.DebitCardType;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;

public class WithdrawAmountPageController {
    public BorderPane mainBorderPane;
    public TextField cardNumberTextField;
    public TextField cvvTextField;
    public DatePicker expireDateDatePicker;
    public TextField amountTextField;
    public Button withdrawAmount;
    public ComboBox<DebitCardType> debitCardTypeComboBox;
    private User user;
    private ServiceInterface service;

    public void setData(User user, ServiceInterface service, BorderPane mainBorderPane) {
        this.user = user;
        this.service = service;
        this.mainBorderPane = mainBorderPane;
        setData();
    }

    public void setData() {
        debitCardTypeComboBox.setItems(FXCollections.observableArrayList(DebitCardType.values()));
    }

    public void withdrawAmount(ActionEvent actionEvent) throws IOException {
        DebitCard debitCard = service.getDebitCardByData(cardNumberTextField.getText(), cvvTextField.getText(),
                expireDateDatePicker.getValue(), debitCardTypeComboBox.getValue());
        if (debitCard == null)
            debitCard = service.addDebitCard(cardNumberTextField.getText(), cvvTextField.getText(),
                    expireDateDatePicker.getValue(), debitCardTypeComboBox.getValue(), user);
        service.withdrawAmount(Double.parseDouble(amountTextField.getText()), debitCard);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("profilePage.fxml"));
        Pane view = fxmlLoader.load();
        ProfilePageController profilePageController = fxmlLoader.getController();
        profilePageController.setData(service, user, mainBorderPane);
        mainBorderPane.setCenter(view);
    }
}
