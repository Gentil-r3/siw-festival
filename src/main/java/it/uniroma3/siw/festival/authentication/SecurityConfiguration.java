package it.uniroma3.siw.festival.authentication;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static it.uniroma3.siw.festival.model.Credentials.ADMIN_ROLE;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // dove sono le credenziali: tabella credentials (username, password, ruolo)
    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
            "SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, role FROM credentials WHERE username=?");
        return manager;
    }

    // algoritmo per cifrare le password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {

        // blocco 1: chi può accedere a cosa (la prima regola che corrisponde vince)
        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/login",
                    "/festivals", "/festivals/**", "/movies", "/movies/**",
                    "/api/**", // le API REST in lettura sono pubbliche (le usa React)
                    "/css/**", "/images/**", "/favicon.ico", "/error").permitAll();
            authorize.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll();
            authorize.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE);
            authorize.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE);
            authorize.anyRequest().authenticated();
        });

        // blocco 2: la form di login
        httpSecurity.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/", true);
            form.failureUrl("/login?error=true");
        });

        // blocco 3: il logout
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/");
            logout.invalidateHttpSession(true);
            logout.deleteCookies("JSESSIONID");
            logout.clearAuthentication(true);
            logout.permitAll();
        });

        return httpSecurity.build();
    }
}
