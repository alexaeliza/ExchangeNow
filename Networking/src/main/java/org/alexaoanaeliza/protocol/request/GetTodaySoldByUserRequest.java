package org.alexaoanaeliza.protocol.request;

import org.alexaoanaeliza.User;

public class GetTodaySoldByUserRequest implements Request {
    private final User user;

    public GetTodaySoldByUserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
