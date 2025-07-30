package com.starkIndustries.RoleBasedAuthorization.auth.security.configuration;

import com.starkIndustries.RoleBasedAuthorization.auth.filter.JwtAuthenticationFilter;
import com.starkIndustries.RoleBasedAuthorization.auth.permissions.Permissions;
import com.starkIndustries.RoleBasedAuthorization.auth.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    public UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder getBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(csrf->csrf.disable())
                .cors(cors->cors.disable())
                .httpBasic(Customizer.withDefaults())
                .formLogin(formLogin->formLogin.disable())
                .authenticationProvider(getAuthenticationProvider())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request->request.requestMatchers("/employee/login/**","/employee/add-employee/**")
                        .permitAll()

                        // /manager/** is available for both Admin and Manager, the below allows for the url for both Admin and Manager
                        .requestMatchers("/manager/**").hasAnyRole(Role.ADMIN.name(),Role.MANAGER.name())

                        // The below url with HttpMethods allow both Admin and Manager, for various permission
                        .requestMatchers(HttpMethod.GET,"/manager/get-manager").hasAnyAuthority(Permissions.ADMIN_READ.name(),Permissions.MANAGER_READ.name())
                        .requestMatchers(HttpMethod.POST,"/manager/post-manager").hasAnyAuthority(Permissions.ADMIN_CREATE.name(),Permissions.MANAGER_CREATE.name())
                        .requestMatchers(HttpMethod.PUT,"/manager/update-manager").hasAnyAuthority(Permissions.ADMIN_UPDATE.name(),Permissions.MANAGER_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE,"/manager/delete-manager").hasAnyAuthority(Permissions.ADMIN_DELETE.name(),Permissions.MANAGER_DELETE.name())

                        // /admin/** is available for both Admin, the below allows for the url for both Admin.
//                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
//
//                        // The below url with HttpMethods allow both Admin for various permission
//                        .requestMatchers(HttpMethod.GET,"/admin/get-admin").hasAuthority(Permissions.ADMIN_READ.name())
//                        .requestMatchers(HttpMethod.POST,"/admin/post-admin").hasAuthority(Permissions.ADMIN_CREATE.name())
//                        .requestMatchers(HttpMethod.PUT,"/admin/update-admin").hasAuthority(Permissions.ADMIN_UPDATE.name())
//                        .requestMatchers(HttpMethod.DELETE,"/admin/delete-admin").hasAuthority(Permissions.ADMIN_DELETE.name())

                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }


    @Bean
    public AuthenticationProvider getAuthenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getBcryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}
