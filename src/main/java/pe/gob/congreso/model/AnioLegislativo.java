
package pe.gob.congreso.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_ANIO_LEGISLATIVO")
public class AnioLegislativo {
    @Id
    @Column(name = "stdal_codigo")
    private Integer codigo;
      
    @Column(name = "stdal_vdescripcion")
    private String descripcion;
    
    @Column(name = "stdal_vdescripcion_abrev")
    private String descripcionAbrev;
       
    @Column(name = "stdal_dfecha_inicio")
    private Date fechaInicio;  
    
    @Column(name = "stdal_dfecha_fin")
    private Date fechaFin;
    
    @JsonIgnore
    @OneToMany(mappedBy = "anioLegislativo")   
    private List<FichaDocumento> listFichaDocumento;
    
    @ManyToOne
    @JoinColumn(name="stdpl_codigo", nullable=true)    
    private PeriodoLegislativo periodoLegislativo;
}
