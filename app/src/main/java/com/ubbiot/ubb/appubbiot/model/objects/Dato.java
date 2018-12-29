package com.ubbiot.ubb.appubbiot.model.objects;
//Objeto que representa los datos obtenidos para el gráfico de las mediciones diarias
public class Dato {

    private String fecha;
    private String hora;
    private String valor;


    public  Dato(String fecha, String hora, String valor){
        super();
        this.fecha = fecha;
        this.hora = hora;
        this.valor = valor;
    }

    public Dato() {

    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
