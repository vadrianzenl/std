package pe.gob.congreso.controllers;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.service.FiltroAnioService;
import pe.gob.congreso.service.impl.Status;

@Controller
@RequestMapping(Urls.FiltroAnio.BASE)
public class FiltroAnioController {
	
    @Autowired
    FiltroAnioService filtroAnioService;
    
    @RequestMapping(value="/findPermisos", method = RequestMethod.GET)
    public @ResponseBody
    Object getFiltroAnio(@QueryParam("centroCosto") String centroCosto, @QueryParam("tipoAnio") String tipoAnio) throws Exception {
        Object filtroAnio = filtroAnioService.buscarPermisos(centroCosto, Integer.parseInt(tipoAnio));
        if(Optional.fromNullable(filtroAnio).isPresent()){
        	return new Status(200,"OK",filtroAnio);
        }else
        	return new Status(400,"NO DATA OBJECT", null);
    }

}
