package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.TransactionRequest;

public interface TransactionService {
    void transfer(TransactionRequest transactionRequest);
    void next(TransactionService transactionService);
}
