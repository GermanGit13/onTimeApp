package com.svalero.ontimeapp.view;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.SignListByUserPresenter;
import com.svalero.ontimeapp.util.SavePreference;

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
    private Button btRegisterLate;
    private Button btBookingDesk;
    private Button btListMySings;
    private Button btListAllSings;
    private String photoUrl;
    private androidx.appcompat.widget.Toolbar toolbar;

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

        /**
         * Salvar los datos de usuario en SharedPreference
         */
        SavePreference.setSavePreference("userId", String.valueOf(user.getId()), this);
        SavePreference.setSavePreference("username", String.valueOf(user.getUsername()), this);
        SavePreference.setSavePreference("rol", String.valueOf(user.getRol()), this);
        SavePreference.setSavePreference("name", String.valueOf(user.getName()), this);
        SavePreference.setSavePreference("surname", String.valueOf(user.getSurname()), this);
        SavePreference.setSavePreference("department", String.valueOf(user.getDepartment()), this );
        SavePreference.setSavePreference("userPhoto", String.valueOf(user.getPhoto()), this);

        /**
         * Toolbar: http://www.androidcurso.com/index.php/473
         */
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( "Prueba Titulo" );
        getSupportActionBar().setIcon(R.drawable.logo);

        presenter = new SignListByUserPresenter(this);
        notifiedNoSign(); // Para saber si ficho el día anterior

        Log.d("MenuPrincipal", "Ver si traigo el user: " + user.getId() + " photo: " + user.getPhoto());
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvName = findViewById(R.id.tv_Name);
        tvName.setText(SavePreference.getSavePreference("name", this));
        ivPhotoMenu = findViewById(R.id.iv_PhotoMenu);
        Glide.with(this)
                .load(SavePreference.getSavePreference("userPhoto", this))
                .error(R.drawable.notphoto)
                .into(ivPhotoMenu);

        if (user.getRol().equals("MANAGER") || user.getRol().equals("USER")) {
            btListAllSings = findViewById(R.id.btListAllSigns);
            btListAllSings.setVisibility(View.GONE);
        }

        if(user.getRol().equals("USER")) {
            btListSignsDepartment = findViewById(R.id.btListSignsByDepartment);
            btListSignsDepartment.setVisibility(View.GONE);
        }

        btRegisterSign = findViewById(R.id.btRegisterSing);
        btRegisterSign.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignRegisterView.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        btRegisterLate = findViewById(R.id.btRegisterSingLate);
        btRegisterLate.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignRegisterLateView.class);
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
        date = date.minusDays(1);
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