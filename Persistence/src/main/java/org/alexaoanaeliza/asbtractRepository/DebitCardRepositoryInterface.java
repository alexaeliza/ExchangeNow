package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.DebitCard;
import org.alexaoanaeliza.enums.DebitCardType;

import java.time.LocalDate;
import java.util.Set;

public interface DebitCardRepositoryInterface extends RepositoryInterface<Long, DebitCard> {
    DebitCard getByData(DebitCardType debitCardType, String cardNumber, String cvv, LocalDate expireDate);
    Set<DebitCard> getDebitCardsByUser(Long userId);
}
