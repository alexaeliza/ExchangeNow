package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.User;

public class LoginUserResponse extends OkResponse {
    private final User user;

    public LoginUserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
