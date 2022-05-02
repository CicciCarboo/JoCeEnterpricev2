package se.joce.springv2.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.joce.springv2.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Extract list of permissions (name)
        this.user.getPermissionList().forEach(permission->{
            GrantedAuthority authority = new SimpleGrantedAuthority(permission);
            authorities.add(authority);
            System.out.println("From UserPrincipal, getAuthorities(), added authority: " + authority);
        });

        // Extract list of permissions (name) with a prettier stream :-)
//        List<GrantedAuthority> authorities = this.user.getPermissionList().stream()
//                .map(permission-> new SimpleGrantedAuthority(permission))
//                .collect(Collectors.toList());

        // Extract list of roles (ROLE_name)
        this.user.getRoleList().forEach(role-> {
            GrantedAuthority authority = new SimpleGrantedAuthority(role);
            authorities.add(authority);
            System.out.println("From UserPrincipal, getAuthorities(), added authority from roles: " + authority);
        });

        System.out.println("From UserPrincipal, getAuthorities(), the full list 'authorities': " + authorities);
            return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getActive() == 1;
    }
}
