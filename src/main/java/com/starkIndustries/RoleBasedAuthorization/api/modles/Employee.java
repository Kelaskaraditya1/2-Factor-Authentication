package com.starkIndustries.RoleBasedAuthorization.api.modles;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {

    private Long empId;

    private String name;

    private String email;

    private String username;

    private String password;
}
