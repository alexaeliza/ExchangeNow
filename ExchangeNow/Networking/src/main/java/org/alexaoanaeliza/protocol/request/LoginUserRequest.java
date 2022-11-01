package org.alexaoanaeliza.protocol.request;

import java.util.List;

public class LoginUserRequest implements Request {
    private final List<String> usersLogin;

    public LoginUserRequest(List<String> usersLogin){ this.usersLogin = usersLogin; }

    public List<String> getUsersLogin() {
        return usersLogin;
    }
}
