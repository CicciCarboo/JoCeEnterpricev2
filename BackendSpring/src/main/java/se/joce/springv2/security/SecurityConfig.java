package se.joce.springv2.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.joce.springv2.security.filter.CustomAuthenticationFilter;
import se.joce.springv2.security.filter.CustomAuthorizationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
   // private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Value("${jwt.key}")
    private String key;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //TODO: defaultSuccessfulUrl not working!
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
//        customAuthenticationFilter.setFilterProcessesUrl("/");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //http.authorizeRequests().antMatchers("/resources/**", "/webjars/**","/assets/**").permitAll();
        http.authorizeRequests().antMatchers("/", "/login/**", "/about", "/landingPage").permitAll();
        http.authorizeRequests().antMatchers("/api/**", "/admin/**", "/myTodoList/**").permitAll();
        //http.authorizeRequests().antMatchers("/api/**","/admin/**", "/myTodoList/**").hasRole("ADMIN"); //Tested hasRole(ADMIN.name())
        http.authorizeRequests().antMatchers( "/api/**","/admin/**", "/myTodoList/**").hasAuthority("ROLE_ADMIN");
        //http.authorizeRequests().antMatchers("/api/**", "/myTodoList/userPage/**").hasAnyAuthority("ROLE_USER, ROLE_ADMIN");
        //http.authorizeRequests().antMatchers("/api/**", "/myTodoList/userPage/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        //defaultSuccessfulUrl("", true)
        http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/landingPage", true).and().logout(); //.defaultSuccessUrl("/landingPage", true)

//                .successHandler(new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                redirectStrategy.sendRedirect(request, response, "/landingPage");
//            }
//        }).permitAll().and().logout();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(key), UsernamePasswordAuthenticationFilter.class);


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
