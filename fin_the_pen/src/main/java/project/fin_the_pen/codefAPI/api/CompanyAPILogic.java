package project.fin_the_pen.codefAPI.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import project.fin_the_pen.codefAPI.domain.IntegratedDTO;
import project.fin_the_pen.codefAPI.domain.company.*;
import project.fin_the_pen.codefAPI.repository.DataAnalysisRepository;
import project.fin_the_pen.codefAPI.util.APIRequest;
import project.fin_the_pen.codefAPI.util.CommonConstant;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyAPILogic implements APILogicInterface {
    private final DataAnalysisRepository repository;

    @Override
    public HashMap<String, Object> registerMap(IntegratedDTO dto, HashMap<String, Object> registerMap) {
        if (dto instanceof AccountVerificationDTO) {
            AccountVerificationDTO transDto = (AccountVerificationDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("withdrawAccountNo", transDto.getWithdrawAccountNo());
            registerMap.put("withdrawAccountPassword", transDto.getWithdrawAccountPassword());
            registerMap.put("transferPassword", transDto.getTransferPassword());

            return registerMap;

        } else if (dto instanceof LoanTransactionDTO) {
            LoanTransactionDTO transDto = (LoanTransactionDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("accountLoanExecNo", transDto.getAccountLoanExecNo());

            return registerMap;

        } else if (dto instanceof CompanyTransactionDTO) {
            CompanyTransactionDTO transDto = (CompanyTransactionDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("fastId", transDto.getFastId());
            registerMap.put("fastPassword", transDto.getFastPassword());
            registerMap.put("id", transDto.getId());
            registerMap.put("password", transDto.getPassword());
            registerMap.put("account", transDto.getAccount());
            registerMap.put("accountPassword", transDto.getAccountPassword());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("identity", transDto.getIdentity());

            return registerMap;

        } else if (dto instanceof CompanyAccountTransactionDTO) {
            CompanyAccountTransactionDTO transDto = (CompanyAccountTransactionDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("inquiryType", transDto.getInquiryType());
            registerMap.put("pageCount", transDto.getPageCount());

            return registerMap;

        } else if (dto instanceof CompanyExchangeDTO) {
            CompanyExchangeDTO transDto = (CompanyExchangeDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("currency", transDto.getCurrency());

            return registerMap;

        } else if (dto instanceof InstallmentSavingDTO) {
            InstallmentSavingDTO transDto = (InstallmentSavingDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            registerMap.put("inquiryType", transDto.getInquiryType());
            return registerMap;

        } else if (dto instanceof FundDTO) {
            FundDTO transDto = (FundDTO) dto;

            registerMap.put("organization", transDto.getOrganization());
            registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
            registerMap.put("account", transDto.getAccount());
            registerMap.put("startDate", transDto.getStartDate());
            registerMap.put("endDate", transDto.getEndDate());
            registerMap.put("orderBy", transDto.getOrderBy());
            return registerMap;

        }
        return null;
    }

    /**
     * 계좌 비밀번호 검증
     *
     * @param dto
     * @return
     */
    public String accountVerification(AccountVerificationDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + "/v1/kr/bank/b/account/account-password-verification";

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }

    /**
     * 대출 거래내역
     *
     * @param dto
     * @return
     */
    public String loanTransaction(LoanTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_B_004;
        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;

    }

    /**
     * 보유 계좌
     *
     * @param organization
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public String accountList(String organization) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_B_001;

        HashMap<String, Object> registerMap = CreateMap.create();
        registerMap.put("connectedId", CommonConstant.CONNECTED_ID);
        registerMap.put("organization", organization);

        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);

        return result;
    }

    /**
     * 빠른 조회
     *
     * @param dto
     * @return
     */
    public String accountTransaction(CompanyTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_B_007;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);

        //string -> jsonObject
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(result);
        JSONArray jsonArray = (JSONArray) jsonObject.get("resTrHistoryList");

        repository.dataAnalysis(jsonObject, jsonArray);

        return result;
    }

    /**
     * 수시 입출 거래 내역
     *
     * @param dto
     * @return
     */
    public String accountTransactionList(CompanyAccountTransactionDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_B_002;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);

        return result;
    }

    /**
     * 외화 거래 내역
     *
     * @param dto
     * @return
     */
    public String exchange(CompanyExchangeDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_B_005;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }

    /**
     * 적금 거래 내역
     *
     * @param dto
     * @return
     */
    public String installmentSaving(InstallmentSavingDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_B_003;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }

    /**
     * 펀드 거래내역
     * @param dto
     * @return
     */
    public String fundTransaction(FundDTO dto) throws IOException, ParseException, InterruptedException {
        String urlPath = CommonConstant.TEST_DOMAIN + CommonConstant.KR_BK_1_B_006;

        HashMap<String, Object> registerMap = registerMap(dto, CreateMap.create());
        String result = APIRequest.request(urlPath, registerMap);
        log.info(result);
        return result;
    }
}
