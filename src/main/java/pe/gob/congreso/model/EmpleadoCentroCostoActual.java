
package pe.gob.congreso.model;

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


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_EMPLEADO_CENTRO_COSTO_ACTUAL")
@SequenceGenerator(name = "SEQ_STD_EMP_CC_ACTUAL", sequenceName = "SEQ_STD_EMP_CC_ACTUAL", allocationSize = 1, initialValue= 1)
public class EmpleadoCentroCostoActual {
    @Id
    @Column(name = "stdecca_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_EMP_CC_ACTUAL") 
    private Integer id;
    
    @Column(name = "stdecca_bhabilitado")
    private boolean habilitado;
    
    @Column(name = "stdcca_activo")
    private boolean activo;
    
    @ManyToOne(optional=true)
    @JoinColumn(name="stde_id", nullable=true)    
    private Empleado empleado; 
    
    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;  
    
    @Column(name="aud_cusuario_crea")    
    private String usuarioCrea;     
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
    
    @Column(name = "stdecca_tipo")
    private String tipo;
}
