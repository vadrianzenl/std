
package pe.gob.congreso.model.util;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class SearchUtil {
    
    private String id;      
    private String descripcion;
    public String getDescripcion(){
    	return this.descripcion.trim();
    }    
}
