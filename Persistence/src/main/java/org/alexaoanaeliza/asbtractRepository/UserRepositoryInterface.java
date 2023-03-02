package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.User;

public interface UserRepositoryInterface extends RepositoryInterface<Long, User> {
    User getByEmail(String email);
    User getOwnerByDebitCard(Long debitCardId);
    Double getTodaySoldByUser(Long userId);
    Double getReturnValueByUser(Long userId);
    Double getReturnPercentageByUser(Long userId);
}
