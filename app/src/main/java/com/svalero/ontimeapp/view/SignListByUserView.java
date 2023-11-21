package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.adapter.SignAdapter;
import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByUserPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignListByUserView extends AppCompatActivity implements SignListByUserContract.View {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignAdapter adapter; // Declaramos el adapter
    private SignListByUserPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private String userId;
    private Button btPickDateByUser;
    private EditText etPlannedDateByUser;
    private String firstDay = "";
    private Button btSearchByUser;
    private Button btClearByUser;


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

        Log.d("List Sign By User and Day", "Llamada desde view "+ userId + " / " + firstDay); // depurar para ver hasta donde llego

        presenter = new SignListByUserPresenter(this); // Instanciamos el presenter y le pasamos el contexto
        presenter.loadSignsByUser(userId, firstDay);
        initializeRecyclerView(); //inicializamos el RecyclerView
        initializeDatePicker(); //Inicializamos el DatePicker

        btSearchByUser = findViewById(R.id.bt_list_by_user_find);
        btSearchByUser.setOnClickListener(view -> {
            findSignsUserByDay();
        });

        btClearByUser = findViewById(R.id.bt_list_by_user_clear);
        btClearByUser.setOnClickListener(view -> {
            resetUserByDay();
        });
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

    /**
     * Método para el Calendario
     */
    private void initializeDatePicker() {
        etPlannedDateByUser = findViewById(R.id.etPlannedDateByUser);
        btPickDateByUser = findViewById(R.id.bt_pick_date_list_by_user);

        btPickDateByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        SignListByUserView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                etPlannedDateByUser.setText(  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
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

    public void findSignsUserByDay() {
        SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd-MM-yyyy");
        firstDay = etPlannedDateByUser.getText().toString();
        Log.d("List Sign By User and Day", "Fecha del calendario " + firstDay); // depurar para ver hasta donde llego

        presenter.loadSignsByUser(userId,firstDay);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios

//        new MaterialAlertDialogBuilder(this)
//                .setTitle(R.string.you_don_t_have_an_department)
//                .setMessage(R.string.contact_with_administration_depatment)
//                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        presenter.loadAllSings(firstDay);
//                    }
//                })
//                .show();
        Log.d("List Sign By User and Day", "Llamada desde view showSigns with Day: " + firstDay );
    }

    public void resetUserByDay() {
        ((TextView) findViewById(R.id.etPlannedDateByUser)).setText("");

        ((TextView) findViewById(R.id.etPlannedDateByUser)).requestFocus();
    }
}