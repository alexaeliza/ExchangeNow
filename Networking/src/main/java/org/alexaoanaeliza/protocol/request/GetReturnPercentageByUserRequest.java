package org.alexaoanaeliza.protocol.request;

import org.alexaoanaeliza.User;

public class GetReturnPercentageByUserRequest implements Request {
    private final User user;

    public GetReturnPercentageByUserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
