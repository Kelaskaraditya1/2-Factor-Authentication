package com.starkIndustries.RoleBasedAuthorization.api.modles;

import com.starkIndustries.RoleBasedAuthorization.api.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",joinColumns = @JoinColumn(name = "user_id")
    ,inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;
}
