package pe.gob.congreso.model;

import java.util.Date;
import java.util.List;

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

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_FILTRO_ANIO")
@SequenceGenerator(name = "SEQ_STD_FILTRO_ANIO", sequenceName = "SEQ_STD_FILTRO_ANIO", allocationSize = 1, initialValue= 1)
public class FiltroAnio {
	
	@Id
    @Column(name="stdfa_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_FILTRO_ANIO")
    private Integer id;
	
	@ManyToOne
    @JoinColumn(name="stdcc_id", nullable=false)    
	private CentroCosto centroCosto;
	
	@ManyToOne
	@JoinColumn(name="stdfa_itipo_anio", nullable=false)   
    private Tipo tipoAnio;
	
	@Column(name="stdfa_banio_actual")
    private boolean anioActual;
	
	@Column(name="stdfa_block_filtro")
    private boolean blockFiltro;
	
	@Column(name="stdfa_bhabilitado")
    private boolean habilitado;//bit

    @Column(name = "aud_cusuario_crea")
    private String usuarioCreacion;

    @Column(name = "aud_dfecha_crea")
    private Date fechaCreacion;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModificacion;

    @Column(name = "aud_dfecha_modifica")
    private Date fechaModificacion;

}
