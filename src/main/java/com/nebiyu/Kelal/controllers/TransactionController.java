package com.nebiyu.Kelal.controllers;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.dto.request.TransferRequestWithEmail;
import com.nebiyu.Kelal.dto.request.TransferRequestWithId;
import com.nebiyu.Kelal.dto.response.RecentTransactionResponse;
import com.nebiyu.Kelal.dto.response.TransactionHistoryResponse;
import com.nebiyu.Kelal.dto.response.TransactionResponseId;
import com.nebiyu.Kelal.dto.response.TransferResponse;
import com.nebiyu.Kelal.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("${TRANSACTION_CLASS_REQUEST_MAPPING}")
public class TransactionController {


    @Autowired
    private TransactionService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TransactionService transactionService;



    @PostMapping("${TRANSFER_EMAIL}")
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
    @PostMapping("${TRANSFER_WITH_ID}")
    public ResponseEntity<TransactionResponseId> tranferWithId(@RequestBody TransferRequestWithId request, @RequestHeader("Authorization") String token){
        TransactionResponseId response = service.transferMoneyById(request, token);
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

