package pe.gob.congreso.util;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;

public class CookieHelper {
    
    /*
     *public static ResponseBuilder attachSession(ResponseBuilder responseBuilder, CookieSession session) {
     *    return responseBuilder
     *            .cookie(createCookie(CookieConstants.TOKEN_COOKIE_NAME, session.getToken()))
     *            .cookie(createCookie(CookieConstants.USER_COOKIE_NAME, session.getUsername()))
     *            .cookie(createCookie(CookieConstants.SESSION_COOKIE_NAME, session.getId()));
     *}
     *
     *public static ResponseBuilder removeSession(ResponseBuilder responseBuilder) {
     *    return responseBuilder
     *            .cookie(clearCookie(CookieConstants.TOKEN_COOKIE_NAME))
     *            .cookie(clearCookie(CookieConstants.USER_COOKIE_NAME))
     *            .cookie(clearCookie(CookieConstants.SESSION_COOKIE_NAME));
     *}
     */


    public static NewCookie createCookie(String name, String value, int maxAge){
        return new NewCookie(name, value, "/", "", "", maxAge, false);
    }
    
    public static NewCookie clearCookie(String name){
        return new NewCookie(name, "", "/", "", "", 0, false);
    }

}
