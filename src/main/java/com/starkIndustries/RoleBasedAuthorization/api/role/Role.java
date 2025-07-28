package com.starkIndustries.RoleBasedAuthorization.api.role;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {

    private Long roleId;

    private String name;
}
