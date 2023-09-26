package com.nebiyu.Kelal.transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestHeader("Authorization") String token,
            @RequestParam("receiverUsername") String receiverUsername,
            @RequestParam("amount") BigDecimal amount) {


        String senderUsername = extractUsernameFromToken(token);

        if (!isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");

        }

        // Perform the money transfer
        boolean success = transactionService.transferMoney(senderUsername, receiverUsername, amount);

        if (success) {
            return ResponseEntity.ok("Money transfer successful.");
        } else {
            return ResponseEntity.badRequest().body("Money transfer failed.");
        }
    }

    // Implement token validation logic here
    private boolean isTokenValid(String token) {
        // Implement token validation logic (e.g., check token expiration)
        // Return true if the token is still valid, false otherwise
        return true;
    }

    // Implement token extraction logic here
    private String extractUsernameFromToken(String token) {
        // Implement token validation logic
        // Extract and return the username from the token
        return null;
    }
}

