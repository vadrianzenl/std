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
import pe.gob.congreso.model.Proveido;
import pe.gob.congreso.service.ProveidoService;

@Controller
@RequestMapping(Urls.Proveido.BASE)
public class ProveidoController {

    @Autowired
    ProveidoService proveidoService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getProveidos() throws Exception {
        return proveidoService.findBy();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Proveido getProveidoId(@PathVariable("id") Integer id) throws Exception {
        return proveidoService.getProveidoId(id);
    }

    @RequestMapping(value = "/centroCosto/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Proveido getProveidoCentroCosto(@PathVariable("id") String id) throws Exception {
        return proveidoService.getProveidoCentroCosto(id);
    }
}
