package com.db.awmd.challenge.domain;

import com.db.awmd.challenge.exception.InvalidAmountException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  public void deposit(BigDecimal amount){
    if(BigDecimal.ZERO.compareTo(amount)<0) {
        balance = balance.add(amount);
    }
      else throw new InvalidAmountException("Deposit amount should be Non negative value");
  }

  public void withDraw(BigDecimal amount){
    if(BigDecimal.ZERO.compareTo(amount)<0 && balance.compareTo(amount)>=0){
      balance=balance.subtract(amount);
    }
      else throw new InvalidAmountException("Withdraw Amount should be greater than current balance and Non negative value");
  }
}
