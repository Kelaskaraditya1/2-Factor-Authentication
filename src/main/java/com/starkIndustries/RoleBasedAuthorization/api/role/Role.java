package com.starkIndustries.RoleBasedAuthorization.api.role;

import com.starkIndustries.RoleBasedAuthorization.api.permissions.Permissions;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.starkIndustries.RoleBasedAuthorization.api.permissions.Permissions.*;

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

        var authorities = getPermissionsSet().stream()
                .map(authority->
                        new SimpleGrantedAuthority(authority.name()))
                .toList();

        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));  // By Default
        return authorities;
    }

}
