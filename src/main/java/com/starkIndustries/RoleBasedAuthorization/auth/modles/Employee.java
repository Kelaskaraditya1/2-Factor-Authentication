package com.starkIndustries.RoleBasedAuthorization.auth.modles;

import com.starkIndustries.RoleBasedAuthorization.auth.role.Role;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;

    private String name;

    private String email;

    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

}
