
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
@Table(name = "STD_MENU")
public class Menu {
    @Id
    @Column(name = "stdm_id")
    private String id;

    @Column(name = "stdm_cdescripcion") 
    private String descripcion;
    
    @Column(name = "stdm_padre") 
    private String padre;    

    @Column(name = "stdm_orden") 
    private String orden;

    @Column(name = "stdm_href") 
    private String href;

    @Column(name = "stdm_icon") 
    private String icon;

    @Column(name = "stdm_tipo") 
    private String tipo;

    @JsonIgnore
    @OneToMany(mappedBy = "menu")
    private List<PerfilMenu> perfilesMenu;
    
}
