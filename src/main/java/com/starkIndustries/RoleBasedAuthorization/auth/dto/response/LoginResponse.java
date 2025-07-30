package com.starkIndustries.RoleBasedAuthorization.auth.dto.response;

import com.starkIndustries.RoleBasedAuthorization.auth.modles.Employee;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponse {

    private Employee employee;

    private String jwtToken;

    private String refreshToken;
}
