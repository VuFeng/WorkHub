package com.workhub.server.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workhub.server.constant.TaskStatus;
import com.workhub.server.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    
    @Query("SELECT t FROM Task t WHERE t.company.id = :companyId")
    Page<Task> findByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.job.id = :jobId")
    Page<Task> findByJobId(@Param("jobId") UUID jobId, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.assignee.id = :assigneeId")
    Page<Task> findByAssigneeId(@Param("assigneeId") UUID assigneeId, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.status = :status")
    Page<Task> findByStatus(@Param("status") TaskStatus status, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.job.id = :jobId AND t.status = :status")
    Page<Task> findByJobIdAndStatus(@Param("jobId") UUID jobId, 
                                     @Param("status") TaskStatus status, 
                                     Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.assignee.id = :assigneeId AND t.status = :status")
    Page<Task> findByAssigneeIdAndStatus(@Param("assigneeId") UUID assigneeId, 
                                          @Param("status") TaskStatus status, 
                                          Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.job.id = :jobId")
    long countByJobId(@Param("jobId") UUID jobId);
}

