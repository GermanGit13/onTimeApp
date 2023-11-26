package com.svalero.ontimeapp.view;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.util.SavePreference;

public class SignPreference extends AppCompatActivity {

    private ImageView ivPhotoMenu;
    private Button btChangePass;
    private Button btSavePass;
    private EditText etNewPass;
    private TextInputLayout tilNewPass;
    private TextInputLayout tilName;
    private EditText etName;
    private TextView tvNamePreferences;
    private TextView tvSurnamePreferences;
    private TextView tvDepartmentPreferences;

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
    }

    public void fillData() {
        tvNamePreferences = findViewById(R.id.tvNamePreference);
        tvSurnamePreferences = findViewById(R.id.tvSurnamePreferences);
        tvDepartmentPreferences = findViewById((R.id.tvDepartmentPreferences));

        tvNamePreferences.setText(SavePreference.getSavePreference("name", this));
        tvSurnamePreferences.setText(SavePreference.getSavePreference("surname", this));
        tvDepartmentPreferences.setText(SavePreference.getSavePreference("department", this));
    }
}