package org.alexaoanaeliza;

import java.io.*;

public class PredictionService {
    public PredictionService() throws IOException, InterruptedException {
        String file = "/Users/alexaoanaeliza/Desktop/ExchangeNow/PatternDetection/patternDetection.py";
        ProcessBuilder processBuilder = new ProcessBuilder("python", "-c", "import os; os.environ['PYTHONPATH'] = '/Users/alexaoanaeliza/.local/lib/python3.10/site-packages'", file);

        Process process = processBuilder.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = bufferedReader.readLine()) != null)
            System.out.println(line);


        int exitCode = process.waitFor();
        System.out.println("Python script exited with code " + exitCode);
    }
}
