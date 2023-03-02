package org.alexaoanaeliza.protocol.response;

public class GetTodaySoldByUserResponse implements Response {
    private final Double todaySold;

    public GetTodaySoldByUserResponse(Double returnPercentage) {
        this.todaySold = returnPercentage;
    }

    public Double getTodaySold() {
        return todaySold;
    }
}
