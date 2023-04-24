package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.User;

public class GetUserByIdResponse implements Response {
    private final User user;

    public GetUserByIdResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
