package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.adapter.SignAdapter;
import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByUserPresenter;

import java.util.ArrayList;
import java.util.List;

public class SignListByUserView extends AppCompatActivity implements SignListByUserContract.View {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignAdapter adapter; // Declaramos el adapter
    private SignListByUserPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private String userId;
    private String firstDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_by_user_view);

        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userId = String.valueOf(user.getId());
//        firstDay = "2013-11-04";

        Log.d("List Sign By User and Day", "Llamada desde view "+ userId + " / " + firstDay); // depurar para ver hasta donde llego

        presenter = new SignListByUserPresenter(this); // Instanciamos el presenter y le pasamos el contexto
        presenter.loadSignsByUser(userId, firstDay);
        initializeRecyclerView(); //inicializamos el RecyclerView
    }

    /**
     * Método para inicializar el RecyclerView
     */
    private void initializeRecyclerView() {
        signsList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rc_signs_by_user);// recreamos un objeto RecyclerView y le pasamos el id del creado en el layout activity_sign_list_view.xml
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SignAdapter(this, signsList); // se lo pasamos al adapter para que pinte los datos de cada fichaje de la lista en el item
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        Log.d("List Sign By User and Day", "Llamada desde view"); // depurar para ver hasta donde llego
        presenter.loadSignsByUser(userId, firstDay);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showSignsByUser(List<Sign> signs) {
        signsList.clear();
        signsList.addAll(signs);
        adapter.notifyDataSetChanged();
        if (signs.isEmpty()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.not_found_data_in_this_day)
                    .setMessage(R.string.there_is_no_data_for_the_selected_date)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String failDay = "";
                            presenter.loadSignsByUser(userId, failDay);
                            signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
                            signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
                            adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
                        }
                    })
                    .show();
        }
        Log.d("List Sign By User and Day", "Llamada desde view_showSignsByDepartment: " + signs.get(1));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}