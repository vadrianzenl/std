
package pe.gob.congreso.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_GRUPO_CENTRO_COSTO")
public class GrupoCentroCosto {
    @Id
    @Column(name = "stdcc_id")
    private String id;

    @Column(name = "stdcc_cdescripcion") 
    private String descripcion;

    @Column(name = "stdcc_vcargo") 
    private String cargo; 

    @ManyToOne
    @JoinColumn(name="stdg_id", nullable=false)    
    private Grupo grupo; 

    @ManyToOne(optional=true)
    @JoinColumn(name="stdcc_iresponsable", nullable=true)    
    private Empleado empleado;

    @Column(name = "stdcc_csigla")
    private String sigla;

    @Column(name = "stdcc_bhabilitado")
    private boolean habilitado;
    
    @Column(name="aud_cusuario_crea")    
    private String usuarioCrea;     
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;

    @Column(name = "stdcc_cconector")
    private String conector;

    @Column(name = "stdcc_bencargatura")
    private Boolean stdcc_bencargatura;

    @ManyToOne
    @JoinColumn(name="stdca_id", nullable=false)
    private Cargo cargoResponsable;

    @JsonProperty
    @Transient
    private Boolean indCanal;
    
    @JsonProperty
    @Transient
    private Boolean indCambio;
}
