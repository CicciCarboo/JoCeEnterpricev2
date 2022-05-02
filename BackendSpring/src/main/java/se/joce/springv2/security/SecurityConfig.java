package se.joce.springv2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import se.joce.springv2.service.UserServiceImpl;

import static se.joce.springv2.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userServiceImpl;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserServiceImpl userServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/about", "/login", "/landingPage").permitAll()//TODO a to permissive permitAll() too early can block rules made after this line according to Romanian Coder episode #16?/C
                .antMatchers("/api/**","/admin/**").hasAuthority("ADMIN")//TODO .authorities() has higher authority than .hasRole(). If you want both, then put the role within like so: .authorities("user:read", "user:write", "ROLE_ADMIN") acc. to R.C episode #18
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/landingPage", true)
                .and()
                .logout();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(this.userServiceImpl);

        return daoAuthenticationProvider;
    }

//    TODO bryt ut UserDetails från inmemory
//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails CicciAdmin = User.builder()
//                .username("Cicci")
//                .password(passwordEncoder.encode("123"))
//                .roles(ADMIN.name())
////                .authorities(ADMIN.getSimpleGrantedAuthorities())
//                .build();
//
//        UserDetails LottaUser = User.builder()
//                .username("Lotta")
//                .password(passwordEncoder.encode("lotta"))
//                .roles(USER.name())
////                .authorities(USER.getSimpleGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(CicciAdmin, LottaUser);
//    }
}
