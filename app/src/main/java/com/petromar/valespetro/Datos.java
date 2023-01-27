package com.petromar.valespetro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Datos extends AppCompatActivity {

    private static final String TAG = "";
            public static String valeguid = "";
    TextView TxtCadena, TxtFolio, TxtEmision, TxtVencimiento, TxtInfoCliente, TxtCantidad, TxtPegasus;
    Button btnConciliar;
    private Dialog dialog;

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        btnConciliar = findViewById(R.id.btnConciliar);
        TxtCadena = findViewById(R.id.TxtCadena);
        TxtFolio = findViewById(R.id.TxtFolio);
        TxtEmision = findViewById(R.id.TxtEmision);
        TxtVencimiento = findViewById(R.id.TxtVencimiento);
        TxtInfoCliente = findViewById(R.id.TxtInfoCliente);
        TxtCantidad = findViewById(R.id.TxtCantidad);
        TxtPegasus = findViewById(R.id.TxtPegasus);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        btnConciliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                // progressBar = ProgressDialog.show(LoginActivity.this,"Cargando","Iniciando sesión",true);
                Intent i = new Intent(LoginActivity.this, MainActivity.class );
                startActivity(i);*/
                btnConciliar.setEnabled(false);
                QuemarDatosVale();

            }
        });

        QRCodeWriter writer = new QRCodeWriter();
        ImageView tnsd_iv_qr = (ImageView)findViewById(R.id.imgQR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String QRcode = bundle.getString("QRcode");
            valeguid = QRcode;
            try {
                QRGEncoder qrgEncoder = new QRGEncoder(QRcode, null, QRGContents.Type.TEXT, 3500);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);
                Bitmap bitmap;

                // Getting QR-Code as Bitmap
                bitmap = qrgEncoder.getBitmap();
                // Setting Bitmap to ImageView
                tnsd_iv_qr.setImageBitmap(bitmap);
            }catch (Exception e ){

            }
        }

        obtenerDatosVale();

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
            }
        });
    }

    private void obtenerDatosVale() {


        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = MainActivity.url; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.print("_____ObtenerDatosVale______"+response);
                   // JSONObject obj = new JSONObject(response);
                    JSONObject obj = new JSONObject(response);
                    JSONArray cast = obj.getJSONArray("datosvale");

                    for (int i = 0; i < cast.length(); i++) {
                        JSONObject datoscliente = cast.getJSONObject(i);
                        TxtFolio.setText(datoscliente.getString("idvale"));
                        TxtInfoCliente.setText(datoscliente.getString("nombrecliente"));
                        TxtEmision.setText(datoscliente.getString("fechaemision"));
                        TxtVencimiento.setText(datoscliente.getString("fechavencimiento"));
                        TxtCantidad.setText("$ " + datoscliente.getString("cantidad"));
                        TxtCadena.setText(datoscliente.getString("valeguid"));
                        TxtPegasus.setText(datoscliente.getString("idpegasus"));

                        System.out.print(datoscliente.getString("idpegasus"));
                        System.out.print(datoscliente.getString("cantidad"));


                        if (datoscliente.getString("cantidad").equals(0)) {
                            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                            if (networkInfo != null && networkInfo.isConnected()) {
                                Log.d("MIAPP", "Estás online");

                                Log.d("MIAPP", " Estado actual: " + networkInfo.getState());

                                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                    // Estas conectado a un Wi-Fi
                                    Log.d("MIAPP", " Nombre red Wi-Fi: " + networkInfo.getExtraInfo());
                                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){

                                    Log.d("MIAPP", " Nombre red: " + networkInfo.getExtraInfo());
                                }

                            } else {
                                Log.d("MIAPP", "Estás offline");
                                dialog.show();
                                TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                                TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                                ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                                tituloDialog.setText("Alerta");
                                iconoDialog.setImageDrawable(ContextCompat.getDrawable(Datos.this, R.drawable.ic_baseline_error_24));
                                subtituloDialog.setText("No estas conectado a internet");

                            }
                        }
                    }


                } catch (JSONException e) {
                    Toast.makeText(Datos.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                //retornar a main
                Intent i = new Intent(Datos.this, MainActivity.class );

                startActivity(i);

                Toast.makeText(Datos.this, "error de conexión", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("id", "obtenerDatosVale");
                MyData.put("valeguid", valeguid);
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);

    }

    private void QuemarDatosVale() {


        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = MainActivity.url; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.print("_____QuemarDatosVale______"+response.trim());
                    // JSONObject obj = new JSONObject(response);
                    dialog.show();
                    TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                    TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                    ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                    tituloDialog.setText("Alerta");
                    if(response.trim().equals("Conciliado correctamente")){
                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Datos.this, R.drawable.ic_baseline_check));
                        subtituloDialog.setText(response.trim());
                        btnConciliar.setEnabled(true);
                    }else{
                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Datos.this, R.drawable.ic_baseline_error_24));
                        subtituloDialog.setText("Error: "+response.trim());
                        btnConciliar.setEnabled(true);
                    }


                    Button Okay2 = dialog.findViewById(R.id.btn_okay);
                    Okay2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.dismiss();
                            Intent i = new Intent(Datos.this, MainActivity.class );

                            startActivity(i);
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(Datos.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("id", "quemarVale");
                MyData.put("valeguid", valeguid);
                MyData.put("estacion", sp.getString("ESTACION", ""));
                MyData.put("usuario", sp.getString("USUARIO", ""));
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);

    }
}