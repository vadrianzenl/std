package pe.gob.congreso.model.util;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EstadoDeriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.MotivoDeriva;
import pe.gob.congreso.model.Tipo;

@NoArgsConstructor
@Data
public class SeguimientoUtil {
	String centroCosto;
	String fechaDocumento;
	String estado;
	String receptores;
	String observaciones;
	Integer nivel;
}
