/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.congreso.util;

/**
 *
 * @author crodas
 */
public class ResponseJson {
    private Boolean success;
    private Object data;
    private Integer errorCode;
    private String errorMessage;

    public ResponseJson() {
        this.success = false;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
