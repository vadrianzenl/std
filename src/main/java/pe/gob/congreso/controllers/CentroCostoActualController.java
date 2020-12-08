
package pe.gob.congreso.controllers;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.congreso.service.CentroCostoActualService;

@Controller
@RequestMapping(Urls.CentroCostoActual.BASE)
public class CentroCostoActualController {
    
    @Autowired
    CentroCostoActualService centroCostoActualService;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map getCentroCostosActuales(@QueryParam("descripcion") String descripcion,  @QueryParam("pag") String pag,  @QueryParam("pagLength") String pagLength) throws Exception {
        return centroCostoActualService.find(Optional.fromNullable(descripcion), Optional.fromNullable(pag), Optional.fromNullable(pagLength));
    }
    
    @RequestMapping(value="/inputSelect", method = RequestMethod.GET)
    public @ResponseBody
    List getCentroCostoActualInputSelect() throws Exception {
        return centroCostoActualService.getCentroCostoActualInputSelect();
    }
    
}
