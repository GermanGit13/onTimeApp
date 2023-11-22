package com.svalero.ontimeapp.lib;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.ontimeapp.R;
import com.svalero.ontimeapp.view.MainActivity;
import com.svalero.ontimeapp.view.SignRegisterView;

public class SomeMessageAlert {

    //            snackbar.make(((EditText) findViewById(R.id.et_snackback)), (message + sign.getUser().getUsername() + " in day: " + sign.getDay()), BaseTransientBottomBar.LENGTH_INDEFINITE)
//                    .setAction(R.string.accept, new View.OnClickListener() { //Crea un boton en el snackbar
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(SignRegisterView.this, MainActivity.class);
//                            intent.putExtra("user", user); // Mandamos el objeto entero ya que es una clase serializable
//                            startActivity(intent);
//                        }
//                    })
//                    .show();


//        new MaterialAlertDialogBuilder(this)
//                .setTitle("Sign Register")
//                .setMessage(R.string.you_must_select_a_modality_in_order_to_register_your_sign)
//                .setPositiveButton(message + sign.getUser().getUsername() + getString(R.string.in_day) + sign.getDay(), new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//            Intent intent = new Intent(SignRegisterView.this, MainActivity.class);
//            intent.putExtra("user", user); // Mandamos el objeto entero ya que es una clase serializable
//            startActivity(intent);
//        }
//    })
//            .show();


//            Snackbar.make(((EditText) findViewById(R.id.et_snackback)), errorMessage,
//    BaseTransientBottomBar.LENGTH_LONG).show();



    //            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.sign_with_incidence);
//            builder.setMessage(R.string.do_you_want_to_register_a_incidence_in_your_sign);
//            builder.setPositiveButton(R.string.accept, null);
//            AlertDialog dialog = builder.create();
//            dialog.show();
}
