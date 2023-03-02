package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.Purchase;

import java.util.Set;

public interface PurchaseRepositoryInterface extends RepositoryInterface<Long, Purchase> {
    Set<Purchase> getPurchasesByUser(Long userId);
}
