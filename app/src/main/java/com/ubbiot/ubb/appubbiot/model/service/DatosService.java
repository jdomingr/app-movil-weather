package com.ubbiot.ubb.appubbiot.model.service;

import com.ubbiot.ubb.appubbiot.model.objects.Respuesta;
import com.ubbiot.ubb.appubbiot.model.objects.RespuestaDato;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DatosService {

    @GET("indextemperatura")
    Call<List<Respuesta>> obtenerTemperaturaActual();
    @GET("indexradiacion")
    Call<List<Respuesta>> obtenerRadiacionActual();
    @GET("indexhumedad")
    Call<List<Respuesta>> obtenerHumedadActual();

    @GET("medicionespordia/{user_authkey}/{sensor_authkey}/{fecha}")
    Call<RespuestaDato> medicionesPorDia(@Path("user_authkey") String user_authkey,@Path("sensor_authkey") String sensor_authkey,@Path("fecha") String fecha);
}
