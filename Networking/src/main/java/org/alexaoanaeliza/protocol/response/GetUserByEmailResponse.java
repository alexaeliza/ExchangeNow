package org.alexaoanaeliza.protocol.response;

import org.alexaoanaeliza.User;

public class GetUserByEmailResponse implements Response {
    private final User user;

    public GetUserByEmailResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
