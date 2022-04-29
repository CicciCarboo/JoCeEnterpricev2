package se.joce.springv2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import se.joce.springv2.model.Role;
import se.joce.springv2.model.User;
import se.joce.springv2.security.UserRole;
import se.joce.springv2.service.UserService;

import java.util.ArrayList;

@SpringBootApplication
public class Springv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Springv2Application.class, args);
    }

    //TODO: add permissions somewhere?
    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_ADMIN")); //Tried to add UserRole.ADMIN here via Role...
            userService.saveRole(new Role(null, "ROLE_USER"));

            userService.saveUser(new User(null, "Cicci", "123", "cicci123@gmail.com", UserRole.ADMIN, new ArrayList<>())); //Tried to add UserRole.ADMIN here via User...
            userService.saveUser(new User(null, "Lotta", "123", "lotta123@gmail.com", UserRole.USER, new ArrayList<>()));

            userService.addRoleToUser("Cicci", "ROLE_ADMIN");
            userService.addRoleToUser("Lotta", "ROLE_USER");
        };
    }

}
