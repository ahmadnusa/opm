package com.dansmultipro.ops.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dansmultipro.ops.model.master.StatusType;

@Repository
public interface StatusTypeRepo extends JpaRepository<StatusType, UUID> {

    Optional<StatusType> findByCode(String code);
}
