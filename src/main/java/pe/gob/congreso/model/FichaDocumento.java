
package pe.gob.congreso.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "STD_FICHA_DOCUMENTO")
@SequenceGenerator(name = "SEQ_STD_FICHA_DOCUMENTO", sequenceName = "SEQ_STD_FICHA_DOCUMENTO", allocationSize = 1, initialValue= 1)
public class FichaDocumento implements Serializable {
    @Id
    @Column(name = "stdf_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_FICHA_DOCUMENTO")
    private Integer id;
      
    @Column(name = "stdf_bpublicar")
    private boolean publicar;//bit    

    @Column(name = "stdf_inumero_folios")
    private Integer numeroFolios;
    
    @Column(name = "stdf_cnumero_doc")    
    @NotBlank(message = "No debe ser vacío")
    private String numeroDoc;
    
    @Column(name = "stdf_vreferencia")
    private String referencia;
    
    @Column(name = "stdf_vasunto")    
    @NotBlank(message = "No debe ser vacío")
    private String asunto;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "stdf_dfecha_documento")    
    @NotNull(message = "No debe ser vacío")
    private Date fechaDocumento;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "stdf_dfecha_cargo")
    private Date fechaCargo;
    
    @Column(name = "stdf_irespuesta")
    private Integer respuesta;
    
    //@Column(name = "stdf_vurl", nullable=true)
    //private String url;
    
    @Column(name = "stdf_bhabilitado")
    private boolean habilitado;//bit 
    
    @Column(name = "stdf_cestafeta")
    private String estafeta;//bit
    
    @Column(name = "stdf_bprivado")
    private boolean privado;//bit

    @Column(name = "stdf_bdocumento_parlamentario")
    private boolean docParlamentario;//bit

    @ManyToOne
    @JoinColumn(name="aud_cusuario_crea", nullable=true)    
    private Usuario usuarioCrea;    
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;

    @Column(name = "stdf_itipo_adjunto")
    private Integer tipoAdjunto;
    
    @ManyToOne
    @JoinColumn(name="stdal_codigo", nullable=true)    
    private AnioLegislativo anioLegislativo;
    
    @ManyToOne
    @JoinColumn(name="stdl_codigo", nullable=true)    
    private Legislatura legislatura;
    
    @ManyToOne(optional=true)
    @JoinColumn(name="stde_id", nullable=true)    
    private Empleado empleado;
    
    @ManyToOne
    @JoinColumn(name="stdmo_id", nullable=true)    
    private Motivo motivo;    
   
    @ManyToOne
    @JoinColumn(name="stdt_itipo_registro", nullable=true)   
    private Tipo tipoRegistro;
    
    @ManyToOne
    @JoinColumn(name="stdtd_id", nullable=true)       
    @NotNull(message = "No debe ser vacío")
    private TipoDocumento tipoDocumento;
    
    @ManyToOne
    @JoinColumn(name="stdt_iestado", nullable=true)    
    private Tipo tipoEstado;
    
    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;
    
    @ManyToOne
    @JoinColumn(name="stdf_remitidocc", nullable=true)    
    private CentroCosto centroCostoRemitido;
    
    @Column(name = "stdf_remitidodes")
    private String remitidoDes;
    
    @Column(name = "stdfp_inumero_mp")
    private Integer numeroMp;
    
    @Column(name = "stdcc_iid")
    private Integer centroCostoId;    
    
    @Column(name = "stdf_anio")
    private Integer anio;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fichaDocumento")  
    private List<Deriva> listDeriva;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fichaDocumento")   
    private List<Adjunto> listAdjunto;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fichaDocumento")   
    private List<Relaciona> listRelaciona;

    @JsonIgnore
    @OneToMany(mappedBy = "fichaRelacionada")   
    private List<Relaciona> listAsociados;
    
    @JsonIgnore
    @OneToMany(mappedBy = "fichaDocumento")   
    private List<FichaProveido> listFichasProveido;

    @JsonIgnore
    @OneToMany(mappedBy = "fichaDocumento")   
    private List<Bitacora> listBitacoras;
    
    @Column(name = "stdef_vobservaciones")
    private String observaciones;
    
    @ManyToOne
    @JoinColumn(name="stdt_itipo_envio", nullable=true)   
    private Tipo tipoEnvio;
    
    @Column(name = "stdf_ienvio_final")
    private Integer envioFinal;//bit
    
    @Transient
    private Date fechaCreaFormatoMP;
    
    @Transient
    private boolean adjuntos;
    
    @Transient
    private boolean notas;
    
    @Transient
    private boolean esMiRecibido;
    
    @Transient
    private String listDirigidos;
    
    public void setEsMiRecibido(boolean esMiRecibido) {
        this.esMiRecibido= esMiRecibido;
    }
    
    public void setListDirigidos(String listDirigidos) {
        this.listDirigidos= listDirigidos;
    }   
       
    public void setNotas(boolean notas) {
        this.notas= notas;
    }
        
    public void setAdjuntos(boolean adjuntos) {
        this.adjuntos= adjuntos;
    }   
    
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea= fechaCrea;
    }    

    public String getFechaCrea() {
        String fecha = "";
        if(this.fechaCrea!=null){
        	this.fechaCreaFormatoMP = this.fechaCrea;
        	fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaCrea);
        }       
        
        return fecha;
    }
    
    @JsonIgnore
    public void setFechaCreaFormatoMP(Date fechaCrea) {
        this.fechaCreaFormatoMP= fechaCrea;
    }   
    
    public String getFechaCreaFormatoMP() {
        String fecha = "";
        if(this.fechaCrea!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaCrea);
        }       
        
        return fecha;
    }

}
