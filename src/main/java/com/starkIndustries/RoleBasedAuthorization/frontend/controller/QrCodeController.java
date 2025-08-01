package com.starkIndustries.RoleBasedAuthorization.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QrCodeController {

        @GetMapping("/signup-2fa")
    public String showQrPage(@RequestParam String username,
                             @RequestParam String secret,
                             @RequestParam String uri,
                             Model model) {

        model.addAttribute("username", username);
        model.addAttribute("secret", secret);
        model.addAttribute("qrUri", uri);

        return "signup-2fa";
    }
}
