package pe.gob.congreso.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.util.FichaConsultas;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Reporte;

public interface GestionConsultaService {

	public Object getConsultaEnvios(Usuario datosSession, FichaConsultas fc, Optional<String> pag, Optional<String> pagLength) throws Exception;
	
	public Object getReporteConsultas(HttpServletRequest request, Usuario usuario, List<FichaPedientes> detReporte, Reporte cabReporte) throws Exception;
	
	public String getEnviados(List<EnviadoUtil> enviados) throws Exception;

}
