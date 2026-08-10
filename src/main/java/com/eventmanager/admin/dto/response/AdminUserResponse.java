package com.eventmanager.admin.dto.response;

import com.eventmanager.user.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean blocked;
}