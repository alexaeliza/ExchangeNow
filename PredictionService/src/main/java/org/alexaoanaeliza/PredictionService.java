package org.alexaoanaeliza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PredictionService {
    private final BufferedReader bufferedReader;

    public PredictionService() throws IOException {
        String file = "/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/patternDetection.py";
        Process process = Runtime.getRuntime().exec("python " + file);
        bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    public List<String> read() throws IOException {
        String line;
        List<String> result = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null)
            result.add(line);
        return result;
    }
}
