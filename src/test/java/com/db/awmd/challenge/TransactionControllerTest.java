package com.db.awmd.challenge;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.service.AccountsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TransactionControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  AccountsRepository accountsRepository;

  @Before
  public void prepareMockMvc() {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void postSuccessfullTransaction() throws Exception {
    accountsRepository.clearAccounts();
    accountsRepository.createAccount(new Account("Id-123",new BigDecimal(1000)));
    accountsRepository.createAccount(new Account("Id-456",new BigDecimal(100)));
    this.mockMvc.perform(post("/v1/transaction").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-456\",\"amount\":1000}")).andExpect(status().is2xxSuccessful());
  }

  @Test
  public void postInvalidTransactionWithTransferAmountAsZero() throws Exception {
    accountsRepository.clearAccounts();
    accountsRepository.createAccount(new Account("Id-123",new BigDecimal(1000)));
    accountsRepository.createAccount(new Account("Id-456",new BigDecimal(100)));

    this.mockMvc.perform(post("/v1/transaction").contentType(MediaType.APPLICATION_JSON)
            .content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-456\",\"amount\":0}"))
            .andExpect(status().isBadRequest());
  }

  @Test
  public void postTransactionWithInvalidAccountId() throws Exception {
    accountsRepository.clearAccounts();

    this.mockMvc.perform(post("/v1/transaction").contentType(MediaType.APPLICATION_JSON)
            .content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-456\",\"amount\":100}"))
            .andExpect(status().is4xxClientError())
            .andExpect(content().string("Source or destination account id not valid"));
  }

    @Test
    public void postTransactionWithAmountGreaterThanSourceAccountBalance() throws Exception {
        accountsRepository.clearAccounts();
        accountsRepository.createAccount(new Account("Id-123", new BigDecimal(10)));
        accountsRepository.createAccount(new Account("Id-456", new BigDecimal(100)));

        this.mockMvc.perform(post("/v1/transaction").contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-456\",\"amount\":20}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Withdraw Amount should be greater than current balance and Non negative value"));
    }

}
