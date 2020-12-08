
package pe.gob.congreso.model.util;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.model.Tipo;

@NoArgsConstructor
@Data
public class FichaUtil {
    
    private Integer id;

    private Date fechaCrea;

    private String numeroDoc;

    private String tipoDocumento;

    private String tipoEstado;
    
    private String asunto;

    private String empleado;

    private String centroCosto;

    private List<EnviadoUtil> enviados;    
    
    private Boolean notas;
    
    private int recibiConforme;
    
    private SeguimientoFisico segFisico;

    public String getFechaCrea() {
        String fecha = "";
        if(this.fechaCrea!=null){
            fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaCrea);
        }       
        
        return fecha;
    }
    
    public String getFechaCreaFormatoMP() {
        String fecha = "";
        if(this.fechaCrea!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaCrea);
        }       
        
        return fecha;
    }
    
    private String remitente;
    
    private Tipo tipoRegistro;
    
    private Integer adjuntos;
    
    private String referencia;
    
    private Integer numeroFolios;
    
    private Integer numeroProveido;
    
    private String sumillaProveido;
    
    private String etapaMesaPartes;
    
    private String etapaDesMesaPartes;
    
    private String colorSemaforoMesaPartes;
    
}
