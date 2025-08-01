package com.starkIndustries.RoleBasedAuthorization.auth.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.starkIndustries.RoleBasedAuthorization.auth.modles.AuthUser;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignupResponse {

    private AuthUser authUser;

    private String jwtToken;

    private String refreshToken;

    private boolean mfaEnabled;

    private String qrCodeImageUri;
}