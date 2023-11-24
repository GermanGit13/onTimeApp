package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.LoginContract;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.LoginPresenter;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 * implements AdapterView.OnItemClickListener Implementa el metodo onItemClick que le dice que cuando hagamos click en un elemento de la lista se habra en otra activity
 * Implementamos el contract en este caso como es un página con botones que nos llevan a otras activities no hace falta
 */
public class LoginView extends AppCompatActivity implements LoginContract.View {

//    private Context context; // Activity en la que estamos
    private Snackbar snackbar;
    private LoginPresenter presenter;

    /**
     * Declaramos la parte gráfica de la activity uy le asociamos el id del recurso
     */
    Button btEnter;
    EditText etUsername;
    EditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        presenter = new LoginPresenter(this);
    }

    public void loginUser(View view) {
        etUsername = findViewById(R.id.etUserLogin);
        etPass = findViewById(R.id.etPassLogin);

        String username = etUsername.getText().toString();
        String pass = etPass.getText().toString();

        presenter.getLogin(username, pass);
    }

    @Override
    public void showLogin(User user) {
        if (user !=null ){
            Intent intent = new Intent(LoginView.this, MainActivity.class);
            intent.putExtra("user", user); // Mandamos el objeto entero ya que es una clase serializable
            startActivity(intent);
        } else {
            snackbar.make(((EditText) findViewById(R.id.tvUserLogin)), "Incorrect Data", BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("Accept", new View.OnClickListener() { // Crea un boton en el snackbar
                        @Override
                        public void onClick(View v) {
//                            onBackPressed();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(((EditText) findViewById(R.id.tvUserLogin)), errorMessage,
                BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}


//        btEnter = findViewById(R.id.btInicio);//botón declarado en el layout
//                btEnter.setOnClickListener(view -> {
//                Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        });