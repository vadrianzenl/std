package pe.gob.congreso.util;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;

@Data
public class SignIn {
    String usuario;
    String sessionKey;

    public void decodeAuthorizationKey(String authorizationKey) {
        if (!authorizationKey.equals("")) {
            byte[] decodedBase64 = Base64.decodeBase64(authorizationKey);
            String decodedKey = new String(decodedBase64);
            SignIn signIn = JsonUtils.parseJsonSignIn(decodedKey);
            this.setUsuario(signIn.getUsuario());
            this.setSessionKey(signIn.getSessionKey());
        } else {
            this.setUsuario("");
            this.setSessionKey("");
        }
    }
}
