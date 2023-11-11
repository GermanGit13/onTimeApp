package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.User;

public class SignRegisterView extends AppCompatActivity implements SignRegisterContract.View {

    private Context context;
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    ImageView ivPhotoMenu;

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
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void resetForm() {

    }

}