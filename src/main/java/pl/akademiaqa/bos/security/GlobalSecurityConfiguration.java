package pl.akademiaqa.bos.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.akademiaqa.bos.security.filters.CustomAuthenticationFilter;
import pl.akademiaqa.bos.security.filters.CustomAuthorizationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(AdminConfig.class)
@AllArgsConstructor
public class GlobalSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/books/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/authors").permitAll()
                .mvcMatchers(HttpMethod.PUT, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.PATCH, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/authors/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/orders").permitAll();

        http
                .addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
