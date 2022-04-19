package se.joce.springv2.security;

import static se.joce.springv2.security.UserPermission.*;
import com.google.common.collect.Sets;

import java.util.Set;

public enum UserRole {
    USER(Sets.newHashSet(
            USER_READ,
            USER_WRITE)),
    ADMIN(Sets.newHashSet(
            USER_READ,
            USER_WRITE,
            ADMIN_READ,
            ADMIN_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions){
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }
}
