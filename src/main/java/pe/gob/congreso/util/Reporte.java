package pe.gob.congreso.util;

import lombok.Data;

@Data
public class Reporte {

    private Integer id;
    private String fechaGenera;
    private Integer numRegistros;
    private Integer numDocFisicos;
    private String registradoPor;
    private String estadoReporte;
    private String desEstadoReporte;
    private Boolean indCargo;
    private CargoReporte cargoReporte;
    private Integer cantidadFisicos;
    private String tipoReporte;
    private String grupoEnvio;
    private String tituloReporte;
    private String centroCostoDes;
    private String totalRegistros;
    private String totalDocFisicos;
}
