/**
 *
 * @author dveliz
 *
 */

package pe.gob.congreso.controllers;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.congreso.util.ResponseJson;

@RestController
@RequestMapping(Urls.ROOT)
public class Home {
  @Autowired
  private Environment environment;

  @RequestMapping(value = "/test")
  @ResponseBody
  String home(HttpServletResponse response) {

    return "hellow word";
  }

  @RequestMapping(value = Urls.VERSION, method = { RequestMethod.GET })
  public ResponseJson version(HttpServletRequest request) {
    ResponseJson rs = new ResponseJson();
    Integer anio = new DateTime().getYear();
    String anio_distr = "2017 - " + (anio);
    Map<String, String> ret = new TreeMap<>();
    ret.put("version", environment.getProperty("app.version"));
    ret.put("anio_distr", anio_distr);
    rs.setSuccess(Boolean.TRUE);
    rs.setData(ret);
    return rs;
  }

}
