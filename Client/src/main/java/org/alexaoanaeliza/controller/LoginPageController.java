package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.exception.ServerException;
import org.alexaoanaeliza.exception.ServiceException;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;

public class LoginPageController {
    public TextField emailTextField;
    public PasswordField passwordPasswordField;
    public Label errorLabel;
    private Stage stage;
    private ServiceInterface service;

    public void setData(Stage stage, ServiceInterface service) {
        this.stage = stage;
        this.service = service;
    }

    public void signUpButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("signupPersonalDetailsPage.fxml"));
        Parent parent = fxmlLoader.load();
        SignupPersonalDetailsPageController signupPersonalDetailsPageController = fxmlLoader.getController();
        signupPersonalDetailsPageController.setData(stage, service);
        Scene scene = new Scene(parent, 750, 500);
        stage.setTitle("ExchangeNow");
        stage.setScene(scene);
        stage.show();
    }

    public void loginButton(ActionEvent actionEvent) throws IOException {
        try {
            String email = emailTextField.getText();
            String password = passwordPasswordField.getText();
            if (email.isBlank() || password.isBlank())
                errorLabel.setText("Fields must be completed");
            else {
                User user = service.loginUser(email, password);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainPage.fxml"));
                Parent parent = fxmlLoader.load();
                MainPageController mainPageController = fxmlLoader.getController();
                mainPageController.setData(stage, service, user);
                Scene scene = new Scene(parent, 750, 500);
                stage.setTitle("Blood4Life");
                stage.setScene(scene);
                stage.show();
            }
        } catch (ServiceException | ServerException exception) {
            errorLabel.setText(exception.getMessage());
        }
    }
}
