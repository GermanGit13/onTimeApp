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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByUserPresenter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Extiende de AppCompatActivity: donde hay un motón de código para usar por esos sobreescribimos los métodos de esta clase
 */
public class MainActivity extends AppCompatActivity implements SignListByUserContract.View {

    private Context context;
    private SignListByUserPresenter presenter; // Declaramos el presenter para solicitar los datos

    private Bundle bundle; // creamos un bundle para crecoger el objeta extra enviado que esta serializable
    private User user;
    private String userId;
    private ImageView ivPhotoMenu;
    private Button btListSignsDepartment;
    private Button btRegisterSign;
    private Button btBookingDesk;
    private Button btListMySings;
    private Button btListAllSings;
    private String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Recuperamos el objeto selecciona en el adapterUSer
         */
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userId = String.valueOf(user.getId());
        photoUrl = user.getPhoto();

        presenter = new SignListByUserPresenter(this);
        notifiedNoSign(); // Para saber si ficho el día anterior

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

    public void notifiedNoSign() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now();
        String firstDay = String.valueOf(date.format(dateTimeFormatter));
        String secondDay = "";

        presenter.loadSignsByUser(userId, firstDay, secondDay);
    }

    @Override
    public void showSignsByUser(List<Sign> signs) {
        if (signs.isEmpty()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.no_signing)
                    .setMessage(R.string.there_is_no_record_of_a_signing_yesterday)
                    .setPositiveButton(R.string.register_sign, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO decidir si quiero mandarle a crear un fichaje nuevo
                            Intent intent = new Intent(context, SignRegisterView.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.back), ((dialog, id) -> dialog.dismiss()))
                    .show();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}