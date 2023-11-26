package com.svalero.ontimeapp.view;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.util.SavePreference;

public class SignPreference extends AppCompatActivity {

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
    private Spinner spinnerModality;
    private String modalityPreference = "1";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_preference);

        ivPhotoMenu = findViewById(R.id.ivPhotoPreferences);
        Glide.with(this)
                .load(SavePreference.getSavePreference("userPhoto", this))
                .error(R.drawable.notphoto)
                .into(ivPhotoMenu);

        fillData();
        initializeSpinnerModality();

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

        tvNamePreferences.setText(SavePreference.getSavePreference("name", this));
        tvSurnamePreferences.setText(SavePreference.getSavePreference("surname", this));
        tvDepartmentPreferences.setText(SavePreference.getSavePreference("department", this));
        tvModalityPreferences.setText(SavePreference.getSavePreference("modality", this));
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

    public void savePreferences() {
        SavePreference.setSavePreference("modality", modalityPreference, this);
        fillData();
    }
}