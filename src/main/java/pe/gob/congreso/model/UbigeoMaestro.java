
package pe.gob.congreso.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "TM50_UBIGEO")
public class UbigeoMaestro {

	@Id
    @Column(name = "tmu_ccodigo")	
    private String codigo;
      
    @Column(name = "tmu_vdescripcion")
    private String descripcion;
    
    @Column(name = "tmu_vdpto")
    private String dpto;
    
    @Column(name = "tmu_vprov")
    private String prov;
    
    @Column(name = "tmu_vdist")
    private String dist;
    
    @Column(name = "tmu_vregion")
    private String region;
    
    @Column(name = "tmu_vsubregion")
    private String subregion;
    
    @Column(name = "tmu_fcambio")
    private Date fechaCambio;
    
    @Column(name = "tmu_vmotivocambio")
    private String motivoCambio;
    
    @Column(name = "tmu_finivig")
    private Date fechaIniVig;
    
    @Column(name = "tmu_ffinvig")
    private Date fechaFinVig;
    
    @Column(name = "aud_cusuario_crea")
    private String usuarioCrea;
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
    
    @JsonIgnore
    @OneToMany(mappedBy = "ubigeo")   
    private List<EnviadoExterno> listEnviadosExternos;
   
    
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }    

    public String getFechaCambio() {
        String fecha = "";
        if(this.fechaCambio!=null){
        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaCambio);
        }       
        
        return fecha;
    }
    
    public void setFechaIniVig(Date fechaIniVig) {
        this.fechaIniVig = fechaIniVig;
    }    

    public String getFechaIniVig() {
        String fecha = "";
        if(this.fechaIniVig!=null){
        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaIniVig);
        }       
        
        return fecha;
    }
    
    public void setFechaFinVig(Date fechaFinVig) {
        this.fechaFinVig = fechaFinVig;
    }    

    public String getFechaFinVig() {
        String fecha = "";
        if(this.fechaFinVig!=null){
        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaFinVig);
        }       
        
        return fecha;
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
    
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica= fechaModifica;
    }    

    public String getFechaModifica() {
        String fecha = "";
        if(this.fechaModifica!=null){
        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaModifica);
        }       
        
        return fecha;
    }
    
    
}
