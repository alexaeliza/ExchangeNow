package org.alexaoanaeliza.protocol.request;

public class GetPortfolioByUserRequest implements Request {
    private final Long userId;

    public GetPortfolioByUserRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
