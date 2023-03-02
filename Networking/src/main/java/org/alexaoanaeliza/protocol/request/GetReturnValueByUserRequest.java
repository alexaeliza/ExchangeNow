package org.alexaoanaeliza.protocol.request;

import org.alexaoanaeliza.User;

public class GetReturnValueByUserRequest implements Request {
    private final User user;

    public GetReturnValueByUserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
