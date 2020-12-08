
package pe.gob.congreso.model;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.IdClass;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@IdClass(value = PerfilMenu.PerfilMenuPK.class)
@Table(name = "STD_PERFIL_MENU")
public class PerfilMenu {
	@Id
    private String perfilId;

    @Id
    private String menuId;

    @Column(name = "std_agregar")
    private String agregar;

    @Column(name = "std_modificar")
    private String modificar;

    @Column(name = "std_borrar")
    private String borrar;

    @MapsId("perfilId")
    @ManyToOne
    @JoinColumn(name="stdp_id", nullable=true)
    @JsonIgnore
    private Perfil perfil;

    @MapsId("menuId")
    @ManyToOne
    @JoinColumn(name="stdm_id", nullable=true)
    private Menu menu;

    @Data
    static class PerfilMenuPK implements Serializable{
        @Column(name="stdp_id")
        private String perfilId;
        @Column(name="stdm_id")
        private String menuId;
    }

    @JsonProperty
    public void setPerfil(Perfil p) {
        this.perfil= p;
    }

    @JsonIgnore
    public Perfil getPerfil() {
        return perfil;
    }
    
}
