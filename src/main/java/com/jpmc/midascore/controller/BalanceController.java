package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") Long userId) {

        // Find the user by their ID
        UserRecord user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            // Per instructions, return 0 if user does not exist
            return new Balance(0.0f);
        } else {
            // Return the user's current balance
            return new Balance(user.getBalance());
        }
    }
}