package org.alexaoanaeliza.protocol.response;

import java.time.LocalDate;
import java.util.Map;

public class GetStockPredictionsResponse implements Response {
    private final Map<LocalDate, Double> predictions;

    public GetStockPredictionsResponse(Map<LocalDate, Double> predictions) {
        this.predictions = predictions;
    }

    public Map<LocalDate, Double> getPredictions() {
        return predictions;
    }
}
