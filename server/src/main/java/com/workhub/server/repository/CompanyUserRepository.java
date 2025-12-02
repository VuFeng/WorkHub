package com.workhub.server.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workhub.server.entity.CompanyUser;

@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, UUID> {
    @Query("SELECT cu.user FROM CompanyUser cu WHERE cu.company.id = :companyId")
    List<com.workhub.server.entity.User> findUsersByCompanyId(@Param("companyId") UUID companyId);

    @Query("SELECT cu FROM CompanyUser cu WHERE cu.user.id = :userId")
    List<CompanyUser> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(cu) FROM CompanyUser cu WHERE cu.company.id = :companyId")
    long countByCompanyId(@Param("companyId") UUID companyId);

    void deleteByCompanyId(UUID companyId);

    boolean existsByCompanyId(UUID companyId);

    @Query("SELECT COUNT(cu) > 0 FROM CompanyUser cu WHERE cu.company.id = :companyId AND cu.user.id = :userId")
    boolean existsByCompanyIdAndUserId(@Param("companyId") UUID companyId, @Param("userId") UUID userId);
}

