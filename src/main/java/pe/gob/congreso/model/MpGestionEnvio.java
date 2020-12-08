
package pe.gob.congreso.model;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapea la estructura de la tabla STD_DET_GES_ENVIO  
 * @author sperezr
 *
 */
@NoArgsConstructor
@Data
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "STD_GESTION_ENVIO")
@SequenceGenerator(name = "SEQ_STD_GESTION_ENVIO", sequenceName = "SEQ_STD_GESTION_ENVIO", allocationSize = 1, initialValue = 1 )
public class MpGestionEnvio implements Serializable, Comparable<Object> {

	/**
	 * Serial para guardar los estados de los atributos del objeto
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "stdge_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STD_GESTION_ENVIO")
	private Integer id;
	
	@Column(name = "stdge_dfecha_generacion")
	private Date fechaGeneracion;
	
	@Column(name = "stdge_cusuario_generacion")
	private String usuarioGeneracion;
	
	@Column(name = "stdge_isubsanado_reporte")
	private Boolean subsanado;
	
	@Column(name = "stdge_dfecha_subsanado")
	private Date fechaSubsanado;
	
	@Column(name = "stdge_cantidad_registros")
	private Integer cantidadRegistros;
	
	@Column(name = "stdge_cantidad_fisicos")
	private Integer cantidadFisicos;
	
	@Column(name = "stdge_ctipo_reporte")
	private String tipoReporte;
	
	@Column(name = "stdge_grupo_envio")
	private Integer grupoEnvio;
	
	@Column(name = "stdge_cestado")
	private String estado;
	
	@Column(name = "stdge_bhabilitado")
	private Boolean habilitado;
	
	@Column(name = "stdge_vobservaciones")
	private String observaciones;
	
	@Column(name = "aud_cusuario_crea")
	private String usuarioCrea;
	
	@Column(name = "aud_dfecha_crea")
	private Date fechaCrea;
	
	@Column(name = "aud_cusuario_modifica")
	private String usuarioModifica;
	
	@Column(name = "aud_dfecha_modifica")
	private Date fechaModifica;
	
    @JsonIgnore
    @OneToMany(mappedBy = "gestionEnvio") 
    private List<MpDetGestionEnvio> listDetMpGestionEnvio;
    
    @JsonIgnore
    @OneToMany(mappedBy = "gestionEnvio")   
    private List<MpEtapaGestionEnvio> listEtapaMpGestionEnvio;
    
    @Transient
    private Date fechaCreaFormatoMP;
    
    @Transient
    private Date fechaModificaFormatoMP;
    
    @Transient
    private Date fechaGeneracionFormatoMP;
    
    @Transient
	private Date fechaSubsanadoFormatoMP;
	
//    public String getFechaCrea() {
//        String fecha = "";
//        if(this.fechaCrea!=null){
//        	this.fechaCreaFormatoMP = this.fechaCrea;
//        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaCrea);
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
    
//    public String getFechaModifica() {
//        String fecha = "";
//        if(this.fechaModifica!=null){
//        	this.fechaModificaFormatoMP = this.fechaModifica;
//        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaModifica);
//        }       
//        
//        return fecha;
//    }
    
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
    
//    public String getFechaGeneracion() {
//        String fecha = "";
//        if(this.fechaGeneracion!=null){
//        	this.fechaGeneracionFormatoMP = this.fechaGeneracion;
//        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaGeneracion);
//        }       
//        
//        return fecha;
//    }
    
    @JsonIgnore
    public void setFechaGeneracionFormatoMP(Date fechaGeneracion) {
        this.fechaGeneracionFormatoMP= fechaGeneracion;
    }   
    
    public String getFechaGeneracionFormatoMP() {
        String fecha = "";
        if(this.fechaGeneracion!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaGeneracion);
        }       
        
        return fecha;
    }
    
//	public String getFechaSubsanado() {
//        String fecha = "";
//        if(this.fechaSubsanado!=null){
//        	this.fechaSubsanadoFormatoMP = this.fechaSubsanado;
//        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaSubsanado);
//        }       
//        
//        return fecha;
//    }
    
    @JsonIgnore
    public void setFechaSubsanadoFormatoMP(Date fechaSubsanado) {
        this.fechaSubsanadoFormatoMP= fechaSubsanado;
    }   
    
    public String getFechaSubsanadoFormatoMP() {
        String fecha = "";
        if(this.fechaSubsanado!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaSubsanado);
        }       
        
        return fecha;
    }
	
	/*
	 * Realiza busquedas en objetos tipo Collection:
	 * objVector.indexOf(objABuscar);
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MpGestionEnvio)) {
			return false;
		}
		MpGestionEnvio other = (MpGestionEnvio) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

		
	/*
	 * Realiza ordenamientos en objetos tipo Collection:
	 * Collections.sort(objVector);
	 * Collections.sort(objVector, objLLaveOrdenamiento);
	 */
	@Override
	public int compareTo(Object obj) {
		if (!(obj instanceof MpGestionEnvio))
			throw new ClassCastException("Valor invalido");
		MpGestionEnvio tmp = (MpGestionEnvio) obj;
		return (this.id.compareTo(tmp.id));
	}

	
}

