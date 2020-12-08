package pe.gob.congreso.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_USUARIO_SESION")
public class UsuarioSesion{
    @Id
    @Column(name="stds_vsesion_key")
    private String sesionKey;

    @Column(name="stds_dfecha_expiracion")
    private Date fechaExpiracion;

    @ManyToOne
    @JoinColumn(name="stdu_cusuario", nullable=false)
    //@JsonBackReference
    private Usuario usuario;
}
