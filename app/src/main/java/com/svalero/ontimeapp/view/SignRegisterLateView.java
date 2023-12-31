package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Dto.SignOutDto;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignRegisterPresenter;
import com.svalero.ontimeapp.util.Calendario;
import com.svalero.ontimeapp.util.SavePreference;
import com.svalero.ontimeapp.util.TimePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SignRegisterLateView extends AppCompatActivity implements SignRegisterContract.View {

    private SignRegisterPresenter presenter;
    private String day;
    private String modality;
    private String scheduleIn;
    private String scheduleOut;
    private String incidence_in = "";
    private String incidence_out = "";
    private Bundle bundle; // creamos un bundle para recoger el objeta extra enviado que esta serializable
    private User user;
    private Button btIncreaseDay;
    private Button btDecreateDay;
    private Button btRegisterLateDay;
    private EditText tvDLateDay;
    private TextView tvScheduleIn;
    private TextView tvScheduleOut;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_register_late_view);

        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");

        /**
         * Toolbar: http://www.androidcurso.com/index.php/473
         */
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.tbRegisterLate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( "Register your missing signings" );
//        getSupportActionBar().setIcon(R.drawable.logo);

        Log.d("List Sign Register Late", "Llamada desde view "); // depurar para ver hasta donde llego

        presenter = new SignRegisterPresenter(this);

        initializeDatePicker();
        initializeTimePicker();

        btIncreaseDay = findViewById(R.id.btincrement_register_late);
        btIncreaseDay.setOnClickListener(view -> {
            increaseDaySearchFromRegisterLate();
        });

        btDecreateDay = findViewById(R.id.btdecrement_register_late);
        btDecreateDay.setOnClickListener(view -> {
            subtractDaySearchFromRegisterLate();
        });

    }

    /**
     * Método para el Calendario
     */
    private void initializeDatePicker() {
        tvDLateDay = findViewById(R.id.tvPlannedDateRegisterLate);

        Calendario datePicketTextTo = new Calendario();
        datePicketTextTo.datepickerTextView(tvDLateDay, tvDLateDay, this);
    }

    /**
     * Método para el TimePicker
     */
    private void initializeTimePicker() {
        tvScheduleIn = findViewById(R.id.tv_in_register_late);
        tvScheduleOut = findViewById(R.id.tv_out_register_late);
        tvScheduleIn.setText(SavePreference.getSavePreference("scheduleIn", this));
        tvScheduleOut.setText(SavePreference.getSavePreference("scheduleOut", this));

        TimePicker timePickerIn = new TimePicker();
        timePickerIn.timePickerTextView(tvScheduleIn, tvScheduleIn, this);
        TimePicker timePickerOut = new TimePicker();
        timePickerOut.timePickerTextView(tvScheduleOut, tvScheduleOut,this);
    }


    @Override
    public void showError(String errorMessage) {
        Snackbar.make(((EditText) findViewById(R.id.et_snackback)), errorMessage,
                BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        snackbar.make(((EditText) findViewById(R.id.tvPlannedDateRegisterLate)), message, BaseTransientBottomBar.LENGTH_SHORT)
                .setAction(R.string.accept, new View.OnClickListener() { // Crea un boton en el snackbar
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    @Override
    public void resetForm() {

    }

    public void registerLateSign(View view) {
//        Log.d("Register Sign Late", "Registrar sing atrasada: " );
        day = tvDLateDay.getText().toString();
        modality = SavePreference.getSavePreference("modality", this);
//        scheduleIn = SavePreference.getSavePreference("scheduleIn", this);
//        scheduleOut = SavePreference.getSavePreference("scheduleOut", this);
        scheduleIn = tvScheduleIn.getText().toString();
        scheduleOut = tvScheduleOut.getText().toString();

//        Log.d("Register Sign Late", "Registrar sing atrasada: " + modality + " / " + scheduleIn + " / " + scheduleOut + " / " + user.getId());
        Sign signLateDay = new Sign(modality, day, scheduleIn, scheduleOut, incidence_in, incidence_out, user);
        presenter.registerSign(user.getId(), signLateDay);
    }

    /**
     * Añadir un día a la fecha seleccionada en el calendario que aparece en el TextView
     */
    public void increaseDaySearchFromRegisterLate() {
        day = tvDLateDay.getText().toString();
        if (day.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            day = String.valueOf(dateNow.format(dateTimeFormatter));
        }

        LocalDate masOne = LocalDate.parse(day);
        masOne = masOne.plusDays(1);
        day= String.valueOf(masOne);
        tvDLateDay.setText(masOne.toString());
    }

    /**
     * Quitar un día a la fecha seleccionada en el calendario que aparece en el TextView
     */
    public void subtractDaySearchFromRegisterLate() {
        day = tvDLateDay.getText().toString();
        if (day.equals("")) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            day = String.valueOf(dateNow.format(dateTimeFormatter));
        }

        LocalDate masOne = LocalDate.parse(day);
        masOne = masOne.minusDays(1);
        day= String.valueOf(masOne);
        tvDLateDay.setText(masOne.toString());
    }

    /**
     * Para crear el menu (toolbar)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true; /** true -> el menú ya está visible */
    }

    /**
     * Para cuando elegimos una opcion del menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itMyList) {
            Intent intent = new Intent(this, SignListByUserView.class);
            intent.putExtra("user", user);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.itPreferences) {
            Intent intent = new Intent(this, SignPreference.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (item.getItemId() == R.id.itLogout) {
            Intent intent = new Intent(this, LoginView.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}