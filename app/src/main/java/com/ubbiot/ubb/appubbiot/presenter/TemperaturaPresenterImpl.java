package com.ubbiot.ubb.appubbiot.presenter;

import android.util.Log;

import com.ubbiot.ubb.appubbiot.model.interactor.TemperaturaInteractorImpl;
import com.ubbiot.ubb.appubbiot.model.interfaces.TempeturaInteractor;
import com.ubbiot.ubb.appubbiot.model.objects.Respuesta;
import com.ubbiot.ubb.appubbiot.presenter.interfaces.TemperaturaPresenter;
import com.ubbiot.ubb.appubbiot.view.interfaces.TemperaturaView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemperaturaPresenterImpl implements TemperaturaPresenter{

    private TemperaturaView view;
    private TempeturaInteractor interactor;
    String resultado;

    public TemperaturaPresenterImpl(TemperaturaView view){
        this.view = view;
        interactor = new TemperaturaInteractorImpl(this);
    }

    @Override
    public void obtenerDatos() {
        if(view!=null){
            Log.e("datos","ola k ase");
            interactor.obtenerTemperatura().enqueue(new Callback<List<Respuesta>>() {
                @Override
                public void onResponse(Call<List<Respuesta>> call, Response<List<Respuesta>> response) {
                    if(response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<List<Respuesta>> call, Throwable t) {
                    t.printStackTrace();

                }
            });
        }

    }
}
