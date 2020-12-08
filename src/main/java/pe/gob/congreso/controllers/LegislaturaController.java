package pe.gob.congreso.controllers;

import com.google.common.base.Optional;
import java.util.List;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.congreso.model.Legislatura;
import pe.gob.congreso.service.LegislaturaService;

@Controller
@RequestMapping(Urls.Legislatura.BASE)
public class LegislaturaController {

    @Autowired
    LegislaturaService legislaturaService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getLegislaturas() throws Exception {
        return legislaturaService.findBy(Optional.fromNullable(""));
    }

    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    Legislatura getAnioLegislativoId(@PathVariable("codigo") String codigo) throws Exception {
        return legislaturaService.getLegislaturaId(codigo);
    }

    @RequestMapping(value = "/periodoLegislativo/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    List getAnioLegislativoPeriodoLegislativo(@PathVariable("codigo") String codigo) throws Exception {
        return legislaturaService.findBy(Optional.fromNullable(codigo));
    }

    @RequestMapping(value = "/actual", method = RequestMethod.GET)
    public @ResponseBody
    Legislatura getLegislaturaActual() throws Exception {
        return legislaturaService.getLegislaturaActual();
    }
}
