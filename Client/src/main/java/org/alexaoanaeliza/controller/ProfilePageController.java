package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ProfilePageController {
    public TextField portfolio;
    public TextField investment;
    public TextField returned;
    public PieChart portfolioChart;
    public Button depositAmount;
    private BorderPane mainBorderPane;
    private User user;
    private ServiceInterface service;

    public void setData(ServiceInterface service, User user, BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
        this.user = user;
        this.service = service;
        setData();
    }

    private void setData() {
        portfolio.setText("$" + user.getVirtualAccount().getTodaySold().toString());
        investment.setText("$" + user.getVirtualAccount().getInvestedAmount().toString());
        Double returnedValue = user.getVirtualAccount().getReturnValue();
        if (returnedValue < 0D)
            returned.setStyle("-fx-text-fill: red");
        else
            returned.setStyle("-fx-text-fill: green");
        returned.setText("$" + returnedValue + " (" +
                user.getVirtualAccount().getReturnPercentage() + "%)");

        Map<Stock, Double> stocks = user.getVirtualAccount().getStocks();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<Stock, Double> entry: stocks.entrySet())
            pieChartData.add(new PieChart.Data(entry.getKey().getName(), entry.getValue()));

        portfolioChart = new PieChart(pieChartData);
        portfolioChart.setTitle("Portfolio");
    }

    public void depositAmount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("transactionPage.fxml"));
        Pane view = fxmlLoader.load();
        TransactionPageController transactionPageController = fxmlLoader.getController();
        transactionPageController.setData(user, service, mainBorderPane);
        mainBorderPane.setCenter(view);
    }
}
