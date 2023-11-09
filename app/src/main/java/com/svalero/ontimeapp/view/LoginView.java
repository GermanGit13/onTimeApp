package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.svalero.ontimeapp.R;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 * implements AdapterView.OnItemClickListener Implementa el metodo onItemClick que le dice que cuando hagamos click en un elemento de la lista se habra en otra activity
 * Implementamos el contract en este caso como es un página con botones que nos llevan a otras activities no hace falta
 */
public class LoginView extends AppCompatActivity {

    /**
     * Declaramos la parte gráfica de la activity uy le asociamos el id del recurso
     */
    Button btEnter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        btEnter = findViewById(R.id.btInicio);//botón declarado en el layout
        btEnter.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }
}