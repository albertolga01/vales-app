package com.petromar.valespetro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button Login;
    EditText email, pass;
    ProgressDialog progressBar;
    private Dialog dialog;

    private SharedPreferences sp;
    private CheckBox remember_key; // Recordar la casilla de verificación de contraseña
    private CheckBox automatic_login; // Casilla de verificación de inicio de sesión automático
    private Boolean rem_isCheck = true;
    private Boolean auto_isCheck = true;
    Spinner spinnerEstacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Login = (Button) findViewById(R.id.loginBttn);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        remember_key = (CheckBox) findViewById(R.id.remember_key);
        automatic_login = (CheckBox) findViewById(R.id.automatic_login);
        spinnerEstacion = (Spinner) findViewById(R.id.estaciones);
        obtenerEstaciones();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progressBar = ProgressDialog.show(LoginActivity.this,"Cargando","Iniciando sesión",true);
                userLogin();
               // Intent i = new Intent(LoginActivity.this, MainActivity.class );
               // startActivity(i);

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
            }
        });


        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        rem_isCheck = remember_key.isChecked();
        auto_isCheck = automatic_login.isChecked();

        remember_key.setChecked(true); // Establezca la contraseña de recuerdo para que se inicialice en true
        automatic_login.setChecked(true); // Establezca la contraseña de recuerdo para que se inicialice en true


        // Juzgar el estado de la casilla de verificación recordar contraseña
        if (sp.getBoolean("rem_isCheck", false)) {
            // La configuración predeterminada es registra
            // r el estado de la contraseña
            remember_key.setChecked(true);
            email.setText(sp.getString("EMAIL", ""));
            pass.setText(sp.getString("PASSWORD", ""));
            Log.e("Restaurar contraseña", "Restaurar automáticamente la contraseña de la cuenta guardada");

            // Juzgar el estado de la casilla de verificación de inicio de sesión automático
            if (sp.getBoolean("auto_isCheck", false)) {
                // Establecer el valor predeterminado es el estado de inicio de sesión automático
                automatic_login.setChecked(true);
                // interfaz de salto
                Intent intent = new Intent(LoginActivity.this, Menu.class);
                LoginActivity.this.startActivity(intent);
                Toast toast1 = Toast.makeText(getApplicationContext(), "Ya inició sesión automáticamente", Toast.LENGTH_SHORT);
                Log.e("Inicio sesiónautomático", "Inicio de sesión automático");

            }
        }


    }

    private void obtenerEstaciones() {  // ESTE ES EL LOGIN


        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = MainActivity.url; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                     System.out.print("_____ObtenerDatos______"+response);
                    JSONObject obj = new JSONObject(response);
                    List<String> estaciones = new ArrayList<String>();
                    JSONArray cast = obj.getJSONArray("estaciones");
                    for (int i = 0; i < cast.length(); i++) {
                        JSONObject estacion = cast.getJSONObject(i);
                        estaciones.add(estacion.getString("nombre"));
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, estaciones);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEstacion = (Spinner) findViewById(R.id.estaciones);
                    spinnerEstacion.setAdapter(dataAdapter);

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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

                MyData.put("id", "obtenerEstaciones");
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);


    }





    private void userLogin() {  // ESTE ES EL LOGIN


        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = MainActivity.url; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.dismiss();
                try {
                    System.out.print("_____LOGIN______"+response.trim());
                    JSONObject obj = new JSONObject(response);

                    //id
                    //estacion
                    if(obj.getString("id") != ""){
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USUARIO", obj.getString("id"));
                        editor.putString("ESTACION", spinnerEstacion.getSelectedItem().toString());
                        editor.putBoolean("rem_isCheck", remember_key.isChecked());
                        editor.putBoolean("auto_isCheck", automatic_login.isChecked());
                        editor.commit();
                        Intent theIntent = new Intent(getApplicationContext(), Menu.class);
                        startActivity(theIntent);

                    }

               //     Toast.makeText(LoginActivity.this, obj.getString("rol"), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                   // Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    dialog.show();
                    TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                    TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                    ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);


                    tituloDialog.setText("Alerta");
                    iconoDialog.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_baseline_error_24));
                    subtituloDialog.setText("Contraseña Incorrecta");

                    Button Okay = dialog.findViewById(R.id.btn_okay);


                    Okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                progressBar.dismiss();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("id", "autenticarLogin");
                MyData.put("usuario", email.getText().toString());
                MyData.put("pass", pass.getText().toString());
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);


    }
}