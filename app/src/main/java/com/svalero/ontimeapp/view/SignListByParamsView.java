package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.adapter.SignAdapter;
import com.svalero.ontimeapp.contract.SignListByParamsContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByParamsPresenter;
import com.svalero.ontimeapp.util.Calendario;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 * Implemento SearchView.OnQueryTextListener para poder hacer busquedas por nombre
 */
public class SignListByParamsView extends AppCompatActivity implements SignListByParamsContract.View, SearchView.OnQueryTextListener {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignAdapter adapter; // Declaramos el adapter
    private SignListByParamsPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para recoger el objeta extra enviado que esta serializable
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
    private Button btIncreaseDayFrom;
    private Button btDecreateDayFrom;
    private Button btIncreaseDayTo;
    private Button btDecreateDayTo;
    private Button btLastSevenDays;
    private SearchView svSearchParams;


    @SuppressLint("MissingInflatedId")
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

        btIncreaseDayFrom = findViewById(R.id.bt_list_paramsFrom_increase);
        btIncreaseDayFrom.setOnClickListener(view -> {
            increaseDaySearchFrom();
        });

        btDecreateDayFrom = findViewById(R.id.bt_list_paramsFrom_decrement);
        btDecreateDayFrom.setOnClickListener(view -> {
            subtractDaySearchFrom();
        });

        btIncreaseDayTo = findViewById(R.id.bt_list_paramsTo_increase);
        btIncreaseDayTo.setOnClickListener(view -> {
            increaseDaySearchTo();
        });

        btDecreateDayTo = findViewById(R.id.bt_list_paramsTo_decrement);
        btDecreateDayTo.setOnClickListener(view -> {
            subtractDaySearchTo();
        });

        btLastSevenDays = findViewById(R.id.bt_list_params_seven_days);
        btLastSevenDays.setOnClickListener(view -> {
            searchParamsLastSevenDays();
        });

        svSearchParams = findViewById(R.id.svSearchParams);
        svSearchParams.setOnQueryTextListener(this); // uso la implementacion con sus métodos para buscar por nombre
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
        etPlannedDateToParams =findViewById(R.id.etPlannedToDateParams);
        btPickDate = findViewById(R.id.bt_pick_date_list_params);
        btPickDateTo = findViewById(R.id.bt_pick_date_list_paramsTo);


        Calendario datePickerFrom = new Calendario();
        datePickerFrom.datepicker(btPickDate, etPlannedDateFromParams, this);
        Calendario datePickerTo = new Calendario();
        datePickerTo.datepicker(btPickDateTo, etPlannedDateToParams, this);
        Calendario datePicketTextFrom = new Calendario();
        datePicketTextFrom.datepickerTextView(etPlannedDateFromParams, etPlannedDateFromParams, this);
        Calendario datePicketTextTo = new Calendario();
        datePicketTextTo.datepickerTextView(etPlannedDateToParams, etPlannedDateToParams, this);
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

        Log.d("List Sign with Params", "Llamada desde view loadSignsByParams with Params: " + firstDay + " / " + department);
    }

    public void increaseDaySearchFrom() {
        firstDay = etPlannedDateFromParams.getText().toString();
        if (firstDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            firstDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(firstDay);
        masOne = masOne.plusDays(1);
        firstDay= String.valueOf(masOne);
        etPlannedDateFromParams.setText(masOne.toString());

        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void subtractDaySearchFrom() {
        firstDay = etPlannedDateFromParams.getText().toString();
        if (firstDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            firstDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(firstDay);
        masOne = masOne.minusDays(1);
        firstDay= String.valueOf(masOne);
        etPlannedDateFromParams.setText(masOne.toString());

        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void increaseDaySearchTo() {
        secondDay = etPlannedDateToParams.getText().toString();
        if (secondDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            secondDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(secondDay);
        masOne = masOne.plusDays(1);
        secondDay= String.valueOf(masOne);
        etPlannedDateToParams.setText(masOne.toString());

        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void subtractDaySearchTo() {
        secondDay = etPlannedDateToParams.getText().toString();
        if (secondDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            secondDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(secondDay);
        masOne = masOne.minusDays(1);
        secondDay= String.valueOf(masOne);
        etPlannedDateToParams.setText(masOne.toString());

        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    /**
     * Buscar ultimos siete dias
     */
    public void searchParamsLastSevenDays() {
        resetDay();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        LocalDate sevenDay;
        sevenDay = dateNow.minusDays(7);

        firstDay = String.valueOf(sevenDay.format(dateTimeFormatter));
        secondDay = String.valueOf(dateNow.format(dateTimeFormatter));

        Log.d("List Sign Params", "Llamada desde view showSigns 7 dias: " + firstDay + " / " + secondDay + " / " + name);
        presenter.loadSignsByParams(department, firstDay, secondDay, name);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    /**
     * Limiar filtros de busqueda
     */
    public void resetDay() {
        ((TextView) findViewById(R.id.etPlannedDateParams)).setText("");
        ((TextView) findViewById(R.id.etPlannedToDateParams)).setText("");

        ((TextView) findViewById(R.id.etPlannedDateParams)).requestFocus();
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
//        String failSecondDay = "";
        name = newText;
        signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
        presenter.loadSignsByParams(user.getDepartment(), firstDay, secondDay, name);

        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
        return false;
    }
    // Todo Falta añadir Menu actionBar
}