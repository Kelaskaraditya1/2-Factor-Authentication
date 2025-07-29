package com.starkIndustries.RoleBasedAuthorization.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/get-admin")
    public ResponseEntity<?> getAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("GET:Admin controller");
    }

    @PostMapping("/post-admin")
    public ResponseEntity<?> postAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("POST:Admin controller");

    }

    @PutMapping("/update-admin")
    public ResponseEntity<?> updateAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("PUT:Admin controller");

    }

    @DeleteMapping("/delete-admin")
    public ResponseEntity<?> deleteAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("DELETE:Admin controller");

    }

}
