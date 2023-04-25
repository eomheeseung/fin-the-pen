package project.fin_the_pen.codefAPI.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.codefAPI.domain.analysis.DataAnalysis;

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

    public void dataAnalysis(JSONObject jsonObject, JSONArray jsonArray) {
        DataAnalysis dataAnalysis = new DataAnalysis();

        dataAnalysis.setResAccount((String) jsonObject.get("resAccount"));
        dataAnalysis.setResAccountBalance((String) jsonObject.get("resAccountBalance"));
        dataAnalysis.setResWithdrawalAmt((String) jsonObject.get("resWithdrawalAmt"));
        dataAnalysis.setResAccountNickName((String) jsonObject.get("resAccountNickName"));
        dataAnalysis.setResAccountStartDate((String) jsonObject.get("resAccountStartDate"));
        dataAnalysis.setResLastTranDate((String) jsonObject.get("resLastTranDate"));
        dataAnalysis.setResLoanLimitAmt((String) jsonObject.get("resLoanLimitAmt"));
        dataAnalysis.setResAccountName((String) jsonObject.get("resAccountName"));
        dataAnalysis.setResAccountHolder((String) jsonObject.get("resAccountHolder"));
        dataAnalysis.setResManagementBranch((String) jsonObject.get("resManagementBranch"));
        dataAnalysis.setResInterestRate((String) jsonObject.get("resInterestRate"));
        dataAnalysis.setResAccountDisplay((String) jsonObject.get("resAccountDisplay"));
        dataAnalysis.setResLoanEndDate((String) jsonObject.get("resLoanEndDate"));
        dataAnalysis.setResAccountStatus((String) jsonObject.get("resAccountStatus"));
        dataAnalysis.setCommStartDate((String) jsonObject.get("commStartDate"));
        dataAnalysis.setCommEndDate((String) jsonObject.get("commEndDate"));
        dataAnalysis.setResAccountStatus((String) jsonObject.get("resAccountStatus"));


        jsonArray.forEach(o -> {
            JSONObject o1 = (JSONObject) o;
            dataAnalysis.setResAccountTrTime((String) o1.get("resAccountTrTime"));
            dataAnalysis.setResAccountOut((String) o1.get("resAccountOut"));
            dataAnalysis.setResAccountIn((String) o1.get("resAccountIn"));
            dataAnalysis.setResAfterTranBalance((String) o1.get("resAfterTranBalance"));
            dataAnalysis.setResAccountDesc1((String) o1.get("resAccountDesc1"));
            dataAnalysis.setResAccountDesc2((String) o1.get("resAccountDesc2"));
            dataAnalysis.setResAccountDesc3((String) o1.get("resAccountDesc3"));
            dataAnalysis.setResAccountDesc4((String) o1.get("resAccountDesc4"));
            dataAnalysis.setResAccountTrDate((String) o1.get("resAccountTrDate"));
        });

        manager.persist(dataAnalysis);
        manager.close();
    }
}
