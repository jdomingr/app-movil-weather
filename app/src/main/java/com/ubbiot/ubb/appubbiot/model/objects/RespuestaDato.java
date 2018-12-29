package com.ubbiot.ubb.appubbiot.model.objects;

//Modelo que represena la respuesta que entrega el json al consultar por los datos en una fecha especifica
import java.util.List;

public class RespuestaDato {

    private boolean success;
    private List<Dato> data = null;

    public RespuestaDato(){

    }

    public RespuestaDato(boolean success, List<Dato> dato){
        super();
        this.success = success;
        this.data = dato;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Dato> getDato() {
        return data;
    }

    public void setDato(List<Dato> dato) {
        this.data = dato;
    }
}
