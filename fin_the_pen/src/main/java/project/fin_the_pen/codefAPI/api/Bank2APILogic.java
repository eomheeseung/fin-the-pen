package project.fin_the_pen.codefAPI.api;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.dto.IntegratedDTO;
import project.fin_the_pen.codefAPI.dto.bank.bank2.Bank2AccountDTO;
import project.fin_the_pen.codefAPI.dto.bank.bank2.Bank2AccountTransactionDTO;
import project.fin_the_pen.codefAPI.util.APIRequest;
import project.fin_the_pen.codefAPI.util.CommonConstant;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class Bank2APILogic implements APILogicInterface {
    @Override
    public HashMap<String, Object> registerMap(IntegratedDTO dto, HashMap<String, Object> registerMap) {
        if (dto instanceof Bank2AccountDTO) {
            Bank2AccountDTO transDto = (Bank2AccountDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("bankName", transDto.getBankName());
            return registerMap;

        } else if (dto instanceof Bank2AccountTransactionDTO) {
            Bank2AccountTransactionDTO transDto = (Bank2AccountTransactionDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("bankName", transDto.getBankName());
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("inquiryType", transDto.getInquiryType());
            return registerMap;
        }
        return null;
    }

    /**
     * 보유계좌
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String account(Bank2AccountDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_2_P_001;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);

        return result;
    }

    /**
     * 수시입출 거래내역
     * @param dto
     * @return
     */
    public String accountTransaction(Bank2AccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_2_P_002;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);

        return result;
    }
}
