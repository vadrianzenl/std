
package pe.gob.congreso.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.FetchType;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_USUARIO")
public class Usuario {
    @Id
    @Column(name = "stdu_cusuario") 
    private String nombreUsuario;

    @JsonIgnore    
    @Column(name = "stdu_vpassword") 
    private String password;
      
    @Column(name = "stdu_vnombres") 
    private String nombres;
       
    @Column(name = "stdu_vapellidos") 
    private String apellidos;
    
    @Column(name = "stdu_vemail") 
    private String email;
    
    @Column(name = "stdu_bhabilitado") 
    private int habilitado;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "stdu_dfecha_registro") 
    private Date fechaRegistro;    

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "stde_id")
    private Empleado empleado;

    @ManyToOne(optional=true)
    @JoinColumn(name="stdu_cperfil", nullable=true)
    private Perfil perfil;
  
    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    //@JsonManagedReference
    private List<UsuarioSesion> sesiones;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Actividad> actividades;

    @JsonIgnore
    @OneToMany(mappedBy = "usuarioCrea")
    private List<FichaSubcategoria> fichasSubCategoria;

    @JsonIgnore
    @OneToMany(mappedBy = "usuarioRelaciona")
    private List<Relaciona> relaciones;

    @JsonIgnore
    @OneToMany(mappedBy = "usuarioCrea")
    private List<FichaDocumento> fichasDocumento;

    @Transient
    private String ipAddress;

    //@Column(name = "stdu_firma") //Comentado AEP 23.07.2019 
    //private String firma;
    
    @JsonProperty    
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
