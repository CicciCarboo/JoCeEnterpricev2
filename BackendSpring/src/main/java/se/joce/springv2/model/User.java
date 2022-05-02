package se.joce.springv2.model;

import lombok.*;
import se.joce.springv2.security.UserRole;

import javax.persistence.*;
//
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_email", columnNames = { "email" })})
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

//    public User(String name, String username, String password, String email, int active, String roles, String permissions) {
//    }


//    Not needed anymore?
//    @Column (name = "user_role")
//    @Enumerated(EnumType.STRING)
//    private UserRole userRole;
}
