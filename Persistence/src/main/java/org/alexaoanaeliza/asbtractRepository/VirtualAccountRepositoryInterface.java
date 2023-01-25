package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.VirtualAccount;

public interface VirtualAccountRepositoryInterface extends RepositoryInterface<Long, VirtualAccount> {
    VirtualAccount getByOwner(Long ownerId);
}
