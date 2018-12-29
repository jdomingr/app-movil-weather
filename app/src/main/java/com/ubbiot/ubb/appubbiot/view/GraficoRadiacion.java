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

public class GraficoRadiacion extends AppCompatActivity {

    private LineChart graficoRadiacion;
    private List<Entry> entradas;
    //Para almacenar las horas
    private ArrayList<String> horasX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_radiacion);
        graficoRadiacion = (LineChart)findViewById(R.id.graficoRadiacion);
        actualizarGraficoRadiacion();
        graficoRadiacion.getAxisLeft().setDrawGridLines(false);
        graficoRadiacion.getXAxis().setDrawGridLines(false);
        graficoRadiacion.getAxisRight().setDrawGridLines(false);
        graficoRadiacion.setNoDataText("Radiación no disponible");
    }

    public void actualizarGraficoRadiacion(){
        Retrofit retrofit;
        DatosService datosService;
        String user_authkey="heLMaOHTNY";
        String sensor_authkey = "8IvrZCP3qa";
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

        Call<RespuestaDato> call = datosService.medicionesPorDia(user_authkey,sensor_authkey,fecha);
        call.enqueue(new Callback<RespuestaDato>() {
            @Override
            public void onResponse(Call<RespuestaDato> call, Response<RespuestaDato> response) {
                if(response.isSuccessful()){
                    RespuestaDato datos = response.body();
                    List<Dato>info = datos.getDato();
                    entradas = new ArrayList<Entry>();
                    entradas.add(new Entry(4f,0));
                    horasX = new ArrayList<>();
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
                    LineDataSet set1 = new LineDataSet(entradas,"Radiación ultravioleta");
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
                    graficoRadiacion.setData(lineData);
                    /*se utiliza el método invalidate para refrescar la vista de la actividad
                     * de lo contrario se tendría que tocar una vez la pantalla para que se muestre el gráfico*/
                    graficoRadiacion.invalidate();

                }

            }

            @Override
            public void onFailure(Call<RespuestaDato> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
