package pe.gob.congreso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.service.SistemaOpcionUsuarioService;

@Controller
@RequestMapping(Urls.SistemaOpcionUsuario.BASE)
public class SistemaOpcionUsuarioController {
	
	@Autowired
	SistemaOpcionUsuarioService sistemaOpcionUsuarioService;

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getDerivado(@PathVariable("id") String id) throws Exception {
        return sistemaOpcionUsuarioService.findByEmpleadoId(Optional.fromNullable(id));
    }
}
