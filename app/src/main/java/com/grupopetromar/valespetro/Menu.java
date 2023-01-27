package com.grupopetromar.valespetro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button unico, multiple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

    }
}