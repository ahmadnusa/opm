package com.dansmultipro.ops.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dansmultipro.ops.model.master.PaymentType;

@Repository
public interface PaymentTypeRepo extends JpaRepository<PaymentType, UUID> {

    Optional<PaymentType> findByCode(String code);
}
