package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.TransactionRequest;
import com.db.awmd.challenge.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/transaction")
@Slf4j
public class TransactionController {

    private TransactionService accountTransactionService;

    @Autowired
    public TransactionController(TransactionService accountTransactionService) {
        this.accountTransactionService = accountTransactionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid TransactionRequest transactionRequest) {
        log.info("Initiating account transaction {}", transactionRequest);

        accountTransactionService.transfer(transactionRequest);

        log.info("Completed account transaction {}", transactionRequest);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

}
