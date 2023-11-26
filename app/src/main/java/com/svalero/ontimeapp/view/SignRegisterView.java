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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignRegisterPresenter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignRegisterView extends AppCompatActivity implements SignRegisterContract.View {

    private SignRegisterPresenter presenter;
    private Context context;
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private Snackbar snackbar;
    private User user;
    private Sign sign;
    private ImageView ivPhotoMenu;
    private TextView tvInRegister;
    private TextView tvOutRegister;
    private Spinner spinnerModality;
    private Spinner spinnerIncidence;
    private String day;
    private String  in_time;
    private String out_time;
    private String modality = "";
    private String incidence_in;
    private String incidence_out;

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


    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(((EditText) findViewById(R.id.et_snackback)), errorMessage,
                BaseTransientBottomBar.LENGTH_LONG).show();
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
        spinnerModality = (Spinner) findViewById(R.id.modality_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.modality_array,
                android.R.layout.simple_spinner_item
        );
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