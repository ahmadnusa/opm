package com.dansmultipro.ops.repository;

import com.dansmultipro.ops.model.master.ProductType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepo extends JpaRepository<ProductType, UUID> {

    Optional<ProductType> findByCode(String code);
}
