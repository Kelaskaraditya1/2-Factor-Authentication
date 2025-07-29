package com.starkIndustries.RoleBasedAuthorization.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/get-admin")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("GET:Admin controller");
    }

    @PostMapping("/post-admin")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> postAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("POST:Admin controller");

    }

    @PutMapping("/update-admin")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> updateAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("PUT:Admin controller");

    }

    @DeleteMapping("/delete-admin")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("DELETE:Admin controller");

    }

}
