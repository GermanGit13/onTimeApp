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
import android.widget.TextView;
import android.widget.Toast;

import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.adapter.SignAdapter;
import com.svalero.ontimeapp.adapter.SignByUserAdapter;
import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByUserPresenter;
import com.svalero.ontimeapp.util.Calendario;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SignListByUserView extends AppCompatActivity implements SignListByUserContract.View {

    private Context context;
    private List<Sign> signsList; // Creamos la lista que vamos a recibir
    private SignByUserAdapter adapter; // Declaramos el adapter
    private SignListByUserPresenter presenter; // Declaramos el presenter para solicitar los datos
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private String userId;
    private Button btPickDateFrom;
    private Button btPickDateTo;
    private EditText etPlannedDateToUser;
    private EditText etPlannedDateFromUser;
    private String firstDay = "";
    private String secondDay = "";
    private Button btSearchByUser;
    private Button btClearByUser;
    private Button btIncreaseDayFrom;
    private Button btDecreateDayFrom;
    private Button btIncreaseDayTo;
    private Button btDecreateDayTo;
    private Button btSearchLastDays;

    @SuppressLint("MissingInflatedId")
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
        presenter.loadSignsByUser(userId, firstDay, secondDay);
        initializeRecyclerView(); //inicializamos el RecyclerView
        initializeDatePicker(); //Inicializamos el DatePicker

        btSearchByUser = findViewById(R.id.bt_list_user_find);
        btSearchByUser.setOnClickListener(view -> {
            findSignsUserByDay();
        });

        btClearByUser = findViewById(R.id.bt_list_user_clear);
        btClearByUser.setOnClickListener(view -> {
            resetUserByDay();
        });

        btIncreaseDayFrom = findViewById(R.id.bt_list_userFrom_increase);
        btIncreaseDayFrom.setOnClickListener(view -> {
            increaseDaySearchFrom();
        });

        btDecreateDayFrom = findViewById(R.id.bt_list_userFrom_decrement);
        btDecreateDayFrom.setOnClickListener(view -> {
            subtractDaySearchFrom();
        });

        btIncreaseDayTo = findViewById(R.id.bt_list_userTo_increase);
        btIncreaseDayTo.setOnClickListener(view -> {
            increaseDaySearchTo();
        });

        btDecreateDayTo = findViewById(R.id.bt_list_userTo_decrement);
        btDecreateDayTo.setOnClickListener(view -> {
            subtractDaySearchTo();
        });

        btSearchLastDays = findViewById(R.id.bt_list_user_seven_days);
        btSearchLastDays.setOnClickListener(view -> {
            searchLastSevenDaysByUser();
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
        adapter = new SignByUserAdapter(this, signsList); // se lo pasamos al adapter para que pinte los datos de cada fichaje de la lista en el item
        recyclerView.setAdapter(adapter);
    }

    /**
     * Método para el Calendario
     */
    private void initializeDatePicker() {
        etPlannedDateFromUser = findViewById(R.id.etPlannedDateFromUser);
        etPlannedDateToUser = findViewById(R.id.etPlannedDateToUser);
        btPickDateTo = findViewById(R.id.bt_pick_date_list_userFrom);
        btPickDateFrom = findViewById(R.id.bt_pick_date_list_userTo);

        Calendario datePickerFrom = new Calendario();
        datePickerFrom.datepicker(btPickDateTo, etPlannedDateToUser, this);
        Calendario datePickerTo = new Calendario();
        datePickerTo.datepicker(btPickDateFrom, etPlannedDateFromUser, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        Log.d("List Sign By User and Day", "Llamada desde view"); // depurar para ver hasta donde llego
        presenter.loadSignsByUser(userId, firstDay, secondDay);
    }

    @Override
    public void showSignsByUser(List<Sign> signs) {
        signsList.clear();
        signsList.addAll(signs);
        adapter.notifyDataSetChanged();
//        if (signs.isEmpty()) {
//            new MaterialAlertDialogBuilder(this)
//                    .setTitle(R.string.not_found_data_in_this_day)
//                    .setMessage(R.string.there_is_no_data_for_the_selected_date)
//                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String failDay = "";
//                            presenter.loadSignsByUser(userId, failDay);
//                            signsList.clear(); // Limpiamos la lista para evitar que tenga datos previos
//                            signsList.addAll(signs); // Añadimos a la lista creada la que recibimos
//                            adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
//                        }
//                    })
//                    .show();
//        }
        Log.d("List Sign By User and Day", "Llamada desde view_showSignsByDepartment: ");
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void findSignsUserByDay() {
        SimpleDateFormat fechaFormateada = new SimpleDateFormat("dd-MM-yyyy");
        firstDay = etPlannedDateFromUser.getText().toString();
        secondDay = etPlannedDateToUser.getText().toString();
        Log.d("List Sign By User and Day", "Fecha del calendario " + firstDay); // depurar para ver hasta donde llego

        presenter.loadSignsByUser(userId,firstDay, secondDay);
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

    public void increaseDaySearchFrom() {
        firstDay = etPlannedDateToUser.getText().toString();
        if (firstDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            firstDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(firstDay);
        masOne = masOne.plusDays(1);
        firstDay= String.valueOf(masOne);
        etPlannedDateFromUser.setText(masOne.toString());

        presenter.loadSignsByUser(userId, firstDay, secondDay);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void subtractDaySearchFrom() {
        firstDay = etPlannedDateFromUser.getText().toString();
        if (firstDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            firstDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate lessOne = LocalDate.parse(firstDay);
        lessOne = lessOne.minusDays(1);
        firstDay= String.valueOf(lessOne);
        etPlannedDateFromUser.setText(lessOne.toString());

        presenter.loadSignsByUser(userId, firstDay, secondDay);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void increaseDaySearchTo() {
        secondDay = etPlannedDateToUser.getText().toString();
        if (secondDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            secondDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(secondDay);
        masOne = masOne.plusDays(1);
        secondDay= String.valueOf(masOne);
        etPlannedDateToUser.setText(masOne.toString());

        presenter.loadSignsByUser(userId, firstDay, secondDay);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void subtractDaySearchTo() {
        secondDay = etPlannedDateToUser.getText().toString();
        if (secondDay.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            secondDay = String.valueOf(dateNow.format(dateTimeFormatter));
        }
        LocalDate masOne = LocalDate.parse(secondDay);
        masOne = masOne.minusDays(1);
        secondDay= String.valueOf(masOne);
        etPlannedDateToUser.setText(masOne.toString());

        presenter.loadSignsByUser(userId, firstDay, secondDay);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    /**
     * Buscar ultimos siete dias
     */
    public void searchLastSevenDaysByUser() {
        resetUserByDay();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        LocalDate sevenDay;
        sevenDay = dateNow.minusDays(7);

        firstDay = String.valueOf(sevenDay.format(dateTimeFormatter));
        secondDay = String.valueOf(dateNow.format(dateTimeFormatter));

        Log.d("List Sign User", "Llamada desde view showSigns 7 dias: " + firstDay + " / " + secondDay + " / " );
        presenter.loadSignsByUser(userId, firstDay, secondDay);
        adapter.notifyDataSetChanged(); // Notificamos al adapter los cambios
    }

    public void resetUserByDay() {
        ((TextView) findViewById(R.id.etPlannedDateFromUser)).setText("");
        ((TextView) findViewById(R.id.etPlannedDateToUser)).setText("");

        ((TextView) findViewById(R.id.etPlannedDateFromUser)).requestFocus();
    }
}