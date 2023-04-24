package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.Stock;
import org.alexaoanaeliza.User;

import java.util.Map;

public interface UserRepositoryInterface extends RepositoryInterface<Long, User> {
    User getByEmail(String email);
    User getOwnerByDebitCard(Long debitCardId);
    Double getTodaySoldByUser(Long userId);
    Double getReturnValueByUser(Long userId);
    Double getReturnPercentageByUser(Long userId);
}
