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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = "STD_DET_GES_ENVIO")
@SequenceGenerator(name = "SEQ_STD_DET_GES_ENVIO", sequenceName = "SEQ_STD_DET_GES_ENVIO", allocationSize = 1, initialValue = 1 )
public class MpDetGestionEnvio implements Serializable, Comparable<Object> {

	/**
	 * Serial para guardar los estados de los atributos del objeto
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "stddge_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STD_DET_GES_ENVIO")
	private Integer id;
	
	@Column(name = "stddge_vnombre_archivo")
	private String nombreArchivo;
	
	@Column(name = "stddge_vdescripcion")
	private String descripcion;
	
	@Column(name = "stddge_dfecha_adjunto")
	private Date fechaAdjunto;
	
	@Column(name = "stddge_itipo")
	private Integer tipo;
	
	@Column(name = "stddge_bderivado")
	private Boolean derivado;
	
	@Column(name = "stddge_cestado")
	private String estado;
	
	@Column(name = "stddge_bhabilitado")
	private Boolean habilitado;
	
	@Column(name = "stddge_bpublico")
	private Boolean publico;
	
	@Column(name = "stddge_isubsanado_cargo")
	private Boolean subsanado;
	
	@Column(name = "aud_cusuario_crea")
	private String usuarioCrea;
	
	@Column(name = "aud_dfecha_crea")
	private Date fechaCrea;
	
	@Column(name = "aud_cusuario_modifica")
	private String usuarioModifica;
	
	@Column(name = "aud_dfecha_modifica")
	private Date fechaModifica;
	
    @ManyToOne
    @JoinColumn(name="stdge_id", nullable=true)   
    private MpGestionEnvio gestionEnvio;
    
	
    public void setFechaAdjunto(Date fechaAdjunto) {
        this.fechaAdjunto= fechaAdjunto;
    }    

    public String getFechaAdjunto() {
        String fecha = "";
        if(this.fechaAdjunto!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaAdjunto);
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
		if (!(obj instanceof MpDetGestionEnvio)) {
			return false;
		}
		MpDetGestionEnvio other = (MpDetGestionEnvio) obj;
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
		if (!(obj instanceof MpDetGestionEnvio))
			throw new ClassCastException("Valor invalido");
		MpDetGestionEnvio tmp = (MpDetGestionEnvio) obj;
		return (this.id.compareTo(tmp.id));
	}
	
}
