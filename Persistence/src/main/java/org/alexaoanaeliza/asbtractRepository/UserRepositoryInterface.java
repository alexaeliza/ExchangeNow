package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.User;

public interface UserRepositoryInterface extends RepositoryInterface<Long, User> {
    User getByEmail(String email);
}
