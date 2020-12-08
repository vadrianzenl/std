package pe.gob.congreso.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mapea la estructura de la tabla STD_CANAL_ASOCIADO_ENVIO
 * 
 * @author sperezr
 *
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_CANAL_ASOCIADO_ENVIO")
@SequenceGenerator(name = "SEQ_STD_CANAL_ASOCIADO_ENVIO", sequenceName = "SEQ_STD_CANAL_ASOCIADO_ENVIO", allocationSize = 1, initialValue = 1)
public class MpCanalAsociadoEnvio  {

	/**
	 * Serial para guardar los estados de los atributos del objeto
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "stdcae_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STD_CANAL_ASOCIADO_ENVIO")
	private Integer id;

	@Column(name = "stdcae_igrupo_id")
	private Integer grupoId;

	@Column(name = "stdcae_idependencia_id")
	private Integer dependenciaId;

	@Column(name = "stdcae_iusuario_id")
	private Integer empleadoId;

	@Column(name = "stdcae_vcargo")
	private String cargo;

	@Column(name = "stdcae_ccanal")
	private String indCanal;

	@Column(name = "stdege_cestado")
	private String estado;

	@Column(name = "stdege_bhabilitado")
	private Boolean habilitado;

	@Column(name = "aud_cusuario_crea")
	private String usuarioCrea;

	@Column(name = "aud_dfecha_crea")
	private Date fechaCrea;

	@Column(name = "aud_cusuario_modifica")
	private String usuarioModifica;

	@Column(name = "aud_dfecha_modifica")
	private Date fechaModifica;

	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}

	public String getFechaCrea() {
		String fecha = "";
		if (this.fechaCrea != null) {
			fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaCrea);
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
		if (!(obj instanceof MpCanalAsociadoEnvio)) {
			return false;
		}
		MpCanalAsociadoEnvio other = (MpCanalAsociadoEnvio) obj;
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
	 * Collections.sort(objVector); Collections.sort(objVector,
	 * objLLaveOrdenamiento);
	 */
	public int compareTo(Object obj) {
		if (!(obj instanceof MpCanalAsociadoEnvio))
			throw new ClassCastException("Valor invalido");
		MpCanalAsociadoEnvio tmp = (MpCanalAsociadoEnvio) obj;
		return (this.id.compareTo(tmp.id));
	}

}
