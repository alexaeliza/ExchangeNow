package org.alexaoanaeliza.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;
import org.alexaoanaeliza.service.ServiceInterface;

import java.util.Map;

public class ProfilePageController {
    public TextField portfolio;
    public TextField investment;
    public TextField returned;
    public PieChart portfolioChart;
    private User user;
    private ServiceInterface service;

    public void setService(ServiceInterface service) {
        this.service = service;
    }
    public void setUser(User user) {
        this.user = user;
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
}
