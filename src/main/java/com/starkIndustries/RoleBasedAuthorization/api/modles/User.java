package com.starkIndustries.RoleBasedAuthorization.api.modles;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private Long userId;
    private String username;
    private String password;
    private boolean enabled;
}
