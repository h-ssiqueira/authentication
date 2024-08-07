package com.hss.authentication.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    ADMIN("admin"),
    USER("user");

    private final String userRole;
}