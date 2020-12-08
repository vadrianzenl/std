package pe.gob.congreso.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.FisicoService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Fisico.BASE)
public class FisicoController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	DerivaService derivaService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	FisicoService fisicoService;

	@RequestMapping(value = "/recibiConforme", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object recibiConforme(HttpServletRequest request, @RequestBody SeguimientoFisico fisico,
			@QueryParam("operacion") String operacion, @QueryParam("motivo") String motivo, @QueryParam("tipo") String tipo)
			throws Exception {
		// log.info("/recibiConforme -> OPERACION: " + operacion + ", MOTIVO: " + motivo
		// + ", TIPO: " + tipo);
		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.recibiConforme((Usuario) datosSession, fisico, operacion, motivo, tipo);
		else
			return datosSession;
	}

	@RequestMapping(value = "/updateRecibiConforme", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object updateRecibiConforme(HttpServletRequest request, @RequestBody SeguimientoFisico fisico,
			@QueryParam("operacion") String operacion, @QueryParam("motivo") String motivo, @QueryParam("tipo") String tipo,
			@QueryParam("fichaId") String fichaId) throws Exception {

		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.updateRecibiConforme((Usuario) datosSession, fisico, operacion, motivo, tipo, fichaId);
		else
			return datosSession;
	}

	@RequestMapping(value = "/updateRecibiConformeBitacora", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Object updateRecibiConformeBitacora(HttpServletRequest request,
			@RequestBody SeguimientoFisico fisico, @QueryParam("operacion") String operacion,
			@QueryParam("motivo") String motivo, @QueryParam("tipo") String tipo, @QueryParam("fichaId") String fichaId)
			throws Exception {

		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.updateRecibiConformeBitacora((Usuario) datosSession, fisico, operacion, motivo, tipo,
					fichaId);
		else
			return datosSession;
	}

	@RequestMapping(value = "/validaRecibiConforme", method = RequestMethod.GET)
	public @ResponseBody Object validaRecibiConforme(HttpServletRequest request, @QueryParam("derivaId") String derivaId,
			@QueryParam("estado") String estado) throws Exception {

		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.findRecibiFisico((Usuario) datosSession, derivaId, estado);
		else
			return datosSession;
	}

	@RequestMapping(value = "/getSeguimientoFisico", method = RequestMethod.GET)
	public @ResponseBody Object getSeguimientoFisico(HttpServletRequest request,
			@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {

		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.findDirigidosByFichaId(fichaDocumentoId);
		else
			return datosSession;
	}

	@RequestMapping(value = "/validaRecibiConformeFichaDocId", method = RequestMethod.GET)
	public @ResponseBody Object validaRecibiConformeFichaDocId(HttpServletRequest request,
			@QueryParam("fichaDocumentoId") String fichaDocumentoId, @QueryParam("estado") String estado) throws Exception {

		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.findRecibiFisicoFichaId((Usuario) datosSession, fichaDocumentoId, estado);
		else
			return datosSession;
	}

	@RequestMapping(value = "/getFisico", method = RequestMethod.GET)
	public @ResponseBody Object getFisico(HttpServletRequest request,
			@QueryParam("fichaDocumentoId") String fichaDocumentoId, @QueryParam("empleadoId") String empleadoId)
			throws Exception {

		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.findRecibiFisicoFichaIdEmpleadoId((Usuario) datosSession, fichaDocumentoId, empleadoId);
		else
			return datosSession;
	}

	@RequestMapping(value = "/getRecibiFisicoDerivaId", method = RequestMethod.GET)
	public @ResponseBody Object getRecibiFisicoDerivaId(HttpServletRequest request,
			@QueryParam("derivaId") String derivaId) throws Exception {

		Object datosSession = usuarioService.getUsuarioInfo(request);
		if (datosSession instanceof Usuario)
			return fisicoService.findRecibiFisicoById((Usuario) datosSession, derivaId);
		else
			return datosSession;
	}

}
