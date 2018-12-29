package com.ubbiot.ubb.appubbiot.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ubbiot.ubb.appubbiot.R;
import com.ubbiot.ubb.appubbiot.model.objects.Respuesta;
import com.ubbiot.ubb.appubbiot.model.service.DatosService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RadiacionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RadiacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RadiacionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView texto;
    private ImageButton irGraficoRadiacion;

    private OnFragmentInteractionListener mListener;

    public RadiacionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RadiacionFragment newInstance(String param1, String param2) {
        RadiacionFragment fragment = new RadiacionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView =  inflater.inflate(R.layout.fragment_radiacion, container, false);
        irGraficoRadiacion = (ImageButton)rootView.findViewById(R.id.verGraficoRadiacion);
        irGraficoRadiacion.setOnClickListener(graficoRadiacion);
       texto = rootView.findViewById(R.id.textoRadiacion);
       actualizarRadiacion();
       return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void actualizarRadiacion(){
        Retrofit retrofit;
        DatosService service;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(    60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/medicion/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DatosService.class);
        Call<List<Respuesta>> call  = service.obtenerRadiacionActual();
        call.enqueue(new Callback<List<Respuesta>>() {
            @Override
            public void onResponse(Call<List<Respuesta>> call, Response<List<Respuesta>> response) {
                if(response.isSuccessful()){
                    Log.e("dato","pase por aqui radiacion");
                    List<Respuesta> lista = response.body();
                    texto.setText(lista.get(0).getValor() + " nm");
                }
            }

            @Override
            public void onFailure(Call<List<Respuesta>> call, Throwable t) {
                 t.printStackTrace();
                Log.e("dato","NO pase por aqui radiacion");
            }
        });
    }

    private View.OnClickListener graficoRadiacion = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            verGraficoRadiacion();
        }
    };

    public void verGraficoRadiacion(){
        Intent intent = new Intent(getActivity(), GraficoRadiacion.class);
        startActivity(intent);
    }
}
