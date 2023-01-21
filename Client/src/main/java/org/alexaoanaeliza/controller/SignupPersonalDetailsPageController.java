package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    public Label errorLabel;
    private Stage stage;

    private ServiceInterface service;

    public void setData(Stage stage, ServiceInterface service) {
        this.stage = stage;
        this.service = service;
    }

    public void backToLoginPage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loginPage.fxml"));
        Parent parent = fxmlLoader.load();
        LoginPageController loginPageController = fxmlLoader.getController();
        loginPageController.setData(stage, service);
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

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || phoneNumber.isBlank() ||
                personalNumber.isBlank() || birthday == null)
            errorLabel.setText("Fields must be completed");
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("signupAddressDetailsPage.fxml"));
            Parent parent = fxmlLoader.load();
            SignupAddressDetailsPageController signupAddressDetailsPageController = fxmlLoader.getController();

            signupAddressDetailsPageController.setData(stage, service);
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
}
