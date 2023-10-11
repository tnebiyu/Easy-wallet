package com.nebiyu.Kelal.transaction;
import com.nebiyu.Kelal.configuration.JWTService;
import com.nebiyu.Kelal.repositories.TransactionRepository;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.request.TransferRequestWithEmail;
import com.nebiyu.Kelal.response.TransferResponse;
import com.nebiyu.Kelal.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController

@RequestMapping("${TRANSACTION_API_CALL}")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TransactionService service;



    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferMoney(
            @RequestHeader("Authorization") String token,
        @RequestBody TransferRequestWithEmail request




    ) {
        TransferResponse response = service.transferMoneyByEmail(request, token);

if (response.isError()){
    return  ResponseEntity.badRequest().body(response);
}
return ResponseEntity.ok().body(response);



    }

    }

