package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.adapter.SignAdapter;
import com.svalero.ontimeapp.contract.SignListByParamsContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByParamsPresenter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 */
public class SignListByParamsView extends AppCompatActivity implements SignListByParamsContract.View, SearchView.OnQueryTextListener {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignAdapter adapter; // Declaramos el adapter
    private SignListByParamsPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private String department;
    private Button btPickDate;
    private Button btPickDateTo;
    private EditText etPlannedDateFromParams;
    private EditText etPlannedDateToParams;
    private String firstDay = "";
    private String secondDay = "";
    private String name = "";
    private Button btSearchParams;
    private Button btClearParams;
    private SearchView svSearchParams;


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

        Log.d("List Sign Params", "Llamada desde view "+ department + " / " + firstDay); // depurar para ver hasta donde llego

        presenter = new SignListByParamsPresenter(this); // Instanciamos el presenter y le pasamos el contexto
        presenter.loadSignsByParams(department, firstDay, secondDay, name);
        initializeRecyclerView(); //inicializamos el RecyclerView
        initializeDatePicker(); //Inicializamos el DatePicker

        btSearchParams = findViewById(R.id.bt_list_params_find);
        btSearchParams.setOnClickListener(view -> {
            findSignsByDepartmentAndDay();
        });

        btClearParams = findViewById(R.id.bt_list_params_clear);
        btClearParams.setOnClickListener(view -> {
            resetDay();
        });

        svSearchParams = findViewById(R.id.svSearchParams);
        svSearchParams.setOnQueryTextListener(this);
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

    /**
     * Método para el Calendario
     */
    private void initializeDatePicker() {
        etPlannedDateFromParams = findViewById(R.id.etPlannedDateParams);
        etPlannedDateToParams = findViewById(R.id.etPlannedToDateParams);
        btPickDate = findViewById(R.id.bt_pick_date_list_params);
        btPickDateTo = findViewById(R.id.bt_pick_date_list_paramsTo);

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
                        SignListByParamsView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                LocalDate date = LocalDate.of(year, (monthOfYear + 1), dayOfMonth); // Pasar yyyy-MM-dd
                                etPlannedDateFromParams.setText(date.toString());
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

        btPickDateTo.setOnClickListener(new View.OnClickListener() {
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
                        SignListByParamsView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                LocalDate date = LocalDate.of(year, (monthOfYear + 1), dayOfMonth); // Pasar yyyy-MM-dd
                                etPlannedDateToParams.setText(date.toString());
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
        Log.d("List Sign Params", "Llamada desde view"); // depurar para ver hasta donde llego
        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name); // Le decimos al presenter cuando vuelve del resume que cargue xtodo de nuevo
    }

    @Override
    public void showSignsByParams(List<Sign> signs) {
        signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
        signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
//        if (signs.isEmpty()) {
//            new MaterialAlertDialogBuilder(this)
//                    .setTitle(R.string.not_found_data_in_this_day)
//                    .setMessage(R.string.there_is_no_data_for_the_selected_date)
//                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String failFirstDay = "";
//                            String failSecondDay = "";
//                            String failName = "";
//                            presenter.loadSignsByParams(user.getDepartment(), failFirstDay, failSecondDay, name);
//                            signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
//                            signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
//                            adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
//                        }
//                    })
//                    .show();
//        }
        Log.d("List Sign Params", "Llamada desde view showSignsByParams: " );
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void findSignsByDepartmentAndDay() {
        SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd-MM-yyyy");
        firstDay = etPlannedDateFromParams.getText().toString();
        secondDay = etPlannedDateToParams.getText().toString();
        Log.d("List Sign with Params", "Fecha del calendario " + firstDay + " / " + department); // depurar para ver hasta donde llego

        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios

//        new MaterialAlertDialogBuilder(this)
//                .setTitle(R.string.search_by_date)
//                .setMessage(getString(R.string.search_by) + firstDay + getString(R.string.in_case_of_no_data_delete_filters_and_search_again))
//                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        presenter.loadSignsByParams(department, firstDay);
//                        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
//                    }
//                })
//                .show();
        Log.d("List Sign with Params", "Llamada desde view loadSignsByParams with Params: " + firstDay + " / " + department);
    }

//    public void sumeDaySearchFrom() {
////        firstDay = etPlannedDateFromParams.getText().toString();
//        LocalDate masOne = LocalDate.now();
////        masOne = masOne.plusDays(1);
//        etPlannedDateFromParams.setText(masOne.toString());
//        firstDay = "2023-11-05";
//        Log.d("List Sign with Params Mas one", "Fecha del calendario " + firstDay + " / " + department); // depurar para ver hasta donde llego
//
//        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name);
//        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
//
//        Log.d("List Sign with Params", "Llamada desde view loadSignsByParams with Params: " + firstDay + " / " + department);
//    }

    public void resetDay() {
        ((TextView) findViewById(R.id.etPlannedDateParams)).setText("");
        ((TextView) findViewById(R.id.etPlannedToDateParams)).setText("");

        ((TextView) findViewById(R.id.etPlannedDateParams)).requestFocus();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String failSecondDay = "";
        name = newText;
        signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
        presenter.loadSignsByParams(user.getDepartment(), firstDay, failSecondDay, name);

        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
        return false;
    }
    // Todo Falta añadir Menu actionBar
}