package project.fin_the_pen.model.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import project.fin_the_pen.finClient.core.error.customException.TokenNotFoundException;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.report.dto.ConsumeReportRequestDTO;
import project.fin_the_pen.model.report.dto.ConsumeReportResponseDTO;
import project.fin_the_pen.model.report.dto.ExpenditureRequestDTO;
import project.fin_the_pen.model.report.entity.Reports;
import project.fin_the_pen.model.report.repository.ReportRepository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.CRUDScheduleRepository;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.usersToken.entity.UsersToken;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReportService {
    private final TokenManager tokenManager;
    private final UsersTokenRepository tokenRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReportRepository reportRepository;

    public Boolean setAmount(ExpenditureRequestDTO dto, HttpServletRequest request) {
        String accessToken = tokenManager.parseBearerToken(request);

        try {
            if (accessToken == null) {
                throw new RuntimeException();
            }

            Reports report = Reports.builder()
                    .userId(dto.getUserId())
                    .amount(dto.getAmount())
                    .build();

            reportRepository.save(report);
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }


    public Map<String, Object> inquiryReport(ConsumeReportRequestDTO dto, HttpServletRequest request) {
        String accessToken = tokenManager.parseBearerToken(request);
        Map<String, Object> responseMap = new HashMap<>();

        String userId = dto.getUserId();
        String date = dto.getDate();

        Map<String, Integer> map = new HashMap<>();

        try {
            if (accessToken == null) {
                throw new RuntimeException();
            }

            Optional<UsersToken> findToken = Optional.ofNullable(tokenRepository.findUsersToken(accessToken)
                    .orElseThrow(() -> new TokenNotFoundException("token not found")));

            // 현재 토큰으로 로그인 된 사용자의 userId와 클라이언트로부터 전달받은 userId값이 일치하지 않은 경우 error!!!
            if (!findToken.get().getUserId().equals(userId)) {
                throw new Exception("error");
            } else {
                List<Schedule> responseArray = scheduleRepository.findMonthSchedule(date, userId);

                if (responseArray.isEmpty()) {
                    responseMap.put("data", "error");
                } else {

                    responseArray.forEach(schedule -> {
                        String category = schedule.getCategory();
                        Integer amount = Integer.parseInt(schedule.getAmount());

                        if (schedule.getPriceType().equals(PriceType.Minus)) {
                            map.compute(category, (key, value) -> (value == null) ? amount : value + amount);
                        }
                    });
                }


                // Map의 entry를 List로 변환
                List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());

                // Comparator를 사용하여 value를 기준으로 내림차순 정렬
                entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                List<Map.Entry<String, Integer>> topEntries = entryList.subList(0, Math.min(5, entryList.size()));
                int sum = topEntries.stream().mapToInt(Map.Entry::getValue).sum();

                List<ConsumeReportResponseDTO> responseList = new ArrayList<>();

                for (Map.Entry<String, Integer> entry : topEntries) {
                    int percentage = (int) ((entry.getValue() * 100.0) / sum);
                    responseList.add(new ConsumeReportResponseDTO(entry.getKey(), entry.getValue(), String.valueOf(percentage)));
                }


                responseMap.put("data", responseList);

            }
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
        return responseMap;
    }
}
