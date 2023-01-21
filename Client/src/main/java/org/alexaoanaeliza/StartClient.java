package org.alexaoanaeliza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.alexaoanaeliza.controller.LoginPageController;
import org.alexaoanaeliza.protocol.ServiceProxy;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(StartClient.class.getResourceAsStream("/config.properties"));
        } catch (IOException ioException) {
            return;
        }

        String defaultServer = "localhost";
        String serverIP = properties.getProperty("host", defaultServer);
        int serverPort = 55555;

        try {
            serverPort = Integer.parseInt(properties.getProperty("port"));
        } catch (NumberFormatException ignored) {
        }

        ServiceInterface service = new ServiceProxy(serverIP, serverPort);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loginPage.fxml"));
        Parent parent = fxmlLoader.load();
        LoginPageController loginPageController = fxmlLoader.getController();
        loginPageController.setData(stage, service);
        Scene scene = new Scene(parent, 750, 500);
        stage.setTitle("ExchangeNow");
        stage.setScene(scene);
        stage.show();
    }
}
