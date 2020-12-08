
package pe.gob.congreso.model.util;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class JsonWeb {
    
    private Integer id;

    private String asunto;

    @Temporal(TemporalType.DATE)
    private Date fechaDocumento; 

    private String url;

    private Integer subCategoriaId;   

    private String subCategoriaDescripcion;

    private Integer tipoAdjunto;
}
