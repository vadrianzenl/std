
package pe.gob.congreso.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.AnioLegislativo;
import pe.gob.congreso.service.AnioLegislativoService;

@Controller
@RequestMapping(Urls.AnioLegislativo.BASE)
public class AnioLegislativoController {
    @Autowired
    AnioLegislativoService anioLegislativoService;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getAnioLegislativos() throws Exception {
        return anioLegislativoService.findBy();
    }
    
    @RequestMapping(value="/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAnioLegislativoId(@PathVariable("codigo") String codigo) throws Exception {
    	//System.out.println("AEP--AnioLegislativoController.getAnioLegislativoId(codigo="+codigo+")");
        return anioLegislativoService.getAnioLegislativoId(codigo);
    }
    
    @RequestMapping(value="/periodoLegislativo/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAnioLegislativoPeriodoLegislativo(@PathVariable("codigo") String Id) throws Exception {
        return anioLegislativoService.getAnioLegislativoPeriodoLegislativo(Id);
    }

    @RequestMapping(value="/actual", method = RequestMethod.GET)
    public @ResponseBody
    Object getAnioLegislativoActual() throws Exception {
        return anioLegislativoService.getAnioActual();
    }
    
    @RequestMapping(value="/periodoActual", method = RequestMethod.GET)
    public @ResponseBody
    Object getAniosPeriodoActual() throws Exception {
    	AnioLegislativo anio = (AnioLegislativo) anioLegislativoService.getAnioActual();
    	Object aniosPeriodoActual = anioLegislativoService.getAnioLegislativoPeriodoLegislativo(String.valueOf(anio.getPeriodoLegislativo().getCodigo()));
    	return aniosPeriodoActual;
    } 
}
