package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SignupAddressDetailsPageController {
    public ComboBox<Country> countryComboBox;
    public TextField countyTextField;
    public TextField cityTextField;
    public TextField streetTextField;
    public TextField numberTextField;
    public TextField apartmentTextField;
    public Label errorLabel;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String personalNumber;
    private LocalDate birthday;
    private Stage stage;
    private ServiceInterface service;

    public void setData(Stage stage, ServiceInterface service) {
        this.stage = stage;
        this.service = service;
        setData();
    }

    private void setData() {
        countryComboBox.setItems(FXCollections.observableArrayList(Country.values()));
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    public void signupCreatePasswordPage(ActionEvent actionEvent) throws IOException {
        Country country = countryComboBox.getValue();
        String county = countyTextField.getText();
        String city = cityTextField.getText();
        String street = streetTextField.getText();
        String number = numberTextField.getText();
        String apartment = apartmentTextField.getText();

        if (country == null || county.isBlank() || city.isBlank() || street.isBlank() || number.isBlank())
            errorLabel.setText("Fields must be completed");
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("signupCreatePasswordPage.fxml"));
            Parent parent = fxmlLoader.load();
            SignupCreatePasswordPageController signupCreatePasswordPageController = fxmlLoader.getController();

            signupCreatePasswordPageController.setData(stage, service);
            signupCreatePasswordPageController.setFirstName(firstName);
            signupCreatePasswordPageController.setLastName(lastName);
            signupCreatePasswordPageController.setPersonalNumber(personalNumber);
            signupCreatePasswordPageController.setPhoneNumber(phoneNumber);
            signupCreatePasswordPageController.setEmail(email);
            signupCreatePasswordPageController.setBirthday(birthday);
            signupCreatePasswordPageController.setCounty(county);
            signupCreatePasswordPageController.setCountry(country);
            if (!apartment.isBlank())
                signupCreatePasswordPageController.setApartment(apartment);
            else signupCreatePasswordPageController.setApartment(null);
            signupCreatePasswordPageController.setCity(city);
            signupCreatePasswordPageController.setStreet(street);
            signupCreatePasswordPageController.setNumber(number);

            Scene scene = new Scene(parent, 750, 500);
            stage.setTitle("Blood4Life");
            stage.setScene(scene);
            stage.show();
        }
    }
}
