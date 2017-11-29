package com.db.awmd.challenge.configuration;

import com.db.awmd.challenge.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DevChallengeConfiguration {

    @Bean
    public TransactionService accountTransactionService( AccountsService accountService,TransactionService notificationAdapter){
        TransactionService transactionService = new AccountTransactionService(accountService);
        transactionService.next(notificationAdapter);
        return transactionService;
    }

    @Bean
    public TransactionService notificationAdaper(AccountsService accountsService, NotificationService notificationService){
        TransactionService transactionService = new TransactionNotificationService(notificationService,accountsService);
        return transactionService;
    }

    @Bean
    public NotificationService notificationService(){
      return  new EmailNotificationService();
    }

}
