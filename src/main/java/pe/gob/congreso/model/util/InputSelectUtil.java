
package pe.gob.congreso.model.util;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class InputSelectUtil implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private Object value;      
    private String label;
    public String getLabel(){
    	if (this.label!=null) return this.label.trim();
    	else return "";
    }

    public Object getValue(){
    	if (this.value!=null) {
    		if(this.value instanceof String){
    			String val;
    			val = String.valueOf(this.value);
    			return val.trim();
    		}    			
    		else
    			return this.value;
    	}    		
    	else return this.value;
    }    
}
