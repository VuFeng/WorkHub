package com.workhub.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "isActive", target = "isActive")
    User toEntity(UserRequest request);

    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "companyName", ignore = true)
    UserResponse toResponse(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget User user);
}
