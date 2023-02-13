package project.fin_the_pen.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.data.account.Account;

@Component
@Getter
@Slf4j
public class APIService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void init(String response) {
        try {
            Account saveAccount = objectMapper.readValue(response, Account.class);
            log.info(String.valueOf(saveAccount.getBalance_amt()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
