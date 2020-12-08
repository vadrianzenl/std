package pe.gob.congreso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.service.ControlFirmaService;

@Controller
@RequestMapping(Urls.ControlFirma.BASE)
public class ControlFirmaController {

	@Autowired
    ControlFirmaService controlFirmaService;
	
	@RequestMapping(value = "/{idempleado}", method = RequestMethod.GET)
    public @ResponseBody Object getEmpleadoFirma(@PathVariable("idempleado") String idEmpleado) throws Exception {
		//System.out.println("AEP-->ControlFirmaController.getEmpleadoFirma(idempleado='" + idEmpleado + "')");
        return controlFirmaService.findByIdEmpleado( idEmpleado );
    }
	
}
