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

    public void setData(Stage stage, ServiceInterface service, User user) throws IOException {
        this.service = service;
        this.stage = stage;
        this.user = user;
        profilePage(null);
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loginPage.fxml"));
        Parent parent = fxmlLoader.load();
        LoginPageController loginPageController = fxmlLoader.getController();
        loginPageController.setData(stage, service);
        Scene scene = new Scene(parent, 750, 500);
        stage.setTitle("ExchangeNow");
        stage.setScene(scene);
        stage.show();
    }

    public void profilePage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("profilePage.fxml"));
        Pane view = fxmlLoader.load();
        ProfilePageController profilePageController = fxmlLoader.getController();
        profilePageController.setData(service, user, mainBorderPane);
        mainBorderPane.setCenter(view);
    }

    public void stocksPage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("stocksPage.fxml"));
        Pane view = fxmlLoader.load();
        StocksPageController stocksPageController = fxmlLoader.getController();
        stocksPageController.setData(service, user, mainBorderPane);
        mainBorderPane.setCenter(view);
    }
}
