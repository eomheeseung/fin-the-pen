package project.fin_the_pen.thirdparty.codefAPI.service.connectedId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.thirdparty.codef.repository.TokenRepository;
import project.fin_the_pen.model.token.entity.Token;
import project.fin_the_pen.thirdparty.codefAPI.dto.bank.individual.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectedService {
    private final TokenRepository tokenRepository;
    private final ConnectedLogic logic;
    private Token token;
    /**
     * connectedId 발급 받는 코드
     * 어차피 토큰 1개만 저장된다는 보장이 있음.
     */
    public void accountCreate() throws RuntimeException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IOException, ParseException, InvalidKeyException, InterruptedException {
        try {
            logic.accountRegister();
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 InvalidKeySpecException | BadPaddingException | InvalidKeyException | IOException | ParseException |
                 InterruptedException e) {
            throw new RuntimeException(e);
        }
        token = tokenRepository.findOneToken();
        logic.accountRegister();
    }

    public void accountCreate(AccountList list) throws RuntimeException {
        try {
            logic.accountRegister(list);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 InvalidKeySpecException | BadPaddingException | InvalidKeyException | IOException | ParseException |
                 InterruptedException e) {
            throw new ConcurrentModificationException("connect error");
        }
        /*token = tokenRepository.findOneToken();
        apiLogic.accountRegister(token.getAccessToken());*/
    }

    /**
     * connectedId 사용해서 계정추가
     */
    public void accountAdd(AccountAddList list) throws IOException, ParseException, InterruptedException {
        logic.accountAdd(list);
    }

    /**
     * 계정 삭제 - 성공
     */
    public void accountDelete(AccountDeleteDTO dto) throws IOException, ParseException, InterruptedException {
        logic.accountDelete(dto);
    }

    /**
     * 계정 수정
     */
    public void accountUpdate(AccountUpdateDTO dto) throws IOException, ParseException, InterruptedException {
        logic.accountUpdate(dto);
    }

    /**
     * 계정 목록
     */
    public void accountOutputList(ConcurrentHashMap<String,String> map) {
        logic.accountOutputList(map);
    }

    /**
     * 계정 추가 - 레퍼런스
     */
    public void accountReferenceAdd(AccountReferenceAddList list) throws IOException, ParseException, InterruptedException {
        logic.accountReferenceAdd(list);
    }
}
