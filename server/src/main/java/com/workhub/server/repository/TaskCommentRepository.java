package com.workhub.server.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.workhub.server.entity.TaskComment;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, UUID> {
    
    @Query("SELECT tc FROM TaskComment tc WHERE tc.task.id = :taskId ORDER BY tc.createdAt DESC")
    Page<TaskComment> findByTaskId(@Param("taskId") UUID taskId, Pageable pageable);
    
    @Query("SELECT tc FROM TaskComment tc WHERE tc.user.id = :userId ORDER BY tc.createdAt DESC")
    Page<TaskComment> findByUserId(@Param("userId") UUID userId, Pageable pageable);
    
    @Query("SELECT COUNT(tc) FROM TaskComment tc WHERE tc.task.id = :taskId")
    long countByTaskId(@Param("taskId") UUID taskId);
}

