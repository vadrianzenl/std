
package pe.gob.congreso.model.util;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class sp {
    @Id
    private Integer id;

    private Integer ccsinrec;

    private Integer ccconrec;

    private Integer ususinrec;

    private Integer usuconrec;

}
