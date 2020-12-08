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
import pe.gob.congreso.model.PeriodoLegislativo;
import pe.gob.congreso.service.PeriodoLegislativoService;

@Controller
@RequestMapping(Urls.PeriodoLegislativo.BASE)
public class PeriodoLegislativoController {

    @Autowired
    PeriodoLegislativoService periodoLegislativoService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getPeriodoLegislativos() throws Exception {
        return periodoLegislativoService.findBy();
    }

    @RequestMapping(value="/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    PeriodoLegislativo getperiodoLegislativoId(@PathVariable("codigo") String codigo) throws Exception {
        return periodoLegislativoService.getPeriodoLegislativoId(codigo);
    }

}
