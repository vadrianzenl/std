package pe.gob.congreso.util;

import java.util.Date;

import lombok.Data;
@Data
public class SeguimientoFisicoUtil {

	private Integer derivaId;
	private String dependencia;
	private String dependenciaId;
	private boolean recibiConforme;
	private String fechaRecepcion;
	private Date fechaRecibiConforme;
	private String codUsuario;
	private String desUsuario;
	private String codigoEstado;
	private String estado;
	
	
	}
