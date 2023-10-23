package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.request.TransferRequestWithEmail;
import com.nebiyu.Kelal.request.TransferRequestWithId;
import com.nebiyu.Kelal.response.TransactionResponseId;
import com.nebiyu.Kelal.response.TransferResponse;
import com.nebiyu.Kelal.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("${TRANSACTION_API_CALL}")
public class TransactionController {


    @Autowired
    private TransactionService service;
    @Autowired
    private UserRepository userRepository;



    @PostMapping("${TRANSACTION_API}")
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
    @PostMapping("/transactionWithId")
    public ResponseEntity<TransactionResponseId> tranferWithId(@RequestBody TransferRequestWithId request){
        TransactionResponseId response = service.transferMoneyById(request);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);

    }




    }

