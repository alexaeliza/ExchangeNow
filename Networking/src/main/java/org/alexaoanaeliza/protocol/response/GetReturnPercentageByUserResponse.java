package org.alexaoanaeliza.protocol.response;

public class GetReturnPercentageByUserResponse implements Response {
    private final Double returnPercentage;

    public GetReturnPercentageByUserResponse(Double returnPercentage) {
        this.returnPercentage = returnPercentage;
    }

    public Double getReturnPercentage() {
        return returnPercentage;
    }
}
