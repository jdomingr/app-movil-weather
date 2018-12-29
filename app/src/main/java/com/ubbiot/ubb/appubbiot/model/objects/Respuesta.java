package com.ubbiot.ubb.appubbiot.model.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respuesta {

    @SerializedName("valor")
    @Expose
    private String valor;

    /**
     * No args constructor for use in serialization
     *
     */
    public Respuesta() {
    }

    /**
     *
     * @param valor
     */
    public Respuesta(String valor) {
        super();
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
