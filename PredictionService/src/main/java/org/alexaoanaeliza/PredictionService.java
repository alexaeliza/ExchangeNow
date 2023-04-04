package org.alexaoanaeliza;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PredictionService {
    private final String stockId;

    public PredictionService(String stockId) {
        this.stockId = stockId;
    }

    private void startProcess(String stockId) throws IOException, InterruptedException {
        String script = "/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/downloadStockWithoutStart.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python", script, stockId);
        Process process = processBuilder.start();
        process.waitFor();
    }

    private void startProcess(String stockId, LocalDate date) throws IOException, InterruptedException {
        String script = "/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/downloadStockWithStart.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python", script, stockId, date.toString());
        Process process = processBuilder.start();
        process.waitFor();
    }

    private Map<LocalDate, Double> readStockData() throws IOException {
        String filename = "/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/stockData.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        String line;
        Map<LocalDate, Double> stockData = new HashMap<>();

        while ((line = bufferedReader.readLine()) != null) {
            String[] characters = line.strip().split(" ");
            stockData.put(LocalDate.parse(characters[0]), Double.valueOf(characters[1]));
        }

        return stockData;
    }

    private Map<LocalDate, Double> sortStockData(Map<LocalDate, Double> stockData) {
        return stockData
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(LinkedHashMap::new, (map1, entry) -> map1.put(entry.getKey(), entry.getValue()), Map::putAll);
    }

    public Map<LocalDate, Double> getStockData() throws IOException, InterruptedException {
        startProcess(stockId);
        return sortStockData(readStockData());
    }

    public Map<LocalDate, Double> getStockData(LocalDate date) throws IOException, InterruptedException {
        startProcess(stockId, date);
        return sortStockData(readStockData());
    }
}
