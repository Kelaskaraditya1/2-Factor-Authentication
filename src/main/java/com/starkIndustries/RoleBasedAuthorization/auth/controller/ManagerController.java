package com.starkIndustries.RoleBasedAuthorization.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/get-manager")
    public ResponseEntity<?> getManager(){
        return ResponseEntity.status(HttpStatus.OK).body("GET:Manager controller");
    }

    @PostMapping("/post-manager")
    public ResponseEntity<?> postManager(){
        return ResponseEntity.status(HttpStatus.OK).body("POST:Manager controller");

    }

    @PutMapping("/update-manager")
    public ResponseEntity<?> updateManager(){
        return ResponseEntity.status(HttpStatus.OK).body("PUT:Manager controller");

    }

    @DeleteMapping("/delete-manager")
    public ResponseEntity<?> deleteManager(){
        return ResponseEntity.status(HttpStatus.OK).body("DELETE:Manager controller");

    }
}
