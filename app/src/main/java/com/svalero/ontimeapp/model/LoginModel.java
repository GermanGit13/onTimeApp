package com.svalero.ontimeapp.model;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.svalero.ontimeapp.api.OnTimeApi;
import com.svalero.ontimeapp.api.OnTimeApiInterface;
import com.svalero.ontimeapp.contract.LoginContract;
import com.svalero.ontimeapp.domain.User;
import com.svalero.ontimeapp.presenter.LoginPresenter;

import java.sql.SQLClientInfoException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *Implementamos el contract LoginContract.Model
 */
public class LoginModel  implements LoginContract.Model {
    private LoginPresenter presenter;
    @Override
    public void getLogin(String username, String pass, OnLoginListener listener) {
         /**
         * Podemos llamar a la APi o a una BBDD
         */
        try {
            OnTimeApiInterface onTimeApi = OnTimeApi.buildInstance();
            Call<User> callUsers = onTimeApi.getLogin(username, pass);
            Log.d("login", "Llamada desde el model"); //Para depurar errores y ver si avanza o donde se para
            callUsers.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d("login", "Llamada desde el model OK"); //Para depurar errores y ver si avanza o donde se para
                    User user = response.body();
                    listener.onLoginSuccess(user); // recibimos el usuario por el listener
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("login", "Llamada desde el model ERROR"); //Para depurar errores y ver si avanza o donde se para
                    t.printStackTrace();
                    String message = "Usuario o Contraseña incorrectos";
                    listener.onLoginError(message);

                }
            });

        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
        }
    }
}
