package com.ubbiot.ubb.appubbiot.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.ubbiot.ubb.appubbiot.R;
import com.ubbiot.ubb.appubbiot.model.objects.Dato;
import com.ubbiot.ubb.appubbiot.model.objects.RespuestaDato;
import com.ubbiot.ubb.appubbiot.model.service.DatosService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraficoTemperatura extends AppCompatActivity {

    private LineChart graficoTemperatura;
    private List<Entry>entradas;
    //Para almacenar las horas
    private ArrayList<String>horasX;
    //Para almacenar los valores por horas
    private List<Float>valores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_temperatura);
        graficoTemperatura = (LineChart)findViewById(R.id.graficoTemperatura);
        actualizarGraficoTemperatura();
        graficoTemperatura.getAxisLeft().setDrawGridLines(false);
        graficoTemperatura.getXAxis().setDrawGridLines(false);
        graficoTemperatura.getAxisRight().setDrawGridLines(false);
        graficoTemperatura.setNoDataText("Temperatura no disponible");
    }



    public void actualizarGraficoTemperatura(){
        Retrofit retrofit;
        DatosService datosService;
        String user_authkey="heLMaOHTNY";
        String sensor_authkey = "E1yGxKAcrg";
        String fecha  = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(new Date());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1,TimeUnit.MINUTES)
                .readTimeout(1,TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        datosService = retrofit.create(DatosService.class);

        Call<RespuestaDato>call = datosService.medicionesPorDia(user_authkey,sensor_authkey,fecha);
        call.enqueue(new Callback<RespuestaDato>() {
            @Override
            public void onResponse(Call<RespuestaDato> call, Response<RespuestaDato> response) {
                if(response.isSuccessful()){
                    RespuestaDato datos = response.body();
                    List<Dato>info = datos.getDato();
                    entradas = new ArrayList<Entry>();
                    entradas.add(new Entry(4f,0));
                    horasX = new ArrayList<>();
                    valores = new ArrayList<>();
                    for(int i=0; i<info.size();i++){
                        String pruebaFecha = info.get(i).getFecha();
                        String hora = info.get(i).getHora().substring(0,2);
                        String pruebaValor = info.get(i).getValor();
                        Float prueba;
                        prueba = Float.parseFloat(hora);
                        Log.e("Dato","Fecha: " + pruebaFecha);
                        Log.e("Dato","Hora: " + prueba);
                        Log.e("Dato","Valor: " +pruebaValor);
                        horasX.add(hora);
                        entradas.add(new Entry(Float.parseFloat(hora),Float.parseFloat(info.get(i).getValor())));
                    }
                    Collections.sort(entradas,new EntryXComparator());
                    LineDataSet set1 = new LineDataSet(entradas,"Temperatura");
                    set1.setColor(Color.BLACK);
                    set1.setCircleColor(Color.BLACK);
                    set1.setLineWidth(1f);
                    set1.setCircleRadius(3f);
                    set1.setDrawCircleHole(true);
                    set1.setValueTextSize(9f);
                    set1.setDrawFilled(true);
                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    dataSets.add(set1);
                    LineData lineData = new LineData(dataSets);
                    //se setea el gráfico con los datos obtenidos
                    graficoTemperatura.setData(lineData);
                    /*se utiliza el método invalidate para refrescar la vista de la actividad
                    * de lo contrario se tendría que tocar una vez la pantalla para que se muestre el gráfico*/
                    graficoTemperatura.invalidate();

                }

            }

            @Override
            public void onFailure(Call<RespuestaDato> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
