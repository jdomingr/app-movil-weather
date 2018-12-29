package com.ubbiot.ubb.appubbiot.model.interactor;

import com.ubbiot.ubb.appubbiot.model.interfaces.TempeturaInteractor;
import com.ubbiot.ubb.appubbiot.model.objects.Respuesta;
import com.ubbiot.ubb.appubbiot.model.service.DatosService;
import com.ubbiot.ubb.appubbiot.presenter.interfaces.TemperaturaPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TemperaturaInteractorImpl implements TempeturaInteractor {

    private Retrofit retrofit;
    private TemperaturaPresenter presenter;
    private DatosService datos;

    public TemperaturaInteractorImpl(TemperaturaPresenter presenter){
        this.presenter = presenter;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.3.2/iot/web/medicion/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        datos = retrofit.create(DatosService.class);

    }

    @Override
    public Call<List<Respuesta>> obtenerTemperatura() {
        return datos.obtenerTemperaturaActual();
    }
}
