
package pe.gob.congreso.model.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EnviadoUtil {
    
    private String empleadoDescripcion;
    
    private Integer empleadoId;

    private String centroCosto;
    
    private String centroCostoId;

    private Integer dirigido;
    
    private boolean ccopia;
    
    private boolean esResponsable;
}
