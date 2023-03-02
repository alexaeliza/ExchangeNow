package org.alexaoanaeliza.protocol.response;

public class GetReturnValueByUserResponse implements Response {
    private final Double returnValue;

    public GetReturnValueByUserResponse(Double returnPercentage) {
        this.returnValue = returnPercentage;
    }

    public Double getReturnValue() {
        return returnValue;
    }
}
