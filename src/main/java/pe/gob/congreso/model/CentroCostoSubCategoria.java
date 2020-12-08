
package pe.gob.congreso.model;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.IdClass;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@IdClass(value = CentroCostoSubCategoria.CentroCostoSubCategoriaPK.class)
@Table(name = "STD_CENTRO_COSTO_SUBCATEGORIA")
public class CentroCostoSubCategoria {
    @Id
    private String centroCostoId;

    @Id
    private Integer subCategoriaId;

    @Column(name = "stdccc_bpublicar") 
    private Boolean publico;

    @Column(name = "stdccc_bhabilitado") 
    private Boolean habilitado;
    
    @Column(name="aud_cusuario_crea")    
    private String usuarioCrea;    
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;

    @MapsId("centroCostoId")
    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)
    private CentroCosto centroCosto;

    @MapsId("subCategoriaId")
    @ManyToOne
    @JoinColumn(name="stdsu_id", nullable=true)
    private SubCategoria subCategoria;
    
    @Data
    static class CentroCostoSubCategoriaPK implements Serializable{
        @Column(name="stdcc_id")
        private String centroCostoId;
        @Column(name="stdsu_id")
        private Integer subCategoriaId;
    }

    @JsonProperty
    public void setCentroCosto(CentroCosto cc) {
        this.centroCosto= cc;
    }
    @JsonIgnore
    public CentroCosto getCentroCosto() {
        return centroCosto;
    }
}
