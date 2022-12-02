package org.alexaoanaeliza.asbtractRepository;

import org.alexaoanaeliza.Entity;

import java.util.Set;

public interface RepositoryInterface<ID, ENTITY extends Entity<ID>> {
    void resetId();
    Set<ENTITY> getAll();
    ENTITY getById(ID id);
    ENTITY add(ENTITY entity);
    void deleteAll();
}
