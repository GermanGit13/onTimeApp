package com.svalero.ontimeapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.domain.User;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 */
public class MainActivity extends AppCompatActivity {

    private Context context;
    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    long userId;
    ImageView ivPhotoMenu;
    Button btListSignsDepartment;
    Button btRegisterSign;
    Button btBookingDesk;
    Button btListMySings;
    Button btListAllSings;
    String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userId = user.getId();
        photoUrl = user.getPhoto();


        Log.d("MenuPrincipal", "Ver si traigo el user: " + user.getId() + " photo: " + user.getPhoto());
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvName = findViewById(R.id.tv_Name);
        tvName.setText(String.valueOf(user.getName()));
        ivPhotoMenu = findViewById(R.id.iv_PhotoMenu);
        Glide.with(this)
                .load(photoUrl)
                .error(R.drawable.notphoto)
                .into(ivPhotoMenu);

        if (!user.getRol().equals("USER")) {
            btListSignsDepartment = findViewById(R.id.btListAllSigns);
            btListSignsDepartment.setVisibility(View.VISIBLE);
        }

        btRegisterSign = findViewById(R.id.btRegisterSing);
        btRegisterSign.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignRegisterView.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        btListMySings = findViewById(R.id.btListMySign);
        btListMySings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignListByUserView.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        btListSignsDepartment = findViewById(R.id.btListSignsByDepartment);
        btListSignsDepartment.setOnClickListener(view -> {
        if (user.getDepartment().equals("")) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.you_don_t_have_an_department)
                    .setMessage(R.string.contact_with_administration_depatment)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(context, MainActivity.class);
//                            intent.putExtra("user", user);
//                            startActivity(intent);
                        }
                    })
                    .show();
        } else {
            Intent intent = new Intent(this, SignListByParamsView.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        });

        btListAllSings = findViewById(R.id.btListAllSigns);
        btListAllSings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignListView.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
    }
}