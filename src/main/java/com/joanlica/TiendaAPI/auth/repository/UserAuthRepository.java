package com.joanlica.TiendaAPI.auth.repository;


import com.joanlica.TiendaAPI.auth.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    boolean existsByUsername(String username);

    Optional<UserAuth> findByUsername(String username);
}