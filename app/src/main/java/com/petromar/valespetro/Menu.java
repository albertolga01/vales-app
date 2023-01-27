package com.petromar.valespetro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button unico, multiple, btnCerrarSesion;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        unico = findViewById(R.id.unicoVale);
        multiple = findViewById(R.id.multipleVale);

        unico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, MainActivity.class );
                startActivity(i);
            }
        });

        multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, MultiplesVales.class );
                startActivity(i);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.edit().remove("USUARIO").commit();
                sp.edit().remove("ESTACION").commit();
                sp.edit().remove("rem_isCheck").commit();
                sp.edit().remove("auto_isCheck").commit();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));


            }
        });

    }
}