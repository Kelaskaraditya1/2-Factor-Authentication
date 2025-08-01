package com.starkIndustries.RoleBasedAuthorization.auth.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {

    private String username;

    private String code;

}
