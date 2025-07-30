package com.starkIndustries.RoleBasedAuthorization.auth.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequestDto {

    private String username;

    private String password;

}
