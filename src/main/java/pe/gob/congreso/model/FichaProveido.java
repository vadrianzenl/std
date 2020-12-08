package pe.gob.congreso.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@IdClass(value = FichaProveido.FichaProveidoPK.class)
@Table(name = "STD_FICHA_PROVEIDO")
public class FichaProveido {

   @Id
   @JsonProperty
   private Integer fichaDocumentoId;
   
   @Id
   @JsonProperty
   private Integer proveidoId;

   @Column(name="stdfp_inumero")
   @JsonProperty
   private Integer numero;
   
   @Column(name="stdfp_vsumilla")
   @JsonProperty
   private String sumilla;
   
   @Column(name="stdfp_ippini")
   @JsonProperty
   private Integer ppinicio;
   
   @Column(name="stdfp_ippfin")
   @JsonProperty
   private Integer ppfin;
   
   @Column(name="stdfp_bhabilitado")
   @JsonProperty
   private Boolean habilitado;

   @Column(name="stdfp_cestado")
   @JsonProperty
   private String estado;
    
   @Column(name="aud_cusuario_crea")
   @JsonProperty
   private String usuarioCrea;    
   
   @Column(name = "aud_dfecha_crea")
   @JsonProperty
   private Date fechaCrea;
   
   @Column(name = "aud_cusuario_modifica")
   @JsonProperty
   private String usuarioModifica;
   
   @Column(name = "aud_dfecha_modifica")
   @JsonProperty
   private Date fechaModifica;
   
   @MapsId("fichaDocumentoId")
   @ManyToOne
   @JoinColumn(name="stdf_id", nullable=true)
   private FichaDocumento fichaDocumento;

   @MapsId("proveidoId")
   @ManyToOne
   @JoinColumn(name="stdp_id", nullable=true)
   private Proveido proveido;
   
   @SuppressWarnings("serial")
   @Data
   static class FichaProveidoPK implements Serializable{
       @Column(name="stdf_id")
       private Integer fichaDocumentoId;
       @Column(name="stdp_id")
       private Integer proveidoId;
   }
   
   @JsonIgnore
   @OneToMany(mappedBy = "fichaProveido")   
   private List<EnvioMultiple> listEnvioMultiple;
   
   @JsonProperty
   public void setProveido(Proveido p) {
       this.proveido= p;
   }
   @JsonIgnore
   public Proveido getProveido() {
       return proveido;
   }

   @JsonProperty
   public void setFichaDocumento(FichaDocumento f) {
       this.fichaDocumento= f;
   }
   @JsonIgnore
   public FichaDocumento getFichaDocumento() {
       return fichaDocumento;
   }

}