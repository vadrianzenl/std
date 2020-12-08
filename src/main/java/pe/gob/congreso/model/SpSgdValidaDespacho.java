package pe.gob.congreso.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class SpSgdValidaDespacho {

	@Id
	private String id;
	private String resultado;

}