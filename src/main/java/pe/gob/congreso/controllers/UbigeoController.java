package pe.gob.congreso.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.UbigeoMaestro;
import pe.gob.congreso.service.UbigeoService;

@Controller
@RequestMapping(Urls.Ubigeo.BASE)
public class UbigeoController {

    @Autowired
    UbigeoService ubigeoService;

    @RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
    public @ResponseBody
    UbigeoMaestro getUbigeo(@PathVariable("codigo") String codigo) throws Exception {
        return ubigeoService.getUbigeo(codigo);
    }

    @RequestMapping(value = "/departamentos", method = RequestMethod.GET)
    public @ResponseBody
    List getDepartamentos() throws Exception {
        return ubigeoService.getDepartamentos();
    }

    @RequestMapping(value = "departamentos/{dpto}/provincias", method = RequestMethod.GET)
    public @ResponseBody
    List getProvincias(@PathVariable("dpto") String dpto) throws Exception {
        return ubigeoService.getProvincias(dpto);
    }

    @RequestMapping(value = "/departamentos/{dpto}/provincias/{provincia}/distritos", method = RequestMethod.GET)
    public @ResponseBody
    List getDistritos(@PathVariable("dpto") String dpto, @PathVariable("provincia") String provincia) throws Exception {
        return ubigeoService.getDistritos(provincia);
    }

}
