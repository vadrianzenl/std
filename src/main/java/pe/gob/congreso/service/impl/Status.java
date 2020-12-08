package pe.gob.congreso.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Data
public class Status {

    private int code;
    private String message;
    private Map<String, Object> errors;
    private Object object;
    
    public Status() {
        this.errors = new HashMap<String, Object>();
    }

    public Status(int code, String message) {
        this.code = code;
        this.message = message;
        this.errors = new HashMap<String, Object>();
    }
    
    public Status(int code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }
    
    public Status(int code, Map<String, Object> errors) {
        this.errors = new HashMap<String, Object>();
        this.errors = errors;
        this.code = code;
    }
    
    public void getErrores(BindingResult result){
        for (Object object : result.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;                    
                    List<String> mensajes = new ArrayList<String>();
                    mensajes.add(fieldError.getDefaultMessage());
                    this.errors.put(fieldError.getField(), mensajes);
                }
        }
        this.code = 401;
    }
}
