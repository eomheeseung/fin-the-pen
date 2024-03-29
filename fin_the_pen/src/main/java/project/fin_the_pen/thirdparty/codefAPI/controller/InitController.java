package project.fin_the_pen.thirdparty.codefAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.thirdparty.codefAPI.service.connectedId.ConnectedService;
import project.fin_the_pen.thirdparty.codefAPI.service.CodefPublishTokenService;
import project.fin_the_pen.thirdparty.codefAPI.service.bank.CodefIndIndividualService;
import project.fin_the_pen.finClient.core.error.customException.ConnectedSaveException;
import project.fin_the_pen.thirdparty.codefAPI.token.entity.Token;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.individual.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/codef")
public class InitController {
    private final CodefPublishTokenService tokenService;
    private final CodefIndIndividualService apiService;
    private final ConnectedService connectedService;

    /**
     * accessTokenPublish
     * 일주일에 1번 발급 받으면 되고, DB에 넣어놓을 것임.
     */
    @GetMapping("/accessTokenPublish")
    public void codefAccessToken() {
        String clientId = "4e08356c-1940-4c69-9ec9-1d55f12c3cf2";
        String clientSecret = "9ea5d443-4699-41bf-bbe6-27db50e2dee8";

        tokenService.init(clientId, clientSecret);
    }

    /**
     * 그냥 토큰 DB에 잘 들어갔나 확인하는 코드
     *
     * @return
     */
    @GetMapping("/findToken")
    public List<Token> codefFindToken() {
        return apiService.findToken();
    }

    /**
     * connectedId 발급(해결!)
     *
     * @param list
     */
//    @GetMapping("/accountCreate")
    public void codefAccountCreate(@RequestBody AccountList list) {
        connectedService.accountCreate(list);
    }

    /**
     * connectedId 발급 front 연동
     */
    @PostMapping("/accountCreate")
    public boolean codeAccountCreate(@RequestBody AccountList list) {
        log.info(list.getAccountList().get(0).getId());

        List<CreateDTO> innerList = list.getAccountList();

        for (CreateDTO createDTO : innerList) {
            createDTO.setCountryCode("KR");
            createDTO.setLoginType("1");
            createDTO.setClientType("P");
        }

        try {
            // 성공하면 true, 실패하면 false
            connectedService.accountCreate(list);
            //TODO 성공하면 true, 실패하면 false로 front에 알려줄 것임
            return true;
        } catch (ConnectedSaveException e) {
            return false;
        }
    }

    /**
     * 계정 추가
     */
    @GetMapping("/account/add")
    public void codefAccountAdd(@RequestBody AccountAddList list)
            throws IOException, ParseException, InterruptedException {
        connectedService.accountAdd(list);
    }

    /**
     * 계정 삭제 - 성공
     */
    @GetMapping("/account/delete")
    public void codefAccountDelete(@RequestBody AccountDeleteDTO dto) throws IOException, ParseException, InterruptedException {
        connectedService.accountDelete(dto);
    }

    /**
     * 계정 수정
     */
    @GetMapping("/account/update")
    public void codefAccountUpdate(@RequestBody AccountUpdateDTO dto) throws IOException, ParseException, InterruptedException {
        connectedService.accountUpdate(dto);
    }

    /**
     * 계정 목록
     */
    @GetMapping("/account/list")
    public void codefAccountList(@RequestBody ConcurrentHashMap<String, String> map) {
        connectedService.accountOutputList(map);
    }

    /**
     * 계정 추가 (레퍼런스)
     */
    @PostMapping("/account/reference-add")
    public void codefAccountReferenceAdd(@RequestBody AccountReferenceAddList list)
            throws IOException, ParseException, InterruptedException {
        connectedService.accountReferenceAdd(list);
    }
}
