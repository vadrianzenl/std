package pe.gob.congreso.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.DerivaUtil;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.service.FichaDocumentoService;
import pe.gob.congreso.service.FichaProveidoService;
import pe.gob.congreso.service.ProveidoService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.service.impl.Status;
import pe.gob.congreso.util.Constantes;

@Controller
@RequestMapping(Urls.FichaProveido.BASE)
public class FichaProveidoController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	
    @Autowired
    FichaProveidoService fichaProveidoService;
	
    @Autowired
    FichaDocumentoService fichaDocumentoService;
    
    @Autowired
    UsuarioService usuarioService;
	
	@Autowired
	ProveidoService proveidoService;
	
	@Autowired
	DerivaService derivaService;
	
	@Autowired
	EnvioMultipleService envioMultipleService;
	
	
	@RequestMapping(value = "/fichaProveido", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Object createFichaProveidoMp(HttpServletRequest request,@RequestBody @Valid FichaProveido fp, BindingResult result, @QueryParam("cCostoId") String cCostoId, @QueryParam("operacion") String operacion) throws Exception {
       if (result.hasErrors()) {
            Status status = new Status();
            status.getErrores(result);	
            return status;
       }
       Object datosSession = usuarioService.getUsuarioInfo(request);
       if (datosSession instanceof Usuario) {
    	   Usuario usuario = (Usuario) datosSession;
    	   Map mp = new HashMap<>();
	       mp.put("fichaProveido", fp);
    	   if (!Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
		    		&& !Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) ) {
    		   return mp;
	       }
    	   fp.setProveidoId(Constantes.ID_PROVEIDO_MESA_DE_PARTES);
	       List<FichaProveido> list = fichaProveidoService.getByIdRU(Optional.fromNullable(fp.getFichaDocumentoId().toString()), Optional.fromNullable(Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString()));	       
	       if ( list != null && !list.isEmpty() ) {
	    	   FichaProveido fichaExistente = list.get(0);
	    	   if ( !fp.getNumero().equals(fichaExistente.getNumero()) || ( fp.getSumilla() != null && fichaExistente.getSumilla() != null && !fp.getSumilla().trim().equals(fichaExistente.getSumilla().trim())) ) {
	    		   fichaExistente.setSumilla(fp.getSumilla());
		    	   fichaExistente.setNumero(fp.getNumero());
		    	   fichaExistente.setPpinicio(fp.getPpinicio());
		    	   fichaExistente.setPpfin(fp.getPpfin());
		    	   fp = (FichaProveido) fichaProveidoService.createFichaProveido(usuario, fichaExistente, Constantes.OPERACION_ACTUALIZAR);
	    	   }	    	   
	       } else {
	    	   fp = (FichaProveido) fichaProveidoService.createFichaProveido(usuario, fp, Constantes.OPERACION_CREAR);  
	       }
	       mp.put("fichaProveido", fp);
	       return mp;
       } else {
    		return 	datosSession;
       }
	         
	}
	
	@RequestMapping(value = "/getEtapaFichaProveido", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Object getEtapaFichaProveido(HttpServletRequest request,@RequestBody @Valid FichaProveido fp, BindingResult result, @QueryParam("cCostoId") String cCostoId, @QueryParam("operacion") String operacion) throws Exception {
       if (result.hasErrors()) {
            Status status = new Status();
            status.getErrores(result);	
            return status;
       }
       Object datosSession = usuarioService.getUsuarioInfo(request);
       if (datosSession instanceof Usuario) {
    	   Usuario usuario = (Usuario) datosSession;
    	   if (!Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
		    		&& !Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) ) {
    		   return null;
	       }
    	   fp.setProveidoId(Constantes.ID_PROVEIDO_MESA_DE_PARTES);
	       List<FichaProveido> list = fichaProveidoService.getByIdRU(Optional.fromNullable(fp.getFichaDocumentoId().toString()), Optional.fromNullable(Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString()));
	       FichaProveido fichaExistente = new FichaProveido();
	       Map mp = new HashMap<>();
	       DerivaUtil doc = new DerivaUtil();
	       if ( list != null && !list.isEmpty() ) {
	    	   fichaExistente = list.get(0);
	    	   doc = getEstadosMp(fichaExistente.getFichaDocumentoId(), fichaExistente.getFechaCrea());
	       } else {
	    	   doc.setEtapaMesaPartes(Constantes.ETAPA_REGISTRADO);
	    	   doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO.get(Constantes.ETAPA_REGISTRADO));	    	   
	       }
	       mp.put("Etapa", doc);
	       return mp;
       } else {
    		return 	datosSession;
       }
	         
	}
	
	public DerivaUtil getEstadosMp(Integer idFicha, Date fechaCrea) throws Exception{
	   DerivaUtil derivaUtil = new DerivaUtil();
	   if (fechaCrea ==  null) {
		   return derivaUtil;
	   }
       FichaDocumento fichaDocumento = new FichaDocumento();
       fichaDocumento.setId(idFicha);
       fichaDocumento.setFechaCrea(fechaCrea);
       List<Deriva> listDeriva = new ArrayList<Deriva>();
       listDeriva = derivaService.findDerivados(fichaDocumento.getId().toString());
       fichaDocumento.setListDeriva(listDeriva);
       derivaUtil.setFichaDocumento(fichaDocumento);
       return derivaService.asignarEtapaIndicadorMesaPartes(derivaUtil);
	}
	
}