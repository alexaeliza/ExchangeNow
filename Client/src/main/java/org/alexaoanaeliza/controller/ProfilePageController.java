package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.io.IOException;
import java.util.Map;

public class ProfilePageController {
    public TextField portfolio;
    public TextField investment;
    public TextField returned;
    public PieChart portfolioChart;
    public Button depositAmount;
    public Button withdrawAmount;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("depositAmountPage.fxml"));
        Pane view = fxmlLoader.load();
        DepositAmountPageController depositAmountPageController = fxmlLoader.getController();
        depositAmountPageController.setData(user, service, mainBorderPane);
        mainBorderPane.setCenter(view);
    }

    public void withdrawAmount(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("withdrawAmountPage.fxml"));
        Pane view = fxmlLoader.load();
        WithdrawAmountPageController withdrawAmountPageController = fxmlLoader.getController();
        withdrawAmountPageController.setData(user, service, mainBorderPane);
        mainBorderPane.setCenter(view);
    }
}
