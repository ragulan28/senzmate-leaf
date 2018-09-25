package com.ragul.leaf.payload;

import com.ragul.leaf.model.Role;

import java.util.Set;

public class RoleResponse {
    private Set<Role> roles;

    public RoleResponse() {
    }

    public RoleResponse(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
