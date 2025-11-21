package com.workhub.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User entity);

}
