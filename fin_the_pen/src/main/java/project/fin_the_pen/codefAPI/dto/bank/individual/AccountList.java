package project.fin_the_pen.codefAPI.dto.bank.individual;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AccountList {
    @JsonProperty(value = "data")
    private List<CreateDTO> accountList = new ArrayList<>();
}
