package com.petromar.valespetro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    public static final String url = "https://vales.grupopetromar.com/apirest/indexapp.php" ;

    Button btnScan, btnContinuar, btnCerrarSesion;
    EditText txtResultado;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

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

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);


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


    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
            } else {
              //  Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();

               //activity datos
                Intent i = new Intent(MainActivity.this, Datos.class );
                i.putExtra("QRcode",result.getContents());
                startActivity(i);

               // txtResultado.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}