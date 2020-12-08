package pe.gob.congreso.util;

import lombok.Data;

@Data
public class FichaConsultas {

    private String indBusqueda;
    private String numeroMp;
    private String tipoDoc;
    private String numeroDoc;
    private String id;
    private String sumilla;
    private String fechaIni;
    private String fechaFin;
    private String dependenciaOrigenId;
    private String empleadoOrigenId;
    private String grupoDestinoId;
    private String dependenciaDestinoId;
    private String empleadoDestinoId;
    private String registradoPor;
    private String remitidoPor;
    private String tipoRegistro; //DOCUMENTO ENVIADO: 1, DOCUMENTO RECIBIDO:2
    private String tipoEstado;
    
}