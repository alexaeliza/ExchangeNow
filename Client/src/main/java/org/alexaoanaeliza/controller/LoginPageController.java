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

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setService(ServiceInterface service) {
        this.service = service;
    }

    public void signUpButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("signupPersonalDetailsPage.fxml"));
        Parent parent = fxmlLoader.load();
        SignupPersonalDetailsPageController signupPersonalDetailsPageController = fxmlLoader.getController();
        signupPersonalDetailsPageController.setStage(stage);
        signupPersonalDetailsPageController.setService(service);
        Scene scene = new Scene(parent, 750, 500);
        stage.setTitle("Blood4Life");
        stage.setScene(scene);
        stage.show();
    }

    public void loginButton(ActionEvent actionEvent) {
        try {
            String email = emailTextField.getText();
            String password = passwordPasswordField.getText();
            if (email.isBlank() || password.isBlank())
                errorLabel.setText("Fields must be completed");
            else
                service.loginUser(email, password);
        } catch (ServiceException | ServerException exception) {
            errorLabel.setText(exception.getMessage());
        }
    }
}
