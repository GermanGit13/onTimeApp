package com.svalero.ontimeapp.model;

import android.content.Context;
import android.util.Log;

import com.svalero.ontimeapp.api.OnTimeApi;
import com.svalero.ontimeapp.api.OnTimeApiInterface;
import com.svalero.ontimeapp.contract.SignListByUserContract;
import com.svalero.ontimeapp.domain.Sign;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignListByUserModel implements SignListByUserContract.Model {

    private Context context; //para poder pasarle el contexto de la aplicacion

    public SignListByUserModel(Context context) {
        this.context = context;
    }

    @Override
    public void loadSignsByUser(String userId, OnLoadSignsByUserListener listener) {
        //Nos devuelve una instancia de onTimeApi como la definimos en OnTimeApiInterface, tiene los métodos que usamos para comunicarnos con la API
        OnTimeApiInterface onTimeApi = OnTimeApi.buildInstance();
        Call<List<Sign>> callSigns = onTimeApi.findByUserInSign(userId);
        Log.d("List Sign By User", "Llamada desde el model"); //Para depurar errores y ver si avanza o donde se para
        callSigns.enqueue(new Callback<List<Sign>>() {
            @Override
            public void onResponse(Call<List<Sign>> call, Response<List<Sign>> response) {
                Log.d("List Sign By User", "Llamada desde el model OK"); //Para depurar errores y ver si avanza o donde se para
                List<Sign> signs = response.body();
                listener.OnLoadSignsByUserListenerSucess(signs);
            }

            @Override
            public void onFailure(Call<List<Sign>> call, Throwable t) {
                Log.d("List Sign By User", "Llamada desde el model ERROR"); //Para depurar errores y ver si avanza o donde se para
                t.printStackTrace();
                String message = "Error invocando la operación";
                listener.OnLoadSignsByUserListenerError(message);
            }
        });
    }
}
