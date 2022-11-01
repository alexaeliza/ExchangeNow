package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.time.LocalDate;

public class SignupPersonalDetailsPageController {
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField phoneNumberTextField;
    public TextField personalNumberTextField;
    public DatePicker birthdayDatePicker;
    private Stage stage;

    private ServiceInterface service;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(ServiceInterface service) {
        this.service = service;
    }

    public void backToLoginPage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loginPage.fxml"));
        Parent parent = fxmlLoader.load();
        LoginPageController loginPageController = fxmlLoader.getController();
        loginPageController.setStage(stage);
        Scene scene = new Scene(parent, 750, 500);
        stage.setTitle("Blood4Life");
        stage.setScene(scene);
        stage.show();
    }

    public void signupAddressDetailsPage(ActionEvent actionEvent) throws IOException {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String personalNumber = personalNumberTextField.getText();
        LocalDate birthday = birthdayDatePicker.getValue();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("signupAddressDetailsPage.fxml"));
        Parent parent = fxmlLoader.load();
        SignupAddressDetailsPageController signupAddressDetailsPageController = fxmlLoader.getController();

        signupAddressDetailsPageController.setStage(stage);
        signupAddressDetailsPageController.setService(service);
        signupAddressDetailsPageController.setFirstName(firstName);
        signupAddressDetailsPageController.setLastName(lastName);
        signupAddressDetailsPageController.setPersonalNumber(personalNumber);
        signupAddressDetailsPageController.setPhoneNumber(phoneNumber);
        signupAddressDetailsPageController.setEmail(email);
        signupAddressDetailsPageController.setBirthday(birthday);

        Scene scene = new Scene(parent, 750, 500);
        stage.setTitle("ExchangeNow");
        stage.setScene(scene);
        stage.show();
    }
}
