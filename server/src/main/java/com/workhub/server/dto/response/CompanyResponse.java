package com.workhub.server.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponse {
    private UUID id;
    private String name;
    private String address;
    private String logoUrl;
    private Long userCount;
    private List<UserResponse> users;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

