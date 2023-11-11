package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.domain.User;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 */
public class MainActivity extends AppCompatActivity {

    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    long userId;

    @SuppressLint("WrongViewCast") //Para poder presentar el TextView con el nombre de usuario y la foto
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userId = user.getId();

        Log.d("MenuPrincipal", "Ver si traigo el user: " + user.getId());
    }
}