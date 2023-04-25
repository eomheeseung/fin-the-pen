package project.fin_the_pen.codefAPI.domain.individual;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class accountList {
    private List<createDTO> accountList = new ArrayList<>();
}
