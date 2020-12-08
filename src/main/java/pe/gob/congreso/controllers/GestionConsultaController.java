package pe.gob.congreso.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.GrupoCentroCostoDao;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.service.FichaDocumentoService;
import pe.gob.congreso.service.FichaProveidoService;
import pe.gob.congreso.service.GestionConsultaService;
import pe.gob.congreso.service.GestionEnvioService;
import pe.gob.congreso.service.GrupoCentroCostoService;
import pe.gob.congreso.service.TipoService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.service.impl.Status;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.ContenedorPendientes;
import pe.gob.congreso.util.FichaConsultas;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Reporte;


@Controller
@RequestMapping(Urls.GestionConsulta.BASE)
public class GestionConsultaController {

	private static final boolean String = false;

	protected final Log log = LogFactory.getLog(getClass());
	
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
	GestionConsultaService gestionConsultaService;
    
    @Autowired
	GestionEnvioService gestionEnvioService;
    
    @Autowired
    TipoService tipoService;
    
    @Autowired
    FichaDocumentoService fichaDocumentoService;
	
    @Autowired
    FichaProveidoService fichaProveidoService;
    
    @Autowired
    DerivaService derivaService;
    
    @Autowired
    GrupoCentroCostoService grupoCentroCostoService;
    
    @Autowired
    EnvioMultipleService envioMultipleService;

