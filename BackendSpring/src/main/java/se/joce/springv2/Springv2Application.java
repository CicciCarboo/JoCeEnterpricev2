package se.joce.springv2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.joce.springv2.model.User;
import se.joce.springv2.security.UserRole;
import se.joce.springv2.service.UserService;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Springv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Springv2Application.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {

        return args ->{
            userService.canRegisterNewUser(new User(null, "Cecilia","Cicci", "123",
                    "cicci@cicci.se", 1, "ADMIN", "ADMIN_READ,ADMIN_WRITE,USER_READ,USER_WRITE"));
            userService.canRegisterNewUser(new User(null, "JosefinAndersson","Josefin", "123",
                    "josefin@josefin.se", 1, "ADMIN", "ADMIN_READ,ADMIN_WRITE,USER_READ,USER_WRITE"));
            userService.canRegisterNewUser(new User(null, "LottaJansson","Lotta", "123",
                    "lotta@lotta.se", 1, "USER", "USER_READ,USER_WRITE"));

        };
    }
}
