
package pe.gob.congreso.controllers;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.congreso.service.CentroCostoService;

@Controller
@RequestMapping(Urls.CentroCosto.BASE)
public class CentroCostoController {
    
    @Autowired
    CentroCostoService centroCostoService;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map getCentroCostos(@QueryParam("descripcion") String descripcion,  @QueryParam("pag") String pag,  @QueryParam("pagLength") String pagLength) throws Exception {
        return centroCostoService.findBy(Optional.fromNullable(descripcion), Optional.fromNullable(pag), Optional.fromNullable(pagLength));
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getCentroCostoId(@PathVariable("id") String id) throws Exception {
        return centroCostoService.getCentroCostoId(id);
    }
    
    @RequestMapping(value="/grupo/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getCentroCostoGrupo(@PathVariable("id") String id) throws Exception {
        return centroCostoService.getGrupoId(id);
    }
    
    @RequestMapping(value="/inputSelect", method = RequestMethod.GET)
    public @ResponseBody
    List getCentrosCostoInputSelect() throws Exception {
        return centroCostoService.getCentrosCostoInputSelect();
    }
}
