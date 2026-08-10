package com.eventmanager.user.mapper;

import com.eventmanager.user.dto.response.UserResponse;
import com.eventmanager.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstname());
        response.setLastName(user.getLastname());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}