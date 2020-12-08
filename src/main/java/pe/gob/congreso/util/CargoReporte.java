package pe.gob.congreso.util;

import lombok.Data;


@Data
public class CargoReporte {

	 private Integer id;
	 private String nombreArchivo;
	 private String descripcion; 
	 private String fechaAdjunto;
	 private Boolean derivado;
	 private String estado;
	 private Boolean habilitado;
	 private Boolean publico;
	 private Boolean subsanado;
	 private String usuarioCrea;
	 private String desUsuarioCrea;
	 private String fechaCrea;
	 private String usuarioModifica;
	 private String fechaModifica; 
	 
	 
}
