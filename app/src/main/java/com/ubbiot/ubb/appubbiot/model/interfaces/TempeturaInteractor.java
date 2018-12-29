package com.ubbiot.ubb.appubbiot.model.interfaces;


import com.ubbiot.ubb.appubbiot.model.objects.Respuesta;
import com.ubbiot.ubb.appubbiot.model.service.DatosService;

import java.util.List;

import retrofit2.Call;

public interface TempeturaInteractor {

    Call<List<Respuesta>> obtenerTemperatura();
}
