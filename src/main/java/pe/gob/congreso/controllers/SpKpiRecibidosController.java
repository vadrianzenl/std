package pe.gob.congreso.controllers;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.congreso.service.SpKpiRecibidosService;

@Controller
@RequestMapping(Urls.Kpis.BASE)
public class SpKpiRecibidosController {

    private final Logger log = LoggerFactory.getLogger(SpKpiRecibidosController.class);

    @Autowired
    SpKpiRecibidosService spKpiRecibidosService;

    @RequestMapping(value = "/recibidos", method = RequestMethod.GET)
    public @ResponseBody Object registro(@QueryParam("idempleado") String idempleado,
            @QueryParam("ccosto") String ccosto, @QueryParam("fecha") String fecha) throws Exception {
        log.info("getKpiRecibidos {" + idempleado + "**" + ccosto + "**" + fecha + "}");
        return spKpiRecibidosService.getKpiRecibidos(idempleado, ccosto, fecha);
    }

}
