package project.fin_the_pen.codefAPI.api;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.dto.IntegratedDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.AuthenticationDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderAuthDTO;
import project.fin_the_pen.codefAPI.dto.bank.etc.HolderDTO;
import project.fin_the_pen.codefAPI.util.APIRequest;
import project.fin_the_pen.codefAPI.util.CommonConstant;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class EtcAPILogic implements APILogicInterface{
    @Override
    public HashMap<String, Object> registerMap(IntegratedDTO dto, HashMap<String, Object> registerMap) {
        if (dto instanceof AuthenticationDTO) {
            AuthenticationDTO transDto = (AuthenticationDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("account", transDto.getAccount());
            registerMap.put("inPrintType", transDto.getInPrintType());
            registerMap.put("inPrintContent", transDto.getInPrintContent());
            return registerMap;

        } else if (dto instanceof HolderDTO) {
            HolderDTO transDto = (HolderDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("account", transDto.getAccount());
            return registerMap;

        }
        return null;
    }

    /**
     * 계좌 인증 (1원인증)
     * @param dto
     * @return
     */
    public String authentication(AuthenticationDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/bank/a/account/transfer-authentication";

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }

    /**
     * 예금주명
     * @param dto
     * @return
     */
    public String holder(HolderDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/bank/a/account/holder";

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }

    /**
     * 예금주명 인증 (계좌 실명 인증)
     * @param dto
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String holderAuth(HolderAuthDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/bank/a/account/holder-authentication";

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }
}
