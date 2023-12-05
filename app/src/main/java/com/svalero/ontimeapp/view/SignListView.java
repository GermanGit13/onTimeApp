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
import android.widget.SearchView;
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
import com.svalero.ontimeapp.util.Calendario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 */
public class SignListView extends AppCompatActivity implements SignListContract.View, SearchView.OnQueryTextListener {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignAdapter adapter; // Declaramos el adapter
    private SignListPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private EditText etPlannedDateFromList;
    private EditText etPlannedDateToList;
    private String firstDay = "";
    private String secondDay = "";
    private String name = "";
    private Button btPickDateFrom;
    private Button btPickDateTo;
    private Button btSearchList;
    private Button btClearList;
    private Button btIncreaseDayFrom;
    private Button btDecreateDayFrom;
    private Button btIncreaseDayTo;
    private Button btDecreateDayTo;
    private Button btLastSevenDays;
    private SearchView svSearchList;


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

        btSearchList = findViewById(R.id.bt_list_find);
        btSearchList.setOnClickListener(view -> {
        findSignsByDay();
        });

        btClearList = findViewById(R.id.bt_list_clear);
        btClearList.setOnClickListener(view -> {
            resetDay();
        });

        btIncreaseDayFrom = findViewById(R.id.bt_list_From_increase);
        btIncreaseDayFrom.setOnClickListener(view -> {
            increaseDaySearchFrom();
        });

        btDecreateDayFrom = findViewById(R.id.bt_list_From_decrement);
        btDecreateDayFrom.setOnClickListener(view -> {
            subtractDaySearchFrom();
        });

        btIncreaseDayTo = findViewById(R.id.bt_list_To_increase);
        btIncreaseDayTo.setOnClickListener(view -> {
            increaseDaySearchTo();
        });

        btDecreateDayTo = findViewById(R.id.bt_list_To_decrement);
        btDecreateDayTo.setOnClickListener(view -> {
            subtractDaySearchTo();
        });

        btLastSevenDays = findViewById(R.id.bt_list_seven_days);
        btLastSevenDays.setOnClickListener(view -> {
            searchLastSevenDays();
        });

        svSearchList = findViewById(R.id.svSearchList);
        svSearchList.setOnQueryTextListener(this); // uso la implementacion con sus métodos para buscar por nombre
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
        etPlannedDateFromList = findViewById(R.id.etPlannedFromDateList);
        etPlannedDateToList =findViewById(R.id.etPlannedToDateList);
        btPickDateFrom = findViewById(R.id.bt_pick_date_list_From);
        btPickDateTo = findViewById(R.id.bt_pick_date_list_To);

        Calendario datePickerFrom = new Calendario();
        datePickerFrom.datepicker(btPickDateFrom, etPlannedDateFromList, this);
        Calendario datePickerTo = new Calendario();
        datePickerTo.datepicker(btPickDateTo, etPlannedDateToList, this);
        Calendario datePicketTextFrom = new Calendario();
        datePicketTextFrom.datepickerTextView(etPlannedDateFromList, etPlannedDateFromList, this);
        Calendario datePicketTextTo = new Calendario();
        datePicketTextTo.datepickerTextView(etPlannedDateToList, etPlannedDateToList, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("List Sign", "Llamada desde view " + firstDay); // depurar para ver hasta donde llego
        presenter.loadAllSings(firstDay, secondDay, name); // Le decimos al presenter cuando vuelve del resume que cargue xtodo de nuevo
    }

    @Override
    public void showSigns(List<Sign> signs) {
         // depurar para ver hasta donde llego
        signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
        signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios

//        Log.d("List Sign", "Llamada desde view showSigns: ");
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void findSignsByDay() {
        SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd-MM-yyyy");
        firstDay = etPlannedDateFromList.getText().toString();
        secondDay = etPlannedDateToList.getText().toString();
//        Log.d("List Sign", "Fecha del calendario " + firstDay); // depurar para ver hasta donde llego

        presenter.loadAllSings(firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios

//        Log.d("List Sign with Day", "Llamada desde view showSigns with Day: " + firstDay );
    }

    public void increaseDaySearchFrom() {
        firstDay = etPlannedDateFromList.getText().toString();
        if (firstDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            firstDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(firstDay);
        masOne = masOne.plusDays(1);
        firstDay= String.valueOf(masOne);
        etPlannedDateFromList.setText(masOne.toString());

        Log.d("List Sign", "Llamada desde view showSigns: " + firstDay + " / " + secondDay + " / " + name);
        presenter.loadAllSings(firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void subtractDaySearchFrom() {
        firstDay = etPlannedDateFromList.getText().toString();
        if (firstDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            firstDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(firstDay);
        masOne = masOne.minusDays(1);
        firstDay= String.valueOf(masOne);
        etPlannedDateFromList.setText(masOne.toString());

        presenter.loadAllSings(firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void increaseDaySearchTo() {
        secondDay = etPlannedDateToList.getText().toString();
        if (secondDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            secondDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(secondDay);
        masOne = masOne.plusDays(1);
        secondDay= String.valueOf(masOne);
        etPlannedDateToList.setText(masOne.toString());

        presenter.loadAllSings(firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void subtractDaySearchTo() {
        secondDay = etPlannedDateToList.getText().toString();
        if (secondDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            secondDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(secondDay);
        masOne = masOne.minusDays(1);
        secondDay= String.valueOf(masOne);
        etPlannedDateToList.setText(masOne.toString());

        presenter.loadAllSings(firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    /**
     * Buscar ultimos siete dias
     */
    public void searchLastSevenDays() {
        resetDay();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        LocalDate sevenDay;
        sevenDay = dateNow.minusDays(7);

        firstDay = String.valueOf(sevenDay.format(dateTimeFormatter));
        secondDay = String.valueOf(dateNow.format(dateTimeFormatter));

        Log.d("List Sign", "Llamada desde view showSigns 7 dias: " + firstDay + " / " + secondDay + " / " + name);
        presenter.loadAllSings(firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }


    /**
     * Limiar filtros de busqueda
     */
    public void resetDay() {
        ((TextView) findViewById(R.id.etPlannedToDateList)).setText("");
        ((TextView) findViewById(R.id.etPlannedFromDateList)).setText("");

        ((TextView) findViewById(R.id.etPlannedFromDateList)).requestFocus();
    }

    /**
     * Buscar por letra se implementan con SearchView.OnQueryTextListener en la clase
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Buscar por letra: se implementan con SearchView.OnQueryTextListener en la clase
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        name = newText;
        signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
        presenter.loadAllSings(firstDay, secondDay, name);

        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
        return false;
    }
}