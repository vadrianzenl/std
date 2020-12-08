
package pe.gob.congreso.util;

import java.util.Map;

import lombok.Data;

@Data
public class FichaPedientes {

    private Integer numeroMp;
    private String asuntoMp;
    private Integer id;
    private String fechaCrea;
    private String numeroDoc;
    private String tipoDoc;//Add
    private Integer numeroFolios;
    private String remitidoPor;//Add
    private String enviadoADes;
    private String registradoPorDes;
    private Boolean seleccion;
    private Integer numDerivados;
    private Boolean indEnvioMultiple;
    private Map<String, String> centroCostoDirigidosId;
    private Integer totalDirigidos;
    private Integer totalDirigidosCanalIndEstafeta;
    private String etapaMesaPartes;  //Add  
    private String etapaDesMesaPartes; //Add   
    private String colorSemaforoMesaPartes; //Add
    private String fechaEnvioReporte; //Add
    private Integer total; //Add
    private String estafeta; //Add
    private String tipoRegistro; //Add
    private String empleadoId; //Add
    private String centroCosto; //Add
    private String centroCostoId; //Add    
    private String tipoEstado; //Add
    private String enviadoA; //Add
    
        
}