
package pe.gob.congreso.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "TM51_ENTIDADEXTERNA")
@SequenceGenerator(name = "SEQ_TM51_ENTIDADEXTERNA", sequenceName = "SEQ_TM51_ENTIDADEXTERNA", allocationSize = 1, initialValue= 1)
public class EntidadExterna {
    
	@Id
    @Column(name = "tmee_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TM51_ENTIDADEXTERNA")
	@JsonProperty
    private Integer id;
    
    @Column(name = "tmu_ccodigo")
    @JsonProperty
    private String ubigeo;
      
    @Column(name = "tmee_cruc")
    @JsonProperty
    private String RUC;
    
    @Column(name = "tmee_vpoder")
    @JsonProperty
    private String poder;
    
    @Column(name = "tmee_vsector")
    @JsonProperty
    private String sector;
      
    @Column(name = "tmee_ventidad")
    @JsonProperty
    private String entidad;
    
    @Column(name = "tmee_vdireccion")
    @JsonProperty
    private String direccion;
      
    @Column(name = "tmee_vtelefono")
    @JsonProperty
    private String telefono;
    
    @Column(name = "tmee_vfax")
    @JsonProperty
    private String fax;
      
    @Column(name = "tmee_vpweb")
    @JsonProperty
    private String paginaWeb;
    
    @Column(name = "tmee_vnombrejefe")
    @JsonProperty
    private String nombreJefe;
    
    @Column(name = "tmee_vapellidojefe")
    @JsonProperty
    private String apellidoJefe;
    
    @Column(name = "tmee_vdnijefe")
    @JsonProperty
    private String DNI;
      
    @Column(name = "tmee_vcargojefe")
    @JsonProperty
    private String cargoJefe;
    
    @Column(name = "tmee_vtelefonojefe")
    @JsonProperty
    private String telefonoJefe;
      
    @Column(name = "tmee_vcorreojefe")
    @JsonProperty
    private String correoJefe;
    
    @Column(name = "tmee_bhabilitado")
    @JsonProperty
    private Boolean habilitado;
    
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
    
    @JsonIgnore
    @OneToMany(mappedBy = "entidadExterna")
    private List<EnviadoExterno> listEntidadExterna;  
   
    @JsonProperty
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea= fechaCrea;
    }    

    @JsonIgnore
    public String getFechaCrea() {
        String fecha = "";
        if(this.fechaCrea!=null){
        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaCrea);
        }       
        
        return fecha;
    }
    
    @JsonProperty
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica= fechaModifica;
    }    

    @JsonIgnore
    public String getFechaModifica() {
        String fecha = "";
        if(this.fechaModifica!=null){
        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaModifica);
        }       
        
        return fecha;
    }
    
    
}
