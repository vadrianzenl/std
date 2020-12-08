
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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_RESPONSABLE")
@SequenceGenerator(name = "SEQ_STD_RESPONSABLE", sequenceName = "SEQ_STD_RESPONSABLE", allocationSize = 1, initialValue= 1)
public class Responsable {
    @Id
    @Column(name = "stdres_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_RESPONSABLE") 
    private Integer id;

    @ManyToOne
    @JoinColumn(name="stdg_id", nullable=false)    
    private Grupo grupo; 

    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=false)    
    private CentroCosto centroCosto; 

    @ManyToOne(optional=true)
    @JoinColumn(name="stde_iresponsable", nullable=true)    
    private Empleado empleado; 

    @Column(name = "stdres_bhabilitado")
    private boolean habilitado;
    
    @Column(name="aud_cusuario_crea")    
    private String usuarioCrea;     
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;

}
