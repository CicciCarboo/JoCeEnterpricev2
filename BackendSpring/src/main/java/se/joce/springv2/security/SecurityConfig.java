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

import static se.joce.springv2.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
                .antMatchers("/","/about", "/login", "/landingPage").permitAll()//TODO permitAll() too early can block rules made after this line according to Romanian Coder/C
                .antMatchers("/api/**","/admin/**").hasRole(ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/landingPage", true)
                .and()
                .logout();
    }


//    TODO bryt ut UserDetails från inmemory
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails CicciAdmin = User.builder()
                .username("Cicci")
                .password(passwordEncoder.encode("123"))
                .roles(ADMIN.name())
//                .authorities(ADMIN.getSimpleGrantedAuthorities())
                .build();

        UserDetails LottaUser = User.builder()
                .username("Lotta")
                .password(passwordEncoder.encode("lotta"))
                .roles(USER.name())
//                .authorities(USER.getSimpleGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(CicciAdmin, LottaUser);
    }
}
