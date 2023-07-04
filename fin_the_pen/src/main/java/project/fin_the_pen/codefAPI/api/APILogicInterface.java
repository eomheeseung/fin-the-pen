package project.fin_the_pen.codefAPI.api;

import project.fin_the_pen.codefAPI.dto.IntegratedDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

public interface APILogicInterface {
    HashMap<String, Object> registerMap(IntegratedDTO dto, HashMap<String, Object> registerMap) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException;
}
