package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.Sale;

import java.util.Set;

public interface SaleRepositoryInterface extends RepositoryInterface<Long, Sale> {
    Set<Sale> getSalesByUser(Long userId);
}
