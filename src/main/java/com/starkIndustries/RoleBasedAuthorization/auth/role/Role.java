package com.starkIndustries.RoleBasedAuthorization.auth.role;

import com.starkIndustries.RoleBasedAuthorization.auth.permissions.Permissions;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.starkIndustries.RoleBasedAuthorization.auth.permissions.Permissions.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum Role {
    USER(
            Collections.emptySet()
    ),
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    MANAGER_READ,
                    MANAGER_CREATE,
                    MANAGER_DELETE,
                    MANAGER_UPDATE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_CREATE,
                    MANAGER_DELETE,
                    MANAGER_UPDATE
            )
    );

    private Set<Permissions> permissionsSet;

    public List<SimpleGrantedAuthority> getAuthorities(){

        List<SimpleGrantedAuthority> authorities = getPermissionsSet().stream()
                .map(permissions->
                        new SimpleGrantedAuthority(permissions.getPermissions()))
                        .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));  // By Default
        return authorities;
    }

}
