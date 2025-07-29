package com.starkIndustries.RoleBasedAuthorization.api.dto.request;

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
