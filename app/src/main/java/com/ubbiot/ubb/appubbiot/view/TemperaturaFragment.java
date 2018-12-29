package com.ubbiot.ubb.appubbiot.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.util.OnPrintTickLabel;
import com.ubbiot.ubb.appubbiot.R;
import com.ubbiot.ubb.appubbiot.model.objects.Respuesta;
import com.ubbiot.ubb.appubbiot.model.service.DatosService;

import java.text.SimpleDateFormat;
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

public class TemperaturaFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView texto;
    private PointerSpeedometer pointerSpeedometer;
    private OnFragmentInteractionListener mListener;
    private float numero;
    private String contenidoTexto;
    private ImageButton irGraficoTemp;
    public TemperaturaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemperaturaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemperaturaFragment newInstance(String param1, String param2) {
        TemperaturaFragment fragment = new TemperaturaFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_temperatura, container, false);
        irGraficoTemp = (ImageButton)rootView.findViewById(R.id.verGraficoTemp);
        irGraficoTemp.setOnClickListener(graficoTemp);
        texto = (TextView)rootView.findViewById(R.id.textoTemperatura2);
        actualizarTemperatura();


        return rootView;
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
                .baseUrl("http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/medicion/")
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
                    texto.setText(lista.get(0).getValor() + "°c");
                }
            }

            @Override
            public void onFailure(Call<List<Respuesta>> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private View.OnClickListener graficoTemp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           verGraficoTemp();
        }
    };

    public void verGraficoTemp(){
        Intent intent = new Intent(getActivity(), GraficoTemperatura.class);
        startActivity(intent);
    }


}
