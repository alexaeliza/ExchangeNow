package org.alexaoanaeliza.protocol.request;

public class GetUserByIdRequest implements Request {
    private final Long userId;

    public GetUserByIdRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
