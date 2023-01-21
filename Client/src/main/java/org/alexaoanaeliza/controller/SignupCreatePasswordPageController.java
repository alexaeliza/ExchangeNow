package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.enums.Country;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.time.LocalDate;

public class SignupCreatePasswordPageController {
    public PasswordField passwordPasswordField;
    public PasswordField retypePasswordPasswordField;
    public Label errorLabel;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String personalNumber;
    private LocalDate birthday;
    private Country country;
    private String county;
    private String city;
    private String street;
    private String number;
    private String apartment;
    private Stage stage;
    private ServiceInterface service;

    public void setData(Stage stage, ServiceInterface service) {
        this.stage = stage;
        this.service = service;
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

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
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

    public void loginPage(ActionEvent actionEvent) throws IOException {
        if (passwordPasswordField.getText().equals(retypePasswordPasswordField.getText())) {
            String password = passwordPasswordField.getText();
            try {
                service.addUser(firstName, lastName, email, password, phoneNumber, personalNumber,
                        birthday, country, county, city, street, number, apartment);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loginPage.fxml"));
                Parent parent = fxmlLoader.load();
                LoginPageController loginPageController = fxmlLoader.getController();
                loginPageController.setData(stage, service);
                Scene scene = new Scene(parent, 750, 500);
                stage.setTitle("ExchangeNow");
                stage.setScene(scene);
                stage.show();
            } catch (ServiceException | ServerException exception) {
                errorLabel.setText(exception.getMessage());
            }
        }
        else
            errorLabel.setText("Passwords do not match");
    }
}
