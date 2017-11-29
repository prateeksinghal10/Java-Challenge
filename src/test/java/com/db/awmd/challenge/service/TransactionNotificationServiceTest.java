package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransactionRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionNotificationServiceTest {

    @Mock
    TransactionService nextTransactionService;

    @Mock
    AccountsService accountsService;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    TransactionNotificationService transactionNotificationService;

    @Before
    public void setUp(){
        transactionNotificationService = new TransactionNotificationService(notificationService,accountsService);
        transactionNotificationService.next(nextTransactionService);
    }

    @Test
    public void testTransferVerifyAccountServiceIsCalledTwice() throws Exception {
        Account sourceAccount = new Account("Id-123", new BigDecimal(100));
        Account destinationAccount = new Account("Id-465", new BigDecimal(222));
        TransactionRequest transactionRequest = new TransactionRequest("Id-123","Id-456",new BigDecimal(10));

        when(accountsService.getAccount("Id-123")).thenReturn(sourceAccount);
        when(accountsService.getAccount("Id-456")).thenReturn(destinationAccount);
        doNothing().when(notificationService).notifyAboutTransfer(any(Account.class), anyString());
        doNothing().when(nextTransactionService).transfer(transactionRequest);

        transactionNotificationService.transfer(transactionRequest);

        verify(accountsService, atMost(2)).getAccount(anyString());

    }

    @Test
    public void testTransferVerifyNotificationServiceIsCalledTwice() throws Exception {
        Account sourceAccount = new Account("Id-123", new BigDecimal(100));
        Account destinationAccount = new Account("Id-465", new BigDecimal(222));
        TransactionRequest transactionRequest = new TransactionRequest("Id-123","Id-456",new BigDecimal(10));

        when(accountsService.getAccount("Id-123")).thenReturn(sourceAccount);
        when(accountsService.getAccount("Id-456")).thenReturn(destinationAccount);
        doNothing().when(notificationService).notifyAboutTransfer(any(Account.class), anyString());
        doNothing().when(nextTransactionService).transfer(transactionRequest);

        transactionNotificationService.transfer(transactionRequest);

        verify(notificationService, atMost(2)).notifyAboutTransfer(any(Account.class), anyString());
    }

    @Test
    public void testTransferVerifyNextTransactionServiceIsCalledOnce() throws Exception {
        Account sourceAccount = new Account("Id-123", new BigDecimal(100));
        Account destinationAccount = new Account("Id-465", new BigDecimal(222));
        TransactionRequest transactionRequest = new TransactionRequest("Id-123","Id-456",new BigDecimal(10));

        when(accountsService.getAccount("Id-123")).thenReturn(sourceAccount);
        when(accountsService.getAccount("Id-456")).thenReturn(destinationAccount);
        doNothing().when(notificationService).notifyAboutTransfer(any(Account.class), anyString());
        doNothing().when(nextTransactionService).transfer(transactionRequest);

        transactionNotificationService.transfer(transactionRequest);

        verify(nextTransactionService, atMost(1)).transfer(transactionRequest);

    }

}