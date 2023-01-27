package com.grupopetromar.valespetro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiplesVales extends AppCompatActivity {


    Button btnScan, quemar;
    TextView totalTxtView;
    String[] array;
    String[] arr = new String[10];
    Double total = 0.0;
    SharedPreferences sp;
    ArrayList<MyAppModel> myappModelArrayList;
    private MyAppAdapter myAppAdapter;
    private RecyclerView recyclerView;

    private Dialog dialog;


    List<String> a = new ArrayList<String>();
    List<String> arrayList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiples_vales);

        btnScan = findViewById(R.id.btnScanMultiple);
        quemar = findViewById(R.id.btnQuemarMultiple);
        totalTxtView = findViewById(R.id.textViewTotal);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.contenedor);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MultiplesVales.this);


                integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setOrientationLocked(false);

                integrator.setPrompt("Leyendo código QR");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);

                integrator.initiateScan();


            }
        });

        quemar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quemarMultiples();

            }
        });


        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_continuar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Okay = dialog.findViewById(R.id.btn_okay);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent i = new Intent(MultiplesVales.this, Menu.class );

                startActivity(i);
            }
        });

    }

    /// crear array

    //for each in array mostrar tarjetas

    //quemar por array

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
            } else {
                 Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();




                //activity datos
                //result.getContents

                if(!a.contains(result.getContents())){
                    a.add(result.getContents());
                }
                mostrarMultiples();
                System.out.println("array: " + Arrays.deepToString(a.toArray()));


                System.out.println("xxxxxxxxxxxxxx");
                // txtResultado.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }



    public void mostrarMultiples(){


        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = MainActivity.url; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.print("_____ObtenerDatosValeMultiple______"+response);
                     JSONObject obj = new JSONObject(response);
                    JSONArray cast = obj.getJSONArray("datosvalemultiple");
                    myappModelArrayList = obtenerInformacion(response);
                    // A mi Adaptador le paso mi modelo
                    myAppAdapter = new MyAppAdapter(getApplicationContext(), myappModelArrayList);
                    // Le paso mi adaptador al RecyclerView
                    recyclerView.setAdapter(myAppAdapter);
                    // Cargo el Layout del RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setHasFixedSize(true);
                    /*
                    for (int i = 0; i < cast.length(); i++) {
                        JSONObject datoscliente = cast.getJSONObject(i);


                        System.out.print(datoscliente);


                    }*/


                } catch (JSONException e) {
                    //Toast.makeText(MultiplesVales.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                //retornar a main
                Intent i = new Intent(MultiplesVales.this, MainActivity.class );

                startActivity(i);

                Toast.makeText(MultiplesVales.this, "Error de conexión", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                JSONArray jsArray = new JSONArray(a);
                MyData.put("id", "obtenerDatosValeMultiple");

                MyData.put("valeguid[]", jsArray.toString());


                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);





    }



    public void quemarMultiples(){


        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = MainActivity.url; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.print(response);
                   dialog.show();
                    TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                    TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                    ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                    tituloDialog.setText("Alerta");

                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(MultiplesVales.this, R.drawable.ic_baseline_check));
                        subtituloDialog.setText(response.trim() + " vales concilidado correctamente");


                    /*
                    for (int i = 0; i < cast.length(); i++) {
                        JSONObject datoscliente = cast.getJSONObject(i);


                        System.out.print(datoscliente);


                    }*/


                } catch (Exception e) {
                    Toast.makeText(MultiplesVales.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                //retornar a main
                Intent i = new Intent(MultiplesVales.this, MainActivity.class );

                startActivity(i);

                Toast.makeText(MultiplesVales.this, "Error de conexión", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("id", "quemarMultiples");

                List<String> b = new ArrayList<String>();
                for (int i = 0; i < myappModelArrayList.size(); i++) {
                  //  System.out.println(myappModelArrayList.get(i).getFolio());
                    if(!b.contains(myappModelArrayList.get(i).getFolio())){
                        b.add(myappModelArrayList.get(i).getFolio());
                    }
                }

                JSONArray jsArray = new JSONArray(b);
                MyData.put("valeguid[]", jsArray.toString());
                MyData.put("estacion", sp.getString("ESTACION", ""));
                MyData.put("usuario", sp.getString("USUARIO", ""));

                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);





    }



    public ArrayList<MyAppModel> obtenerInformacion(String response) {
        // Creo un array con los datos JSON que he obtenido
        ArrayList<MyAppModel> listaArray = new ArrayList<>();
        // Solicito los datos al archivo JSON
        total = 0.0;
        try {        //System.out.print(response);
            JSONObject jsonObject = new JSONObject(response);
            // Accedo a la fila 'tarjetas del archivo JSON
            JSONArray dataArray = jsonObject.getJSONArray("datosvalemultiple");
            // Recorro los datos que hay en la fila del archivo JSON
            for (int i = 0; i < dataArray.length(); i++) {
            // Creo la variable 'datosModelo' y le paso mi modelo 'MyAppModel'
            MyAppModel datosModelo = new MyAppModel();
            // Creo la  variable 'objetos' y recupero los valores
            JSONObject objetos = dataArray.getJSONObject(i);
            // Selecciono dato por dato
            datosModelo.setName("Cliente: "+ objetos.getString("nombrecliente"));
            datosModelo.setdescription("Fecha vencimiento: " + objetos.getString("fechavencimiento"));
            datosModelo.setPlacas("Importe: $" + (objetos.getString("cantidad")));
            datosModelo.setIdTarjeta(objetos.getString("idvale"));
            datosModelo.setFolio(objetos.getString("idvale"));

            total += Double.parseDouble(objetos.getString("cantidad"));


            // Meto los datos en el array que definí más arriba 'listaArray
                listaArray.add(datosModelo);
            }

            totalTxtView.setText("$" + total.toString());
    } catch (JSONException e) {
        e.printStackTrace();
        Toast.makeText(MultiplesVales.this, "Error", Toast.LENGTH_LONG);    }
    // Devuelvo el array con los datos del archivo JSON
    return listaArray;
    }


}