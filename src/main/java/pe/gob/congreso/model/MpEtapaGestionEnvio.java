package pe.gob.congreso.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapea la estructura de la tabla STD_ETAPA_GESTION_ENVIO  
 * @author sperezr
 *
 */
@NoArgsConstructor
@Data
@Entity
@IdClass(value = MpEtapaGestionEnvio.MpEtapaGestionEnvioPK.class)
@Table(name = "STD_ETAPA_GESTION_ENVIO")
public class MpEtapaGestionEnvio implements Serializable {

	/**
	 * Serial para guardar los estados de los atributos del objeto
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    private Integer gestionEnvioId;
    
    @Id
    private Integer fichaDocumentoId;
    
	@MapsId("gestionEnvioId")
	@ManyToOne
    @JoinColumn(name="stdge_id", nullable=true)   
    private MpGestionEnvio gestionEnvio;
    
	@MapsId("fichaDocumentoId")
	@ManyToOne
    @JoinColumn(name="stdf_id", nullable=true)   
    private FichaDocumento fichaDocumento;
	
	
    @Data
    static class MpEtapaGestionEnvioPK implements Serializable{
        @Column(name="stdge_id")
        private Integer gestionEnvioId;
        @Column(name="stdf_id")
        private Integer fichaDocumentoId;
    }	
		
	@Column(name = "sdtege_ccodigo")
	private String codigo;
	
	@Column(name = "sdtege_vmotivo")
	private String motivo;
	
	@Column(name = "stdege_cestado")
	private String estado;
	
	@Column(name = "stdege_bhabilitado")
	private Boolean habilitado;
	
	@Column(name = "stdege_ccoloretapa")
	private String indicadorColor;
	
	@Column(name = "aud_cusuario_crea")
	private String usuarioCrea;
	
	@Column(name = "aud_dfecha_crea")
	private Date fechaCrea;
	
	@Column(name = "aud_cusuario_modifica")
	private String usuarioModifica;
	
	@Column(name = "aud_dfecha_modifica")
	private Date fechaModifica;
	
	public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea= fechaCrea;
    }    

    public String getFechaCrea() {
        String fecha = "";
        if(this.fechaCrea!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaCrea);
        }       
        
        return fecha;
    }

    
}
