package com.fabio.financialtransfer.domain.repository;

import com.fabio.financialtransfer.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Account} entities.
 * Provides basic CRUD operations due to the extension of {@link JpaRepository}.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
