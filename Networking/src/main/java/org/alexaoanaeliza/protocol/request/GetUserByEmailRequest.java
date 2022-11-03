package org.alexaoanaeliza.protocol.request;

public class GetUserByEmailRequest implements Request {
    private final String email;

    public GetUserByEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
