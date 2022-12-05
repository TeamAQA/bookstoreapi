package pl.akademiaqa.bos.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.akademiaqa.bos.user.db.UserEntityRepository;

import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(AdminConfig.class)
@AllArgsConstructor
public class GlobalSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserEntityRepository userEntityRepository;
    private final AdminConfig adminConfig;

    @Bean
    User systemAdmin() {
        return new User(
                "system_admin",
                "",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/books/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/authors").permitAll()
                .mvcMatchers(HttpMethod.PUT, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.PATCH, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/orders").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        GlobalUserDetailsService detailsService = new GlobalUserDetailsService(userEntityRepository, adminConfig);
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
