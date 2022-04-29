package se.joce.springv2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.joce.springv2.security.filter.CustomAuthenticationFilter;
import se.joce.springv2.security.filter.CustomAuthorizationFilter;

import static se.joce.springv2.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //TODO: defaultSuccessfulUrl not working!
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());


        http.csrf().disable();
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/","/about", "/login/**", "/landingPage").permitAll();
        //http.authorizeRequests().antMatchers("/api/**","/admin/**", "/myTodoList/**").hasRole("ADMIN"); //Tested hasRole(ADMIN.name())
        http.authorizeRequests().antMatchers( "/api/**","/admin/**", "/myTodoList/**").hasAnyAuthority("ROLE_ADMIN");
        //http.authorizeRequests().antMatchers("/api/**", "/myTodoList/userPage/**").hasAnyAuthority("ROLE_USER, ROLE_ADMIN");
        //http.authorizeRequests().antMatchers("/api/**", "/myTodoList/userPage/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/landingPage", true).and().logout();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);


//        http
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests().anyRequest().permitAll()
//                .and()
//                .addFilter(customAuthenticationFilter))
//                .antMatchers("/","/about", "/login", "/landingPage").permitAll()
//                .antMatchers("/api/**","/admin/**").hasRole(ADMIN.name())
//                .antMatchers("/api/**", "/myTodoList/userPage/**").hasAnyRole(USER.name(), ADMIN.name())
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/landingPage", true)
//                .and()
//                .logout();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    //    TODO: bryt ut UserDetails från inmemory
//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails CicciAdmin = User.builder()
//                .username("Cicci")
//                .password(passwordEncoder.encode("123"))
//                .roles(ADMIN.name())
//                .authorities(ADMIN.getSimpleGrantedAuthorities())
//                .build();
//
//        UserDetails LottaUser = User.builder()
//                .username("Lotta")
//                .password(passwordEncoder.encode("lotta"))
//                .roles(USER.name())
//                .authorities(USER.getSimpleGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(CicciAdmin, LottaUser);
//    }
}
