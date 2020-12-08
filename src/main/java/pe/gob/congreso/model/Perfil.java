
package pe.gob.congreso.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_PERFIL")
public class Perfil {
    @Id
    @Column(name = "stdp_id")
    private String id;

    @Column(name = "stdp_cnombre") 
    private String nombre;
    
    @JsonIgnore
    @OneToMany(mappedBy = "perfil")
    private List<Usuario> usuarios;

    @JsonIgnore
    @OneToMany(mappedBy = "perfil")
    private List<PerfilMenu> perfilesMenu;
}
