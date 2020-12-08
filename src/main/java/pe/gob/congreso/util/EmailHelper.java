package pe.gob.congreso.util;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;

public class EmailHelper {

    public void send(Email email, String user, String pass) {
        final String host = "mail.congreso.gob.pe";
        final int port = 25;
        final String username = user;
        final String password = pass;
        new Mailer(host, port, username, password).sendMail(email);
    }
}
