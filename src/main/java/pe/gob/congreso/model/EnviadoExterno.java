
package pe.gob.congreso.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_ENVIADO_EXTERNO")
@SequenceGenerator(name = "SEQ_STD_ENVIADO_EXTERNO", sequenceName = "SEQ_STD_ENVIADO_EXTERNO", allocationSize = 1, initialValue= 1)
public class EnviadoExterno {     
    @Id
    @Column(name = "stdee_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_ENVIADO_EXTERNO") 
    private Integer id; 

    @Column(name = "stdee_vgrupo") 
    private String grupo; 
    
    @Column(name = "stdee_vcargo") 
    private String cargo; 

    @Column(name = "stdee_vapellidos") 
    private String apellidos; 

    @Column(name = "stdee_vnombres") 
    private String nombres; 

    @Column(name = "stdee_vdni") 
    private String dni; 

    @Column(name = "stdee_vtelefono") 
    private String telefono; 

    @Column(name = "stdee_vcargo_externo") 
    private String cargoExterno; 

    @Column(name = "stdee_vdireccion") 
    private String direccion; 

    @Column(name = "stdee_vorigen") 
    private String origen;
    
    @ManyToOne
    @JoinColumn(name="stdee_itipo_enviado_por", nullable=true)    
    private Tipo tipoEnviado;    
    
    @ManyToOne
    @JoinColumn(name="stdf_id", nullable=true)   
    private FichaDocumento fichaDocumento;

    @ManyToOne(optional=true)
    @JoinColumn(name="stde_ienviado_por", nullable=true)    
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name="stdub_codigo", nullable=true)
    private UbigeoMaestro ubigeo;
    
    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;
    
    @JsonIgnore
    @OneToMany(mappedBy = "enviadoExterno")   
    private List<Deriva> listDerivaExterno;

    @JsonIgnore
    @Formula("concat(stdee_vorigen,' * ', stdee_vnombres,' ',stdee_vapellidos)")
    private String externoDescripcion;    
    
    @ManyToOne
    @JoinColumn(name="tmee_id", nullable=true)
    private EntidadExterna entidadExterna;
    
}
