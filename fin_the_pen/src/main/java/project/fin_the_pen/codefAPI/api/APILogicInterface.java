package project.fin_the_pen.codefAPI.api;

import project.fin_the_pen.codefAPI.domain.IntegratedDTO;

import java.util.HashMap;

public interface APILogicInterface {
    HashMap<String, Object> registerMap(IntegratedDTO dto, HashMap<String, Object> registerMap);
}
