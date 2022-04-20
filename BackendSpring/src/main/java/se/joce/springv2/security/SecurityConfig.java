package se.joce.springv2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static se.joce.springv2.security.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/about").permitAll()
                .antMatchers("/api/**","myTodoList/admin/**").hasAnyRole(ADMIN.name())
                .anyRequest().authenticated()
                .and()
//                .httpBasic();
                .formLogin().loginPage("/login").permitAll();
//                .usernameParameter("email")
//                .defaultSuccessUrl("/todo").permitAll()
//                .and()
//                .logout().logoutSuccessUrl("/").permitAll();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails CicciAdmin = User.builder()
                .username("Cicci")
                .password(passwordEncoder.encode("123"))
                .roles(ADMIN.name())
                .build();

        UserDetails LottaUser = User.builder()
                .username("Lotta")
                .password(passwordEncoder.encode("lotta"))
                .roles(UserRole.USER.name())
                .build();

        return new InMemoryUserDetailsManager(CicciAdmin, LottaUser);
    }
}
