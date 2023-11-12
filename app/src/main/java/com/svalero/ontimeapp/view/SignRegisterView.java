package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignRegisterPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class SignRegisterView extends AppCompatActivity implements SignRegisterContract.View {

    private SignRegisterPresenter presenter;
    private Context context;
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private Snackbar snackbar;
    private User user;
    ImageView ivPhotoMenu;
    TextView tvInRegister;
    TextView tvOutRegister;
    RadioGroup groupIncidence;
    RadioGroup groupModality;
    LocalDate day;
    LocalTime in_time;
    LocalTime out_time;
    String modality;
    String incidence_in;
    String incidence_out;


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
        ivPhotoMenu = findViewById(R.id.iv_photo_register);
        Glide.with(this)
                .load(user.getPhoto())
                .error(R.drawable.notphoto)
                .into(ivPhotoMenu);

        groupIncidence = (RadioGroup) findViewById(R.id.rg_incidence);
        groupModality = (RadioGroup) findViewById(R.id.rg_modality);

        presenter = new SignRegisterPresenter(this);
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(((EditText) findViewById(R.id.et_snackback)), errorMessage,
                BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
//          Opción para crear un mensaje  con un boton
            snackbar.make(((EditText) findViewById(R.id.et_snackback)), message, BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("Entrar", new View.OnClickListener() { //Crea un boton en el snackbar
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
        LocalDate day = LocalDate.now();
//        DateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        LocalTime in_time = LocalTime.now();

        Log.d("Register Sign", "Ver el día y la hora que recojo: " + day + " - " + in_time);

        if (groupModality.isActivated()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.assign_a_modality_for_register_you_sign);
            builder.setMessage(R.string.select_office_or_homework);
            builder.setPositiveButton(R.string.accept, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (groupModality.getCheckedRadioButtonId() == R.id.rb_office) {
            modality = getString(R.string.officeBD);
        } else {
            modality = getString(R.string.homeworkBD);
        }
        Log.d("Register Sign", "Ver la modalidad seleccionada: " + modality);

        if (groupIncidence.isActivated()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.sign_with_incidence);
            builder.setMessage(R.string.do_you_want_to_register_a_incidence_in_your_sign);
            builder.setPositiveButton(R.string.accept, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (groupIncidence.getCheckedRadioButtonId() == R.id.rb_holidays) {
            incidence_in = getString(R.string.holidaysbd);
        } else if (groupIncidence.getCheckedRadioButtonId() == R.id.rb_medical) {
            incidence_in = getString(R.string.medicalbd);
        } else if (groupIncidence.getCheckedRadioButtonId() == R.id.rb_personal){
            incidence_in = getString(R.string.personalbd);
        }
        Log.d("Register Sign", "Ver la incidencia seleccionada: " + incidence_in);

        Sign sign = new Sign(modality, day, in_time, out_time, incidence_in, incidence_out, user);
        presenter.registerSign(user.getId(), sign);
    }

    public void goBackButton(View view) {
        onBackPressed();
    }
}