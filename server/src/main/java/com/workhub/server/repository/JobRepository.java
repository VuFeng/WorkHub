package com.workhub.server.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workhub.server.constant.JobStatus;
import com.workhub.server.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    
    @Query("SELECT j FROM Job j WHERE j.company.id = :companyId")
    Page<Job> findByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.owner.id = :ownerId")
    Page<Job> findByOwnerId(@Param("ownerId") UUID ownerId, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = :status")
    Page<Job> findByStatus(@Param("status") JobStatus status, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.company.id = :companyId AND j.status = :status")
    Page<Job> findByCompanyIdAndStatus(@Param("companyId") UUID companyId, 
                                        @Param("status") JobStatus status, 
                                        Pageable pageable);
    
    @Query("SELECT COUNT(j) FROM Job j WHERE j.company.id = :companyId")
    long countByCompanyId(@Param("companyId") UUID companyId);
}


