package com.svalero.ontimeapp.view;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.svalero.ontimeapp.contract.SignOutDtoContract;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Dto.SignOutDto;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByUserPresenter;
import com.svalero.ontimeapp.presenter.SignOutDtoPresenter;
import com.svalero.ontimeapp.presenter.SignRegisterPresenter;
import com.svalero.ontimeapp.util.SavePreference;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SignRegisterView extends AppCompatActivity implements SignRegisterContract.View, SignListByUserContract.View, SignOutDtoContract.View {

    private SignRegisterPresenter presenter;
    private SignListByUserPresenter userPresenter;
    private SignOutDtoPresenter signOutDtoPresenter;
    private Context context;
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private Snackbar snackbar;
    private User user;
    private Sign sign;
    private long signIdOut; // para recoger el id del fichaje con entrada y pasarselo al fichaje con salida
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
    private String incidence;
    private String modalityPreferences;
    private Button btIn;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_register_view);

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
        getSupportActionBar().setTitle( "Register your sing" );
//        getSupportActionBar().setIcon(R.drawable.logo);

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
        signOutDtoPresenter = new SignOutDtoPresenter(this);

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
                    signIdOut = sign.getId(); // para guardar el id del sign con entrada y poder pasarselo al sign de salida
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
        snackbar.make(((TextView) findViewById(R.id.tv_in_register)), message, BaseTransientBottomBar.LENGTH_SHORT)
                .setAction(R.string.accept, new View.OnClickListener() { // Crea un boton en el snackbar
                    @Override
                    public void onClick(View v) {
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
//        Log.d("Register Sign", "Ver el día y la hora que recojo: " + day + " - " + in_time);

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
            sign = new Sign(modality, day, in_time, incidence, user);
            presenter.registerSign(user.getId(), sign);
        }

//        Log.d("Register Sign", "Ver la modalidad seleccionada: " + modality);
    }

    public void registerOutSign(View view) {
//        Log.d("Register Sign Out", "Ver la incidencia: " + incidence);
        SignOutDto signOutDto = new SignOutDto();
        signOutDto.setIncidende_out(incidence);

        signOutDtoPresenter.signOut(signIdOut, signOutDto);
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
                incidence = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goBackButton(View view) {
        onBackPressed();
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