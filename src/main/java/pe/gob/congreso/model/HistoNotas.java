
package pe.gob.congreso.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_HISTONOTAS")
@SequenceGenerator(name = "SEQ_STD_HISTONOTAS", sequenceName = "SEQ_STD_HISTONOTAS", allocationSize = 1, initialValue= 1)
public class HistoNotas {
    
	@Id
    @Column(name = "stdhn_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_HISTONOTAS") 
	@JsonProperty
    private Integer id;
	
    @Column(name = "stdn_id")
	@JsonProperty
    private Integer notasId;
    
    @Column(name = "stdf_id")
    @JsonProperty
    private Integer fichaId;
    
    @Column(name = "stdcc_id")
    @JsonProperty
    private Integer centroCostoId;
    
    @Column(name = "stde_id")
    @JsonProperty
    private Integer empleadoId;
    
    @Column(name = "stdn_vdescripcion")
    @JsonProperty
    private String descripcion;
    
    @Column(name = "stdn_cestado")
    @JsonProperty
    private String estado;
    
    @Column(name = "stdn_prioridad")
    @JsonProperty
    private Integer prioridad;
    
    @Column(name = "aud_cusuario_crea")
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
    
    @Transient
    private String usuarioDescripcion;
    
    @Transient
    private String fechaCreaFormato;
    
    @Transient
    private String fechaModificaFormato;
    
    @Transient
    private String indVis;
    
    @Transient
    private String indEdi;
        
    public void setFechaCrea(Date fecha) {
        this.fechaCrea= fecha;
    }

    public String getFechaCreaFormato() {
        String fecha = "";
        String hora = "";
        if(this.fechaCrea!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(this.fechaCrea);
            this.fechaCreaFormato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(this.fechaCrea);
        }       
        
        return fecha;
    }
    
    public void setFechaModifica(Date fecha) {
        this.fechaModifica= fecha;
    }    

    public String getFechaModifica() {
        String fecha = "";
        String hora = "";
        if(this.fechaModifica!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(this.fechaModifica);
            this.fechaModificaFormato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(this.fechaModifica);
        }       
        
        return fecha;
    }
    
}
