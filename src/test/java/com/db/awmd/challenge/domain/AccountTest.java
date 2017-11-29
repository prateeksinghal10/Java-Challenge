package com.db.awmd.challenge.domain;

import com.db.awmd.challenge.exception.InvalidAmountException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by hodor on 28-11-2017.
 */

public class AccountTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testDepositWithNegativeValue(){
        thrown.expect(InvalidAmountException.class);
        thrown.expectMessage(is("Deposit amount should be Non negative value"));

        Account account = new Account("Id-123",new BigDecimal(100));
        account.deposit(new BigDecimal(-100));
        Assert.assertThat(account.getBalance(), is(new BigDecimal(100)));

    }

    @Test
    public void testDepositWithPositiveValue(){
        Account account = new Account("Id-123",new BigDecimal(100));
        account.deposit(new BigDecimal(10.5));
        Assert.assertThat(account.getBalance(), is(new BigDecimal(110.5)));
    }

    @Test
    public void testWithdrawWithNegativeValue(){
        thrown.expect(InvalidAmountException.class);
        thrown.expectMessage(is("Withdraw Amount should be greater than current balance and Non negative value"));

        Account account = new Account("Id-123",new BigDecimal(100));
        account.withDraw(new BigDecimal(-10));
        Assert.assertThat(account.getBalance(), is(new BigDecimal(100)));
    }

    @Test
    public void testWithdrawPositiveValue(){
        Account account = new Account("Id-123",new BigDecimal(100));
        account.withDraw(new BigDecimal(10.5));
        Assert.assertThat(account.getBalance(), is(new BigDecimal(89.5)));
    }

    @Test
    public void testWithdrawValueGreaterThanCurrentBalance(){
        thrown.expect(InvalidAmountException.class);
        thrown.expectMessage(is("Withdraw Amount should be greater than current balance and Non negative value"));

        Account account = new Account("Id-123",new BigDecimal(100));
        account.withDraw(new BigDecimal(150));
        Assert.assertThat(account.getBalance(), is(new BigDecimal(100)));
    }

}