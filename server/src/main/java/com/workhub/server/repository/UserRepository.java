package com.workhub.server.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workhub.server.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.company.id = :companyId")
    Page<User> findByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.company.id = :companyId")
    long countByCompanyId(@Param("companyId") UUID companyId);
}
