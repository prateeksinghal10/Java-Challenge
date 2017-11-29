package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransactionRequest;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AccountTransactionService implements TransactionService {

    private ReentrantLock reentrantLock;

    private AccountsService accountsService;

    private TransactionService nextService;

    public AccountTransactionService(AccountsService accountsService) {
        this.accountsService = accountsService;
        this.reentrantLock = new ReentrantLock(Boolean.TRUE);
    }

    public void transfer(TransactionRequest transactionRequest) {

        Account sourceAccount = accountsService.getAccount(transactionRequest.getAccountFromId());
        Account destinationAccount = accountsService.getAccount(transactionRequest.getAccountToId());

        if (sourceAccount == null || destinationAccount == null) {
            log.error("Source account id {} or destination account id {} is not valid", transactionRequest.getAccountFromId(), transactionRequest.getAccountToId());
            throw new AccountNotFoundException("Source or destination account id not valid");
        }

        while (true) {
            try {
                reentrantLock.tryLock(100, TimeUnit.MILLISECONDS);
                sourceAccount.withDraw(transactionRequest.getAmount());
                destinationAccount.deposit(transactionRequest.getAmount());
                break;
            } catch (InterruptedException interruptedException) {
                log.error("Thread interrupted {}", Thread.currentThread(), interruptedException);
            } finally {
                if (reentrantLock.isHeldByCurrentThread()) {
                    reentrantLock.unlock();
                }
            }
        }

        log.info("Transaction request completed successfully {}", transactionRequest);

        Optional.ofNullable(nextService).ifPresent(nextService -> nextService.transfer(transactionRequest));
    }

    @Override
    public void next(TransactionService nextService) {
        this.nextService = nextService;
    }

}

