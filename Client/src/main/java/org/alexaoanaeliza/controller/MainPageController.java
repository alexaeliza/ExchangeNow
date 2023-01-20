package org.alexaoanaeliza.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;

public class MainPageController {
    public BorderPane mainBorderPane;
    private User user;
    private Stage stage;
    private ServiceInterface service;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(ServiceInterface service) {
        this.service = service;
    }

    public void setUser(User user) throws IOException {
        this.user = user;
        profilePage(null);
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loginPage.fxml"));
        Parent parent = fxmlLoader.load();
        LoginPageController loginPageController = fxmlLoader.getController();
        loginPageController.setStage(stage);
        loginPageController.setService(service);
        Scene scene = new Scene(parent, 750, 500);
        stage.setTitle("ExchangeNow");
        stage.setScene(scene);
        stage.show();
    }

    public void profilePage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("profilePage.fxml"));
        Pane view = fxmlLoader.load();
        ProfilePageController profilePageController = fxmlLoader.getController();
        profilePageController.setService(service);
        profilePageController.setUser(user);
        profilePageController.setMainBorderPane(mainBorderPane);
        mainBorderPane.setCenter(view);
    }
}
