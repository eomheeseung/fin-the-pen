package project.fin_the_pen.codefAPI.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.codefAPI.dto.analysis.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DataAnalysisRepository {
    @PersistenceContext
    EntityManager manager;

    private final DataRepository dataRepository;

    public void dataAnalysis(JSONObject jsonObject) {
        JSONObject dataJson = (JSONObject) jsonObject.get("data");
        JSONArray historyList = (JSONArray) dataJson.get("resTrHistoryList");
        JSONObject eachJson;

        for (Object o : historyList) {
            DataAnalysis dataAnalysis = new DataAnalysis();

            eachJson = (JSONObject) o;

            dataAnalysis.setCommEndDate(dataJson.get("resAccount").toString());
            dataAnalysis.setResAccountDisplay(dataJson.get("resAccountDisplay").toString());
            dataAnalysis.setResAccountName(dataJson.get("resAccountName").toString());
            dataAnalysis.setResAccountNickName(dataJson.get("resAccountNickName").toString());
            dataAnalysis.setResAccountHolder(dataJson.get("resAccountHolder").toString());
            dataAnalysis.setResAccountStartDate(dataJson.get("resAccountStartDate").toString());
            dataAnalysis.setResManagementBranch(dataJson.get("resManagementBranch").toString());
            dataAnalysis.setResAccountStatus(dataJson.get("resAccountStatus").toString());
            dataAnalysis.setResLastTranDate(dataJson.get("resLastTranDate").toString());
            dataAnalysis.setResLoanEndDate(dataJson.get("resLoanEndDate").toString());
            dataAnalysis.setResLoanLimitAmt(dataJson.get("resLoanLimitAmt").toString());
            dataAnalysis.setResInterestRate(dataJson.get("resInterestRate").toString());
            dataAnalysis.setResAccountBalance(dataJson.get("resAccountBalance").toString());
            dataAnalysis.setResWithdrawalAmt(dataJson.get("resWithdrawalAmt").toString());
            dataAnalysis.setCommStartDate(dataJson.get("commStartDate").toString());
            dataAnalysis.setCommEndDate(dataJson.get("commEndDate").toString());

            dataAnalysis.setResAccountTrDate(eachJson.get("resAccountTrDate").toString());
            dataAnalysis.setResAccountTrTime(eachJson.get("resAccountTrTime").toString());
            dataAnalysis.setResAccountOut(eachJson.get("resAccountOut").toString());
            dataAnalysis.setResAccountIn(eachJson.get("resAccountIn").toString());
            dataAnalysis.setResAccountDesc1(eachJson.get("resAccountDesc1").toString());
            dataAnalysis.setResAccountDesc2(eachJson.get("resAccountDesc2").toString());
            dataAnalysis.setResAccountDesc3(eachJson.get("resAccountDesc3").toString());
            dataAnalysis.setResAccountDesc4(eachJson.get("resAccountDesc4").toString());
            dataAnalysis.setResAfterTranBalance(eachJson.get("resAfterTranBalance").toString());

            manager.persist(dataAnalysis);
        }

    }

    // 카드 / 승인 내역
    public void dataApproval(JSONObject jsonObject) {
        JSONArray jsonArray = (JSONArray) jsonObject.get("data");

        for (Object o : jsonArray) {
            JSONObject innerJson = (JSONObject) o;

            DataApproval dataApproval = DataApproval.builder()
                    .resUsedDate(innerJson.get("resUsedDate").toString())
                    .resUsedTime(innerJson.get("resUsedTime").toString())
                    .resCardNo(innerJson.get("resCardNo").toString())
                    .resCardName(innerJson.get("resCardName").toString())
                    .resUsedAmount(innerJson.get("resUsedAmount").toString())
                    .resPaymentType(innerJson.get("resPaymentType").toString())
                    .resInstallmentMonth(innerJson.get("resInstallmentMonth").toString())
                    .resPaymentDueDate(innerJson.get("resPaymentDueDate").toString())
                    .resMemberStoreType(innerJson.get("resMemberStoreType").toString())
                    .resCancelYN(innerJson.get("resCancelYN").toString())
                    .resCancelAmount(innerJson.get("resCancelAmount").toString())
                    .resVAT(innerJson.get("resVAT").toString())
                    .resCashBack(innerJson.get("resCashBack").toString())
                    .resKRWAmt(innerJson.get("resKRWAmt").toString())
                    .build();

            dataRepository.save(dataApproval);
        }
    }

    // 실적 조회
    public void resultCheck(JSONObject jsonObject) {
        JSONObject benefitJson = (JSONObject) jsonObject.get("resCardBenefitList");
        JSONObject detailJson = (JSONObject) jsonObject.get("resDetailList");
        JSONObject performanceJson = (JSONObject) jsonObject.get("resCardPerformanceList");


        DataResultCheck dataResultCheck = DataResultCheck.builder()
                .resCardName(jsonObject.get("resCardName").toString())
                .resCardNo(jsonObject.get("resCardNo").toString())
                .resCardType(jsonObject.get("resCardType").toString())
                .resCardCompany(jsonObject.get("resCardCompany").toString())
                .resCardBenefitName(benefitJson.get("resCardBenefitName").toString())
                .resBusinessTypes(benefitJson.get("resBusinessTypes").toString())
                .resDetailCardBenefitName(detailJson.get("resCardBenefitName").toString())
                .resDetailPerformanceConditions(detailJson.get("resPerformanceConditions").toString())
                .resDetailBusinessTypes(detailJson.get("resBusinessTypes").toString())
                .resCurrentUseAmt(performanceJson.get("resCurrentUseAmt").toString())
                .resRequiredPerformanceAmt(performanceJson.get("resRequiredPerformanceAmt").toString())
                .commStartDate(performanceJson.get("commStartDate").toString())
                .commEndDate(performanceJson.get("commEndDate").toString())
                .resCoverageCriteria(performanceJson.get("resCoverageCriteria").toString())
                .resMeetPerformanceYN(performanceJson.get("resMeetPerformanceYN").toString())
                .resStandardUseAmt(performanceJson.get("resStandardUseAmt").toString())
                .build();

        manager.persist(dataResultCheck);
    }

    // 청구내역
    public void BillingList(JSONObject jsonObject) {
        JSONObject historyJson = (JSONObject) jsonObject.get("resChangeHistoryList");

        DataBilling dataBilling = DataBilling.builder()
                .resTotalAmount(jsonObject.get("resTotalAmount").toString())
                .resPaymentAccount(jsonObject.get("resPaymentAccount").toString())
                .resFullAmt(jsonObject.get("resFullAmt").toString())
                .resInstallmentAmt(jsonObject.get("resInstallmentAmt").toString())
                .resMinDepositAmt(jsonObject.get("resMinDepositAmt").toString())
                .resAmountOutstanding(jsonObject.get("resAmountOutstanding").toString())
                .resLateFee(jsonObject.get("resLateFee").toString())
                .resCashService(jsonObject.get("resCashService").toString())
                .resCardLoan(jsonObject.get("resCardLoan").toString())
                .resRevolving(jsonObject.get("resRevolving").toString())
                .resAnnualFee(jsonObject.get("resAnnualFee").toString())
                .resPreWithdrawal(jsonObject.get("resPreWithdrawal").toString())
                .resOverseasUse(jsonObject.get("resOverseasUse").toString())
                .resWithdrawalDueDate(jsonObject.get("resWithdrawalDueDate").toString())
                .resPaymentDueDate(jsonObject.get("resPaymentDueDate").toString())
                .resFee(historyJson.get("resFee").toString())
                .resMemberStoreName(historyJson.get("resMemberStoreName").toString())
                .resUsedAmount(historyJson.get("resUsedAmount").toString())
                .resPaymentType(historyJson.get("resPaymentType").toString())
                .resInstallmentMonth(historyJson.get("resInstallmentMonth").toString())
                .resRoundNo(historyJson.get("resRoundNo").toString())
                .resPaymentAmt(historyJson.get("resPaymentAmt").toString())
                .resAfterPaymentBalance(historyJson.get("resAfterPaymentBalance").toString())
                .resEarnPoint(historyJson.get("resEarnPoint").toString())
                .resUsedCard(historyJson.get("resUsedCard").toString())
                .resPaymentPrincipal(historyJson.get("resPaymentPrincipal").toString())
                .resMemberStoreType(historyJson.get("resMemberStoreType").toString())
                .resUsedDate(historyJson.get("resUsedDate").toString()).build();

        manager.persist(dataBilling);
    }

    // 한도 조회
    public void AmountLimit(JSONObject jsonObject) {
        JSONObject totalJson = (JSONObject) jsonObject.get("resLimitOfTotalList");
        JSONObject fullJson = (JSONObject) jsonObject.get("resLimitOfFullTotalList");
        JSONObject installJson = (JSONObject) jsonObject.get("resLimitOfInstallmentList");
        JSONObject shortJson = (JSONObject) jsonObject.get("resLimitOfShortLoanList");


        DataAccountLimit dataAccountLimit = DataAccountLimit.builder()
                .resCardCompany(jsonObject.get("resCardCompany").toString())
                .resUsedAmount(totalJson.get("resUsedAmount").toString())
                .resLimitAmount(totalJson.get("resLimitAmount").toString())
                .resRemainLimit(totalJson.get("resRemainLimit").toString())
                .resFullUsedAmount(fullJson.get("resUsedAmount").toString())
                .resFullRemainLimit(fullJson.get("resLimitAmount").toString())
                .resFullRemainLimit(fullJson.get("resRemainLimit").toString())
                .resInstallUsedAmount(installJson.get("resUsedAmount").toString())
                .resInstallLimitAmount(installJson.get("resLimitAmount").toString())
                .resInstallRemainLimit(installJson.get("resRemainLimit").toString())
                .resShortUsedAmount(shortJson.get("resUsedAmount").toString())
                .resShortLimitAmount(shortJson.get("resLimitAmount").toString())
                .resShortRemainLimit(shortJson.get("resRemainLimit").toString())
                .build();

        manager.persist(dataAccountLimit);
    }

    // 하이패스
    public void HiPass(JSONObject jsonObject) {
        DataHipass hipass = DataHipass.builder()
                .resUsedDate(jsonObject.get("resUsedDate").toString())
                .resCardNo(jsonObject.get("resCardNo").toString())
                .resMemberStoreName(jsonObject.get("resMemberStoreName").toString())
                .resUsedAmount(jsonObject.get("resUsedAmount").toString())
                .resDepartureTime(jsonObject.get("resDepartureTime").toString())
                .resArrivalTime(jsonObject.get("resArrivalTime").toString())
                .resDepartureStation(jsonObject.get("resDepartureStation").toString())
                .resArrivalStation(jsonObject.get("resArrivalStation").toString())
                .resPaymentDueDate(jsonObject.get("resPaymentDueDate").toString())
                .build();

        manager.persist(hipass);
    }
}
