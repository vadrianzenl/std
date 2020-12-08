
package pe.gob.congreso.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_ENVIO_MULTIPLE")
@SequenceGenerator(name = "SEQ_STD_ENVIO_MULTIPLE", sequenceName = "SEQ_STD_ENVIO_MULTIPLE", allocationSize = 1, initialValue= 1)
public class EnvioMultiple implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "stdem_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_ENVIO_MULTIPLE")
	@JsonProperty
    private Integer id;
      
	@Column(name = "stdf_id")
    @JsonProperty
    private Integer fichaDocumentoId;    

    @Column(name = "stdp_id")
    @JsonProperty
    private Integer proveidoId;
    
    @Column(name = "stddem_ccanal_envio")
    @JsonProperty
    private String canalEnvio;
    
    @Column(name = "stddem_benvio_multiple")
    @JsonProperty
    private Boolean multiple;
   
    @Column(name = "stddem_idCentroCosto")
    @JsonProperty
    private String centroCostoId;

    @Column(name = "stddem_ctipo_reporte")
    @JsonProperty
    private String tipoReporte;
    
    @Column(name = "stddem_breporte")
    @JsonProperty
    private Boolean reporte;
    
    @Column(name = "stddem_cestado", nullable=true)
    @JsonProperty
    private String estado;
    
    @Column(name = "stddem_bhabilitado")
    @JsonProperty
    private Boolean habilitado; 
    
    @Column(name = "stddem_isubsanado")
    @JsonProperty
    private String subsanado;
    
    @Column(name = "stddem_gestionEnvioId")
    @JsonProperty
    private Integer gestionEnvioId;
    
    @Column(name="aud_cusuario_crea")
    @JsonProperty
    private String usuarioCrea;
    
    @Column(name = "aud_dfecha_crea")
    @JsonProperty
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    @JsonProperty
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    @JsonProperty
    private Date fechaModifica;
    
    @ManyToOne
    @JoinColumnsOrFormulas( {
        @JoinColumnOrFormula(column= @JoinColumn(name="stdf_id", referencedColumnName="stdf_id", insertable=false, updatable=false)),
        @JoinColumnOrFormula(column= @JoinColumn(name="stdp_id", referencedColumnName="stdp_id", insertable=false, updatable=false))
    })
    private FichaProveido fichaProveido;
    
    @Transient
    private Date fechaCreaFormatoMP;
    
    @Transient
    private Date fechaModificaFormatoMP;
    

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea= fechaCrea;
    }    

//    public String getFechaCrea() {
//        String fecha = "";
//        if(this.fechaCrea!=null){
//            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaCrea);
//        }       
//        
//        return fecha;
//    }
//    
//    public String getFechaModifica() {
//        String fecha = "";
//        if(this.fechaModifica!=null){
//            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaModifica);
//        }       
//        
//        return fecha;
//    }
    
    @JsonIgnore
    public void setFechaCreaFormatoMP(Date fechaCrea) {
        this.fechaCreaFormatoMP= fechaCrea;
    }   
    
    public String getFechaCreaFormatoMP() {
        String fecha = "";
        if(this.fechaCrea!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaCrea);
        }       
        
        return fecha;
    }
    
    @JsonIgnore
    public void setFechaModificaFormatoMP(Date fechaModifica) {
        this.fechaModificaFormatoMP= fechaModifica;
    }   
    
    public String getFechaModificaFormatoMP() {
        String fecha = "";
        if(this.fechaModifica!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaModifica);
        }       
        
        return fecha;
    }

}
