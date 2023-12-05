package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.dto.request.TransferRequestWithPhone;
import com.nebiyu.Kelal.dto.response.*;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.dto.request.TransferRequestWithEmail;
import com.nebiyu.Kelal.dto.request.TransferRequestWithId;
import com.nebiyu.Kelal.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("/api/v1/user/transactions")
public class TransactionController {


    @Autowired
    private TransactionService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TransactionService transactionService;



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
    @PostMapping("/transactionWithId")
    public ResponseEntity<TransactionResponseId> tranferWithId(@RequestBody TransferRequestWithId request, @RequestHeader("Authorization") String token){
        TransactionResponseId response = service.transferMoneyById(request, token);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);

    }
    @PostMapping("/transaction_via_phone")
    public ResponseEntity<TransactionResponseViaPhone> tranferWithPhone(@RequestBody TransferRequestWithPhone request, @RequestHeader("Authorization") String token){
        TransactionResponseViaPhone response = service.transferMoneyViaPhone(request, token);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);

    }
    @PostMapping(value = "${GET_TRANSACTION_HISTORY}", produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<TransactionHistoryResponse> getTransactionHistoryPerUserId(@RequestParam Long id){
        TransactionHistoryResponse response = transactionService.getTransactionHistoryWithId(id);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }
    @PostMapping(value = "${RECENT_TRANSACTION}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecentTransactionResponse> getRecentTransaction(@RequestParam Long id){
        RecentTransactionResponse response = transactionService.getRecentTransaction(id);
        if (response.isError()){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }




    }

