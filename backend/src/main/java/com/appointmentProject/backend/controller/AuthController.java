package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AccountService accService;

    @GetMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {

        System.out.println("[AUTH] /api/auth/login called for username=" + username);

        // Try to find a matching account
        Account acc = accService.findByUsernameAndPassword(username, password);

        if (acc == null) {
            System.out.println("[AUTH] Login FAILED for username=" + username);
            return "INVALID";
        }

        String role = acc.getUserType().toString().trim();
        System.out.println("[AUTH] Login OK for username=" + username + " role=" + role);

        // Return the userType enum as a string
        return role;
    }
}
