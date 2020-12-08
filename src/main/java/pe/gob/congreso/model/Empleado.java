
package pe.gob.congreso.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_EMPLEADO")
public class Empleado {
    @Id
    @Column(name = "stde_id")
    private Integer id;
      
    @Column(name = "stde_vapellidos")
    private String apellidos;
       
    @Column(name = "stde_vnombres")
    private String nombres;  

    @Column(name = "stde_descripcion")
    private String descripcion;

    @Column(name = "stde_vdni")
    private String dni;

    @Column(name = "stde_vsexo")
    private String sexo;

    @Column(name = "stde_vemail")
    private String email;

    @Column(name = "stde_cestado")
    private String estado;

    @Column(name = "stde_cestadoEmpleado")
    private String estadoEmpleado;

    @JsonIgnore
    @OneToOne(mappedBy = "empleado")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;
    
    @JsonIgnore
    @OneToMany(mappedBy = "empleado")   
    private List<FichaDocumento> listFichaDocumento;
    
    @JsonIgnore
    @OneToMany(mappedBy = "empleadoOrigen")   
    private List<Deriva> listDerivaOrigen;

    @JsonIgnore
    @OneToMany(mappedBy = "empleadoDestino")   
    private List<Deriva> listDerivaDestino;

    @JsonIgnore
    @OneToMany(mappedBy = "empleado")   
    private List<GrupoCentroCosto> listCentroCosto;

    @JsonIgnore
    @OneToMany(mappedBy = "empleado")   
    private List<Adjunto> listAdjuntos;

    @JsonIgnore
    @OneToMany(mappedBy = "empleado")   
    private List<Bitacora> listBitacoras;
    
    @JsonIgnore
    @OneToMany(mappedBy = "empleado")
    private List<NotificacionEmpleado> listNotificacionEmpleado;
    
    //STD_DIRIGIDO
    /*@JsonIgnore
    @ManyToMany(mappedBy="listaEmpleadoDir")
    private List<FichaDocumento> listaFichaDocumentoDir = new ArrayList<FichaDocumento>();
    
    //STD_ENVIADO_FISICO
    @JsonIgnore
    @ManyToMany(mappedBy="listaEmpleadoEnv")
    private List<FichaDocumento> listaFichaDocumentoEnv = new ArrayList<FichaDocumento>();*/
}
