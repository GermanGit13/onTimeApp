package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.adapter.SignAdapter;
import com.svalero.ontimeapp.contract.SignListByParamsContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByParamsPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 */
public class SignListByParamsView extends AppCompatActivity implements SignListByParamsContract.View {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignAdapter adapter; // Declaramos el adapter
    private SignListByParamsPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private String department;
    private String firstDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_by_params_view);
        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        department = user.getDepartment();

//        department = "Informatica";
//        firstDay = "2013-11-04";

        Log.d("List Sign Params", "Llamada desde view "+ department + " / " + firstDay); // depurar para ver hasta donde llego

        presenter = new SignListByParamsPresenter(this); // Instanciamos el presenter y le pasamos el contexto
        presenter.loadSignsByParams(department, firstDay);
        initializeRecyclerView(); //inicializamos el RecyclerView
    }

    /**
     * Método para inicializar el RecyclerView
     */
    private void initializeRecyclerView() {
        signsList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rc_sign_by_params);// recreamos un objeto RecyclerView y le pasamos el id del creado en el layout activity_sign_list_view.xml
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SignAdapter(this, signsList); // se lo pasamos al adapter para que pinte los datos de cada fichaje de la lista en el item
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("List Sign Params", "Llamada desde view"); // depurar para ver hasta donde llego
        presenter.loadSignsByParams(user.getDepartment(), firstDay); // Le decimos al presenter cuando vuelve del resume que cargue xtodo de nuevo
    }

    @Override
    public void showSignsByParams(List<Sign> signs) {
        signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
        signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
        if (signs.isEmpty()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.not_found_data_in_this_day)
                    .setMessage(R.string.there_is_no_data_for_the_selected_date)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String failDay = "";
                            presenter.loadSignsByParams(user.getDepartment(), failDay);
                            signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
                            signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
                            adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
                        }
                    })
                    .show();
        }
        Log.d("List Sign Params", "Llamada desde view showSignsByParams: " );
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}