package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransactionRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class TransactionNotificationService implements TransactionService {

    private NotificationService notificationService;

    private AccountsService accountsService;

    private TransactionService nextService;

    public TransactionNotificationService(NotificationService notificationService, AccountsService accountsService) {
        this.notificationService = notificationService;
        this.accountsService = accountsService;
    }

    @Override
    public void transfer(TransactionRequest transactionRequest) {
        Account sourceAccount = accountsService.getAccount(transactionRequest.getAccountFromId());
        Account destinationAccount = accountsService.getAccount(transactionRequest.getAccountToId());

        notificationService.notifyAboutTransfer(sourceAccount,"Amount " + transactionRequest.getAmount()+" debited to account "+ destinationAccount.getAccountId());
        notificationService.notifyAboutTransfer(destinationAccount,"Amount " + transactionRequest.getAmount()+" credited from account "+ sourceAccount.getAccountId());

        Optional.ofNullable(nextService).ifPresent(nextService -> nextService.transfer(transactionRequest));
    }

    @Override
    public void next(TransactionService transactionService) {
        this.nextService = transactionService;
    }
}
