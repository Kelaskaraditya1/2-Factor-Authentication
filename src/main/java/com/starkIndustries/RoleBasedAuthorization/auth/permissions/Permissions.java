package com.starkIndustries.RoleBasedAuthorization.auth.permissions;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public enum Permissions {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("manager:read"),
    MANAGER_CREATE("manager:create"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_DELETE("manager:delete");;


    private String permissions;
}
