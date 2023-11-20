package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.svalero.ontimeapp.contract.SignListByParamsContract;
import com.svalero.ontimeapp.contract.SignListContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByParamsPresenter;
import com.svalero.ontimeapp.presenter.SignListPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 */
public class SignListView extends AppCompatActivity implements SignListContract.View {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignAdapter adapter; // Declaramos el adapter
    private SignListPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private Button btPickDate;
    private EditText etPlannedDate;
    private String firstDay = "";
    private Button btSearch;
    private Button btClear;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_list_view);

        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");

        presenter = new SignListPresenter(this); // Instanciamos el presenter y le pasamos el contexto
        initializeRecyclerView(); //inicializamos el RecyclerView
        initializeDatePicker(); //Inicializamos el DatePicker

        btSearch = findViewById(R.id.bt_list_all_find);
        btSearch.setOnClickListener(view -> {
        findSignsByDay();
        });

        btClear = findViewById(R.id.bt_list_all_clear);
        btClear.setOnClickListener(view -> {
            resetDay();
        });

//        firstDay = String.valueOf(((TextView) findViewById(R.id.etPlannedDate)));
//        Log.d("List Sign", "Fecha del calendario " + firstDay); // depurar para ver hasta donde llego

    }

    /**
     * Método para inicializar el RecyclerView
     */
    private void initializeRecyclerView() {
        signsList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rc_sign_all);// recreamos un objeto RecyclerView y le pasamos el id del creado en el layout activity_sign_list_view.xml
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
        etPlannedDate = findViewById(R.id.etPlannedDate);
        btPickDate = findViewById(R.id.bt_pick_date_list_all);

        btPickDate.setOnClickListener(new View.OnClickListener() {
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
                        SignListView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                etPlannedDate.setText(  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

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

        Log.d("List Sign", "Llamada desde view " + firstDay); // depurar para ver hasta donde llego
        presenter.loadAllSings(firstDay); // Le decimos al presenter cuando vuelve del resume que cargue xtodo de nuevo
    }

    @Override
    public void showSigns(List<Sign> signs) {
         // depurar para ver hasta donde llego
        signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
        signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
        Log.d("List Sign", "Llamada desde view showSigns: ");
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void findSignsByDay() {
        SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd-MM-yyyy");
        firstDay = etPlannedDate.getText().toString();
        Log.d("List Sign", "Fecha del calendario " + firstDay); // depurar para ver hasta donde llego

        presenter.loadAllSings(firstDay);
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
        Log.d("List Sign with Day", "Llamada desde view showSigns with Day: " + firstDay );
    }

    public void resetDay() {
        ((TextView) findViewById(R.id.etPlannedDate)).setText("");

        ((TextView) findViewById(R.id.etPlannedDate)).requestFocus();
    }

    // Todo Falta añadir Menu actionBar
}