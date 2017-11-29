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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountTransactionServiceTest {

    @Mock
    AccountsService accountsService;

    @Mock
    TransactionService nextTransactionService;

    @InjectMocks
    AccountTransactionService transactionService;

    @Before
    public void setUp(){
        transactionService = new AccountTransactionService(accountsService);
        transactionService.next(nextTransactionService);
    }

    @Test
    public void testTransferWithDraw() throws Exception {
        Account sourceAccount = new Account("Id-123", new BigDecimal(100));
        Account destinationAccount = new Account("Id-456", new BigDecimal(200));

        when(accountsService.getAccount("Id-123")).thenReturn(sourceAccount);
        when(accountsService.getAccount("Id-456")).thenReturn(destinationAccount);

        transactionService.transfer(new TransactionRequest("Id-123", "Id-456", new BigDecimal(10)));

        assertThat(sourceAccount.getBalance(), is(new BigDecimal(90)));

    }

    @Test
    public void testTransferDeposit() throws Exception {
        Account sourceAccount = new Account("Id-123", new BigDecimal(100));
        Account destinationAccount = new Account("Id-456", new BigDecimal(200));

        when(accountsService.getAccount("Id-123")).thenReturn(sourceAccount);
        when(accountsService.getAccount("Id-456")).thenReturn(destinationAccount);

        transactionService.transfer(new TransactionRequest("Id-123", "Id-456", new BigDecimal(10)));

        assertThat(destinationAccount.getBalance(), is(new BigDecimal(210)));

    }

    @Test
    public void testTransferVerifyNextIsCalledOnce() throws Exception {
        Account sourceAccount = new Account("Id-123", new BigDecimal(100));
        Account destinationAccount = new Account("Id-456", new BigDecimal(200));
        TransactionRequest transactionRequest = new TransactionRequest("Id-123", "Id-456", new BigDecimal(10));

        when(accountsService.getAccount("Id-123")).thenReturn(sourceAccount);
        when(accountsService.getAccount("Id-456")).thenReturn(destinationAccount);
        doNothing().when(nextTransactionService).next(transactionService);

        transactionService.transfer(transactionRequest);

        verify(nextTransactionService,atMost(1)).transfer(transactionRequest);
    }
}