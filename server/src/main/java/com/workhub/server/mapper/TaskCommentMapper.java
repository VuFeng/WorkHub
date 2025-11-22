package com.workhub.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.workhub.server.dto.request.TaskCommentRequest;
import com.workhub.server.dto.response.TaskCommentResponse;
import com.workhub.server.entity.TaskComment;

@Mapper(componentModel = "spring")
public interface TaskCommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TaskComment toEntity(TaskCommentRequest request);

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.title", target = "taskTitle")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userName")
    TaskCommentResponse toResponse(TaskComment entity);
}

