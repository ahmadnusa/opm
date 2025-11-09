package com.dansmultipro.ops.repository;

import com.dansmultipro.ops.model.master.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {

    Optional<Role> findByCode(String code);
}
