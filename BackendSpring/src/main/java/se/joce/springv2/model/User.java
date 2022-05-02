package se.joce.springv2.model;

import lombok.*;
import se.joce.springv2.security.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_email", columnNames = {"email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    private int active;

    private String roles = ""; //TODO to be transformed to sets or lists?

    private String permissions = ""; //TODO to be transformed to sets or lists?

//    Not needed anymore?
//    @Column (name = "user_role")
//    @Enumerated(EnumType.STRING)
//    private UserRole userRole;

    // Helper method to enable mapping between User and UserPrincipal (UserDetails interface)
    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    // Helper method to enable mapping between User and UserPrincipal (UserDetails interface)
    public List<String> getPermissionList() {
        if (this.permissions.length() > 0) {
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }
}
