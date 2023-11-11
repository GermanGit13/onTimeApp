package com.svalero.ontimeapp.model;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.svalero.ontimeapp.api.OnTimeApi;
import com.svalero.ontimeapp.api.OnTimeApiInterface;
import com.svalero.ontimeapp.contract.SignRegisterContract;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;

import okio.Sink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *Implementamos el contract SignRegisterContract.Model
 */
public class SignRegisterModel implements SignRegisterContract.Model {
    @Override
    public void registerSign(long userId, Sign sign, OnRegisterSignListener listener) {
        try {
            OnTimeApiInterface onTimeApi = OnTimeApi.buildInstance();
            Call<Sign> callSign = onTimeApi.addSign(userId, sign);
            Log.d("Register Sign", "Llamada desde el model"); //Para depurar errores y ver si avanza o donde se para
            callSign.enqueue(new Callback<Sign>() {
                @Override
                public void onResponse(Call<Sign> call, Response<Sign> response) {
                    Log.d("Register Sign", "Llamada desde el model OK"); //Para depurar errores y ver si avanza o donde se para
                    Sign sign = response.body();
                    listener.onRegisterSuccess(sign); // recibimos el fichaje por el listener
                }

                @Override
                public void onFailure(Call<Sign> call, Throwable t) {
                    Log.d("Register Sign", "Llamada desde el model ERROR"); //Para depurar errores y ver si avanza o donde se para
                    t.printStackTrace();
                    String message = "Error invocando a la operación";
                    listener.onRegisterError(message);

                }
            });
        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
        }
    }
}
