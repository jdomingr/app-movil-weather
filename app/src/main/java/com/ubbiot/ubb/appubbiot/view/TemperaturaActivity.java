package com.ubbiot.ubb.appubbiot.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ubbiot.ubb.appubbiot.R;
import com.ubbiot.ubb.appubbiot.model.objects.Respuesta;
import com.ubbiot.ubb.appubbiot.model.service.DatosService;
import com.ubbiot.ubb.appubbiot.presenter.TemperaturaPresenterImpl;
import com.ubbiot.ubb.appubbiot.presenter.interfaces.TemperaturaPresenter;
import com.ubbiot.ubb.appubbiot.view.interfaces.TemperaturaView;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TemperaturaActivity extends AppCompatActivity implements TemperaturaView {
    private TemperaturaPresenter presenter;
    private TextView resultado;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperatura_main);
        resultado = (TextView) findViewById(R.id.textoTemperatura);
        presenter = new TemperaturaPresenterImpl(this);
        timer = new Timer();
        actualizarTemperatura();


    }





    @Override
    public void setTemperatura(String temperatura) {
        resultado.setText("temperatura");
    }

    @Override
    public void setError(String error) {

    }

    public void actualizarTemperatura(){
        Retrofit retrofit;
        DatosService service;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.3.2/iot/web/medicion/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DatosService.class);

        Call<List<Respuesta>> call = service.obtenerTemperaturaActual();
        call.enqueue(new Callback<List<Respuesta>>() {
            @Override
            public void onResponse(Call<List<Respuesta>> call, Response<List<Respuesta>> response) {
                if(response.isSuccessful()){
                    List<Respuesta> lista = response.body();

                    resultado.setText(lista.get(0).getValor() + "°C");
                }
            }

            @Override
            public void onFailure(Call<List<Respuesta>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
}
