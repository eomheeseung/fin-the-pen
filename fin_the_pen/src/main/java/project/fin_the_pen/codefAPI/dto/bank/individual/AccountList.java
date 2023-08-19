package project.fin_the_pen.codefAPI.dto.bank.individual;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AccountList {
    private List<CreateDTO> accountList = new ArrayList<>();
}
