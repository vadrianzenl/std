package pe.gob.congreso.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class SpKpiRecibidos {

	@Id
	private String id;
	private String ccsinrec;
	private String ccconrec;
	private String ususinrec;
	private String usuconrec;

}
