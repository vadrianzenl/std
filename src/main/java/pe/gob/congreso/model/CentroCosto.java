
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
@Table(name = "STD_CENTRO_COSTO")
public class CentroCosto {
    @Id
    @Column(name = "stdcc_id")
    private String id;

    @Column(name = "stdcc_cdescripcion") 
    private String descripcion;

    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<Empleado> listEmpleado;  
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<Proveido> listProveido; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<Adjunto> listAdjunto; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<Bitacora> listBitacora; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCostoOrigen")
    private List<Deriva> listCentrosCostoOrigen; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCostoDestino")
    private List<Deriva> listCentrosCostoDestino; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<EnviadoExterno> listEnviadoExterno; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<FichaDocumento> listFichaDocumento; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<Relaciona> listRelaciona; 

    @JsonIgnore
    @OneToMany(mappedBy = "centroCostoRemitido")
    private List<FichaDocumento> listFichaDocumentoRemitido; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "centroCosto")
    private List<FiltroAnio> listFiltroAnio;
    
    //STD_CENTRO_COSTO_CATEGORIA
    //@ManyToMany
    //@JoinTable(name="STD_CENTRO_COSTO_CATEGORIA", 
    //            joinColumns={@JoinColumn(name="stdcc_id")},          //STD_CENTRO_COSTO 
    //            inverseJoinColumns={@JoinColumn(name="stdc_id")})   //STD_CATEGORIA
    //@LazyCollection(LazyCollectionOption.FALSE)
    //private List<Categoria> listaCategoria = new ArrayList<Categoria>();
    
}
