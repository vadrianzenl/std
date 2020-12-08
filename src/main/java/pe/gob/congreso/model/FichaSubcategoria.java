
package pe.gob.congreso.model;

import java.util.Date;

import java.io.Serializable;


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

import java.text.SimpleDateFormat;

@NoArgsConstructor
@Data
@Entity
@IdClass(value = FichaSubcategoria.FichaSubcategoriaPK.class)
@Table(name = "STD_FICHA_SUBCATEGORIA")
public class FichaSubcategoria {
    @Id
    private Integer fichaDocumentoId;

    @Id
    private Integer subCategoriaId;

    @Column(name = "stdfs_bhabilitado")
    private boolean habilitado;

    @ManyToOne
    @JoinColumn(name="aud_cusuario_crea", nullable=true)    
    private Usuario usuarioCrea;     
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
    
    @MapsId("fichaDocumentoId")
    @ManyToOne
    @JoinColumn(name="stdf_id", nullable=true)
    private FichaDocumento fichaDocumento;

    @MapsId("subCategoriaId")
    @ManyToOne
    @JoinColumn(name="stdsu_id", nullable=true)
    private SubCategoria subCategoria;

    @Data
    static class FichaSubcategoriaPK implements Serializable{
        @Column(name="stdf_id")
        private Integer fichaDocumentoId;
        @Column(name="stdsu_id")
        private Integer subCategoriaId;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea= fechaCrea;
    }    

    public String getFechaCrea() {
        String fecha = "";
        if(this.fechaCrea!=null){
            fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaCrea);
        }       
        
        return fecha;
    }
    
}
