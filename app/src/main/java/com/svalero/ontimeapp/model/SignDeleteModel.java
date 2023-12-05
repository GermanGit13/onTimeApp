package com.svalero.ontimeapp.model;

import android.util.Log;

import com.svalero.ontimeapp.api.OnTimeApi;
import com.svalero.ontimeapp.api.OnTimeApiInterface;
import com.svalero.ontimeapp.contract.SignDeleteContract;
import com.svalero.ontimeapp.domain.Sign;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignDeleteModel implements SignDeleteContract.Model {

    @Override
    public void deleteSing(String signId, onDeleteSignListener listener) {
        //Nos devuelve una instancia de onTimeApi como la definimos en OnTimeApiInterface, tiene los métodos que usamos para comunicarnos con la API
        OnTimeApiInterface onTimeApi = OnTimeApi.buildInstance();
        Call<Void> callSigns = onTimeApi.deleteSign(signId);
//        Log.d("List Sign Delete", "Llamada desde el model"); //Para depurar errores y ver si avanza o donde se para
        callSigns.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                Log.d("List Sign Delete", "Llamada desde el model OK"); //Para depurar errores y ver si avanza o donde se para
                listener.onDeleteSuccess();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("List Sign Delete", "Llamada desde el model ERROR"); //Para depurar errores y ver si avanza o donde se para
                t.printStackTrace();
                String message = "Error invocando la operación";
                listener.onDeleteError(message);
            }
        });
    }
}