	private List<java.lang.String> cc;
	
    
    @RequestMapping(value = "/consultaDocEnvio", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object consultaDocEnvio(HttpServletRequest request, @RequestBody FichaConsultas fc, BindingResult result, @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
        if (result.hasErrors()) {  
            Status status = new Status();
            status.getErrores(result);
            return status;
        }
    	Object datosSession;
		try {
			datosSession = usuarioService.getUsuarioInfo(request);
		
	    	if (datosSession instanceof Usuario) {
	    		return gestionConsultaService.getConsultaEnvios((Usuario) datosSession, fc, Optional.fromNullable(pag), Optional.fromNullable(pagLength));
	    	}	    	
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;

    }
    
	
	@RequestMapping(value = "/reporteConsulta/pdf", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody
    Object reporteConsultas(HttpServletRequest request, @QueryParam("indBusqueda") String indBusqueda, @QueryParam("numeroMp") String numeroMp, @QueryParam("tipoDoc") String tipoDoc, @QueryParam("numeroDoc") String numeroDoc, @QueryParam("id") String id, @QueryParam("sumilla") String sumilla, @QueryParam("fechaIni") String fechaIni, @QueryParam("fechaFin") String fechaFin, @QueryParam("grupoId") String grupoId, @QueryParam("dependenciaId") String dependenciaId, @QueryParam("empleadoId") String empleadoId, @QueryParam("registradoPor") String registradoPor, @QueryParam("remitidoPor") String remitidoPor, @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
		Object datosSession;
		try {
			datosSession = usuarioService.getUsuarioInfo(request);
		
	    	if (datosSession instanceof Usuario) {
				FichaConsultas fc = new FichaConsultas();
				fc.setIndBusqueda(indBusqueda);
			    fc.setNumeroMp(numeroMp);
			    fc.setTipoDoc(tipoDoc);
			    fc.setNumeroDoc(numeroDoc);
			    fc.setId(id);
			    fc.setSumilla(sumilla);
			    fc.setFechaIni(fechaIni);
			    fc.setFechaFin(fechaFin);
			    fc.setGrupoDestinoId(grupoId);
			    fc.setDependenciaDestinoId(dependenciaId);
			    fc.setEmpleadoDestinoId(empleadoId);
			    fc.setRegistradoPor(registradoPor);
			    fc.setRemitidoPor(remitidoPor);
			    Reporte cabReporte = new Reporte();
			    List<FichaPedientes> detReporte = (List<FichaPedientes>) gestionConsultaService.getConsultaEnvios((Usuario) datosSession, fc, Optional.fromNullable(pag), Optional.fromNullable(pagLength));
				return gestionConsultaService.getReporteConsultas(request, (Usuario) datosSession, detReporte, cabReporte);
	    	}	    	
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;
		
    }

	
    @RequestMapping(value = "/findSoloCasilleros", method = RequestMethod.GET)
    public @ResponseBody
    List getSoloCasillerosUtil(HttpServletRequest request) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	
    	List<InputSelectUtil> casillerosSelect = new ArrayList<>();
    	if (datosSession instanceof Usuario) {
    		Usuario usuario = (Usuario) datosSession;
    		if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
    				|| Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) ) {
	    		List<Tipo> casilleros = tipoService.findByOrden( Optional.fromNullable( "" ) , Optional.fromNullable( Constantes.TIPO_CASILLERO ) );
	        	for ( Tipo c : casilleros ) {
	        		InputSelectUtil i = new InputSelectUtil();
	        		i.setValue(c.getDescripcion().split("\\|")[0]);            	
	        		i.setLabel(c.getDescripcion().split("\\|")[1]);
	        		casillerosSelect.add(i);       		
	        	}
    		}
    	}
    	return casillerosSelect;
    }
    
    
    @RequestMapping(value = "/findCasilleros", method = RequestMethod.GET)
    public @ResponseBody
    List getCasillerosUtil(HttpServletRequest request) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	
    	List<InputSelectUtil> casillerosSelect = new ArrayList<>();
    	if (datosSession instanceof Usuario) {
    		Usuario usuario = (Usuario) datosSession;
    		if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
	    		|| Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) ) {
    			Map<String, Object> parametros = new HashMap<String, Object>();
    			StringBuffer estados = new StringBuffer();
    			estados.append(Constantes.ESTADO_ENVIADO).append(",").append(Constantes.ESTADO_LEIDO);
    			parametros.put("canalEnvio", Constantes.ENVIO_PARA_CASILLERO);
    	        parametros.put("habilitado", Integer.parseInt(Constantes.ESTADO_ACTIVO));
    	        parametros.put("estados", estados.toString());
    	        parametros.put("reporte", Constantes.FP_PENDIENTE_DE_REPORTE);
    	        
    			casillerosSelect = envioMultipleService.getCantidadCasillerosPendientes(parametros);
	    	}
    	}
    	return casillerosSelect;
    }
    
    private Integer cantidadCentroCosto(List<FichaPedientes> list, String centroCostoId) {
		Integer cont = 0;
    	for(FichaPedientes fp : list) {
			if ( fp.getCentroCostoDirigidosId().containsKey(centroCostoId) ) {
				cont++;
			}
		}
		return cont.intValue();
	}


	@RequestMapping(value = "/findCasilleros/id/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List getCasillero(HttpServletRequest request, @PathVariable("id")  String id) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	List<FichaPedientes> list = new ArrayList<>();
    	if (datosSession instanceof Usuario) {
    		Usuario usuario = (Usuario) datosSession;
    		if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
	    		|| Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) ) {
    			list = (List<FichaPedientes>) gestionEnvioService.getEnviosPendientes(usuario, Constantes.ENVIO_PARA_CASILLERO.toString(), "1", "10", id, Constantes.VACIO);
	    	}
	    }
    	return list;
    }
	
    private List<FichaPedientes> getListCasillerosById(List<FichaPedientes> list, String centroCostoId) {		
    	for (int i = list.size() - 1; i >= 0; i--) {
	    	FichaPedientes fp = list.get(i);
	    	if ( !fp.getCentroCostoDirigidosId().containsKey(centroCostoId) ) {
	    		fp.getCentroCostoDirigidosId().get(centroCostoId);
	    		list.remove(i);
			} else {
				fp.setEnviadoADes(fp.getCentroCostoDirigidosId().get(centroCostoId));
				fp.setNumDerivados(1);
				fp.setTotalDirigidosCanalIndEstafeta(1);
			}
	    }
 		return list;
	}
    
    
}
