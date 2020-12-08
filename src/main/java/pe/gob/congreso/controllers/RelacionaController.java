package pe.gob.congreso.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.Relaciona;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.BitacoraService;
import pe.gob.congreso.service.RelacionaService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.util.Constantes;

@Controller
@RequestMapping(Urls.Relaciona.BASE)
public class RelacionaController {

    @Autowired
    RelacionaService relacionaService;

    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    BitacoraService bitacoraService;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getRelacionados(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return relacionaService.findRelacionados(fichaDocumentoId);
    }

    @RequestMapping(value = "/asociados", method = RequestMethod.GET)
    public @ResponseBody
    List getAsociados(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return relacionaService.findAsociados(fichaDocumentoId);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List getAll(HttpServletRequest request,@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	List<FichaDocumento> ls = new  ArrayList<FichaDocumento>();
    	if (datosSession instanceof Usuario){
    		Usuario usuario = (Usuario) datosSession;
    		ls = relacionaService.findFichaRelacionados(fichaDocumentoId);
    		for(FichaDocumento fichaDocumento:ls){
    		   if (fichaDocumento.getCentroCosto().getId().trim().equals(usuario.getEmpleado().getCentroCosto().getId().trim())) {
    			   fichaDocumento.setEsMiRecibido(true);
    			   continue;
    		   }else{
    			   fichaDocumento.setEsMiRecibido(false);   
    		   }
    		   if(!Constantes.VACIO.equals(fichaDocumento.getListDirigidos()) && fichaDocumento.getListDirigidos()!=null  ){
    			  if(fichaDocumento.getListDirigidos().contains(usuario.getEmpleado().getCentroCosto().getId().trim())){
        			 fichaDocumento.setEsMiRecibido(true); 
        			 continue;
        		  }else{
        			  fichaDocumento.setEsMiRecibido(false); 
        		  }
    		   } else{
    			   fichaDocumento.setEsMiRecibido(false);
    		   }
    		   
    		   //bitacoras
    		   List<Bitacora>  lsBitacora = bitacoraService.findBitacoras(fichaDocumento.getId().toString());
    		   for(Bitacora bi:lsBitacora){
    			   if(!Constantes.VACIO.equals(bi.getListDeriva()) && bi.getListDeriva()!=null  ){
    	    			  if(bi.getListDeriva().contains(usuario.getEmpleado().getCentroCosto().getId().trim())){
    				   	  //if(bi.getListDeriva().contains(fichaDocumento.getCentroCostoId().toString().trim())){
    	        			 fichaDocumento.setEsMiRecibido(true); 
    	        			 continue;
    	        		  }else{
    	        			  fichaDocumento.setEsMiRecibido(false); 
    	        		  }
    	    		   } else{
    	    			   fichaDocumento.setEsMiRecibido(false);
    	    		   }
    		   }
    		}
    		return ls;
    		
    	}else{
    		return ls;
    	}
    	
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createRelacionado(HttpServletRequest request,@RequestBody Relaciona a, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return relacionaService.create((Usuario) datosSession, a, operacion);
    	else
    		return datosSession;
    }
    
    @RequestMapping(value = "/respuesta", method = RequestMethod.GET)
    public @ResponseBody
    List getRespuestaA(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return relacionaService.findRespuestaA(fichaDocumentoId);
    }
    
    @RequestMapping(value = "/referencia", method = RequestMethod.GET)
    public @ResponseBody
    List getReferencia(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return relacionaService.findReferencia(fichaDocumentoId);
    }
    
    @RequestMapping(value = "/reporte/relacionados/pdf/ru/{ru}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody
    Object reporteRelacionados(HttpServletRequest request,@PathVariable("ru") String ru) throws Exception { 	
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)    		
    		return relacionaService.reporteRelacionados(request, (Usuario) datosSession, ru);
    	else
    		return datosSession;
    }
    
}
