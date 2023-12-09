package com.svalero.ontimeapp.view;

import static android.view.View.GONE;

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
import com.google.android.material.textfield.TextInputLayout;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.UserPassDtoContract;
import com.svalero.ontimeapp.domain.Dto.UserPassDto;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.UserPassDtoPresenter;
import com.svalero.ontimeapp.util.SavePreference;
import com.svalero.ontimeapp.util.TimePicker;

public class SignPreference extends AppCompatActivity implements UserPassDtoContract.View {

    private UserPassDtoPresenter presenter;
    private User user;
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private Context context;
    private ImageView ivPhotoMenu;
    private Button btChangePass;
    private Button btSavePass;
    private Button btSavePreferences;
    private EditText etNewPass;
    private TextInputLayout tilNewPass;
    private TextInputLayout tilName;
    private EditText etName;
    private TextView tvNamePreferences;
    private TextView tvSurnamePreferences;
    private TextView tvDepartmentPreferences;
    private TextView tvModalityPreferences;
    private TextView tvScheduleInPreferences;
    private TextView tvScheduleOutPreferences;
    private Spinner spinnerModality;
    private String modalityPreference;
    private String scheduleInPreference;
    private String scheduleOutPreference;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_preference);
        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userId = String.valueOf(user.getId());

        ivPhotoMenu = findViewById(R.id.ivPhotoPreferences);
        Glide.with(this)
                .load(user.getPhoto())
                .error(R.drawable.notphoto)
                .into(ivPhotoMenu);

        fillData();
        initializeSpinnerModality();
        initializeTimePicker();
        presenter = new UserPassDtoPresenter(this);

        btSavePass = findViewById(R.id.btSavePass);
        etNewPass = findViewById(R.id.etPassPreferences);
        tilNewPass = findViewById(R.id.tilPassPreferences);
        btChangePass = findViewById(R.id.btChangePass);
        btChangePass.setOnClickListener(view -> {
            if(btSavePass.getVisibility()  == GONE && tilNewPass.getVisibility() == GONE && etNewPass.getVisibility() == GONE) {
                btSavePass.setVisibility(View.VISIBLE);
                etNewPass.setVisibility(View.VISIBLE);
                tilNewPass.setVisibility(View.VISIBLE);
            } else {
                btSavePass.setVisibility(GONE);
                etNewPass.setVisibility(GONE);
                tilNewPass.setVisibility(GONE);
                etNewPass.setText("");
            }
        });

        btSavePreferences = findViewById(R.id.btSavePreferences);
        btSavePreferences.setOnClickListener(view -> {
            savePreferences();
        });
    }

    public void fillData() {

        tvNamePreferences = findViewById(R.id.tvNamePreference);
        tvSurnamePreferences = findViewById(R.id.tvSurnamePreferences);
        tvDepartmentPreferences = findViewById((R.id.tvDepartmentPreferences));
        tvModalityPreferences = findViewById(R.id.tvModalityPreferences);
        tvScheduleInPreferences = findViewById(R.id.tv_horario_in);
        tvScheduleOutPreferences = findViewById(R.id.tv_horario_out);

        tvNamePreferences.setText(SavePreference.getSavePreference("name", this));
        tvSurnamePreferences.setText(SavePreference.getSavePreference("surname", this));
        tvDepartmentPreferences.setText(SavePreference.getSavePreference("department", this));
        tvModalityPreferences.setText(SavePreference.getSavePreference("modality", this));
        tvScheduleInPreferences.setText(SavePreference.getSavePreference("scheduleIn", this));
        tvScheduleOutPreferences.setText(SavePreference.getSavePreference("scheduleOut", this));
    }

    public void initializeSpinnerModality() {
        spinnerModality = (Spinner) findViewById(R.id.preference_spinner);
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
                modalityPreference = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Método para el TimePicker
     */
    private void initializeTimePicker() {
        tvScheduleInPreferences = findViewById(R.id.tv_horario_in);
        tvScheduleOutPreferences = findViewById(R.id.tv_horario_out);

        TimePicker timePickerIn = new TimePicker();
        timePickerIn.timePickerTextView(tvScheduleInPreferences, tvScheduleInPreferences, this);
        TimePicker timePickerOut = new TimePicker();
        timePickerOut.timePickerTextView(tvScheduleOutPreferences, tvScheduleOutPreferences, this);
    }

    public void savePreferences() {
        /**
         * Recoger el horario de los timepicker
         */
        scheduleInPreference = tvScheduleInPreferences.getText().toString();
        scheduleOutPreference = tvScheduleOutPreferences.getText().toString();

        SavePreference.setSavePreference("modality", modalityPreference, this);
        SavePreference.setSavePreference("scheduleIn", scheduleInPreference, this);
        SavePreference.setSavePreference("scheduleOut", scheduleOutPreference, this);

        fillData();
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar.make(((EditText) findViewById(R.id.et_snackback)), errorMessage,
                BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Update Pass")
                .setMessage("Password correctly updated  ")
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

   public void updatePass(View view) {
       Log.d("Update Pass User", "Cambiar la contraseña de usuario:");
       etNewPass = findViewById(R.id.etPassPreferences);

       String pass = etNewPass.getText().toString();

       UserPassDto userPassDto = new UserPassDto();
       userPassDto.setPass(pass);

       presenter.updatePass(user.getId(), userPassDto);

       if(btSavePass.getVisibility()  == GONE && tilNewPass.getVisibility() == GONE && etNewPass.getVisibility() == GONE) {
           btSavePass.setVisibility(View.VISIBLE);
           etNewPass.setVisibility(View.VISIBLE);
           tilNewPass.setVisibility(View.VISIBLE);
       } else {
           btSavePass.setVisibility(GONE);
           etNewPass.setVisibility(GONE);
           tilNewPass.setVisibility(GONE);
           etNewPass.setText("");
       }
   }
}