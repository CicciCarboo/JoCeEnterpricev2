package se.joce.springv2.security;

import static se.joce.springv2.security.UserPermission.*;
import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
// 2022-05-02 Not in use during R.C tutorial, which does it in another way.
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

    // Från Krillinator: Boiler plate code - .authorities()
    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
