package com.svalero.ontimeapp.view;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByUserPresenter;
import com.svalero.ontimeapp.presenter.SignRegisterPresenter;
import com.svalero.ontimeapp.util.SavePreference;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

public class SignRegisterView extends AppCompatActivity implements SignRegisterContract.View, SignListByUserContract.View {

    private SignRegisterPresenter presenter;
    private SignListByUserPresenter userPresenter;
    private Context context;
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private Snackbar snackbar;
    private User user;
    private Sign sign;
    private ImageView ivPhotoMenu;
    private TextView tvInRegister;
    private TextView tvOutRegister;
    private TextView tvHourRegister;
    private Spinner spinnerModality;
    private Spinner spinnerIncidence;
    private String day;
    private String  in_time;
    private String out_time;
    private String modality = "";
    private String incidence_in;
    private String incidence_out;
    private String modalityPreferences;
    private Button btIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_register_view);

        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");

        Log.d("Register Sign", "Ver si traigo el user: " + user.getId() + " photo: " + user.getPhoto());
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvName = findViewById(R.id.tv_name_register);
        tvName.setText(String.valueOf(user.getName()));
        ivPhotoMenu = findViewById(R.id.iv_register_photo);
        Glide.with(this)
                .load(user.getPhoto())
                .error(R.drawable.notphoto)
                .into(ivPhotoMenu);

        initializeSpinnerModality();
        initializeSpinnerIncidence();
        presenter = new SignRegisterPresenter(this);
        userPresenter = new SignListByUserPresenter(this);

        updateInTime();
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(((EditText) findViewById(R.id.et_snackback)), errorMessage,
                BaseTransientBottomBar.LENGTH_LONG).show();
    }

    /**
     * Para traernos una lista con los posibles fichajes en el dia y poder actualizar la hora en la vista y calcular el tiempo trabajado
     */
    @SuppressLint("WrongViewCast")
    @Override
    public void showSignsByUser(List<Sign> signs) {
        if (!signs.isEmpty()) {
            for (Sign sign  : signs) {
                if (sign.getIn_time() != null && sign.getOut_time() == null)  {
                    tvInRegister = findViewById(R.id.tv_in_register);
                    tvInRegister.setText(sign.getIn_time());
                    tvHourRegister = findViewById(R.id.tv_hours_register);
                    btIn = findViewById(R.id.bt_in);
                    btIn.setVisibility(View.GONE); // Quitamos el botón de IN

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime in = LocalTime.parse(sign.getIn_time());
                    LocalTime out = LocalTime.now(); // para saber la hora del móvil y calcular las horas
                    long jornada = (Duration.between(in, out).getSeconds());
                    long hora = (jornada / 3600);
                    long minutos = ((jornada - (3600 * hora)) / 60);
                    long segundos = (minutos-((hora*3600)+(minutos*60)));
                    String jornadaCompleta = hora + ":" + minutos + ": 00 " ;

                    tvHourRegister.setText(jornadaCompleta);
                }
                Log.d("Register Sign", String.valueOf(sign));
            }
        }
    }

    public void updateInTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now();
        String firstDay = String.valueOf(date.format(dateTimeFormatter));
        String secondDay = "";
        String userId = String.valueOf(user.getId());
        userPresenter.loadSignsByUser(userId, firstDay, secondDay);
    }

    @Override
    public void showMessage(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sign_register)
                .setMessage(message + sign.getUser().getUsername() + getString(R.string.in_day) + sign.getDay())
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SignRegisterView.this, MainActivity.class);
                        intent.putExtra("user", user); // Mandamos el objeto entero ya que es una clase serializable
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void resetForm() {

    }

    public void registerInSign(View view) {
//        day = LocalDate.now().toString();
        Log.d("Register Sign", "Ver el día y la hora que recojo: " + day + " - " + in_time);

        if (modality.equals("")) {
              new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.select_modality)
                .setMessage(R.string.you_must_select_a_modality_in_order_to_register_your_sign)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
        } else {
            sign = new Sign(modality, day, in_time, incidence_in, user);
            presenter.registerSign(user.getId(), sign);
        }

        Log.d("Register Sign", "Ver la modalidad seleccionada: " + modality);
    }

    public void registerOutSign(View view) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        out_time = LocalDateTime.now().toLocalTime().format(format);
        day = LocalDate.now().toString();

        Log.d("Register Sign", "Ver el día y la hora que recojo: " + day + " - " + in_time);

        Log.d("Register Sign", "Ver la modalidad seleccionada: " + modality);

        sign = new Sign(modality, day, in_time, incidence_in, user);
        presenter.registerSign(user.getId(), sign);
    }

    public void initializeSpinnerModality() {
        modalityPreferences = SavePreference.getSavePreference("modality", this); // Recuperamos la modalidad de las preferencias
        String[] tu_spinner_array = getResources().getStringArray(R.array.modality_array); // creamos un array de String con el Array-string xml
        tu_spinner_array[0] = modalityPreferences; // asignamos a la posicion 0 la modalidad de las preferencias

        spinnerModality = (Spinner) findViewById(R.id.modality_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout.
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.modality_array,
//                android.R.layout.simple_spinner_item
//        );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tu_spinner_array);
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinnerModality.setAdapter(adapter);
        // Apply the adapter to the spinner.

        spinnerModality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modality = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initializeSpinnerIncidence() {
        spinnerIncidence= (Spinner) findViewById(R.id.incidence_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.incidence_array,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinnerIncidence.setAdapter(adapter);
        // Apply the adapter to the spinner.

        spinnerIncidence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incidence_in = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goBackButton(View view) {
        onBackPressed();
    }
}