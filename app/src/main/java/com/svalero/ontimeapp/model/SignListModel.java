package com.svalero.ontimeapp.model;

import android.content.Context;
import android.util.Log;

import com.svalero.ontimeapp.api.OnTimeApi;
import com.svalero.ontimeapp.api.OnTimeApiInterface;
import com.svalero.ontimeapp.contract.SignListContract;
import com.svalero.ontimeapp.domain.Sign;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *Implementamos el contract SignListContract.Model
 */
public class SignListModel implements SignListContract.Model {

    private Context context; //para poder pasarle el contexto de la aplicacion

    public SignListModel(Context context) {
        this.context = context;
    }

    /**
     * Sustituimos la llamada a la BBDD por la llamada a la API
     */
    @Override
    public void loadAllSigns(OnLoadSignsListener listener, String firstDay) {
        //Nos devuelve una instancia de onTimeApi como la definimos en OnTimeApiInterface, tiene los métodos que usamos para comunicarnos con la API
        OnTimeApiInterface onTimeApi = OnTimeApi.buildInstance();
        Call<List<Sign>> callSigns = onTimeApi.getSigns(firstDay);
        Log.d("List Sign", "Llamada desde el model"); //Para depurar errores y ver si avanza o donde se para
        callSigns.enqueue(new Callback<List<Sign>>() {
            @Override
            public void onResponse(Call<List<Sign>> call, Response<List<Sign>> response) {
                Log.d("List Sign", "Llamada desde el model OK"); //Para depurar errores y ver si avanza o donde se para
                List<Sign> signs = response.body(); // Recogemos la respuesta de la Api en una lista
                listener.onLoadSignsSucess(signs);
            }

            @Override
            public void onFailure(Call<List<Sign>> call, Throwable t) {
                Log.d("List Sign", "Llamada desde el model ERROR"); //Para depurar errores y ver si avanza o donde se para
                t.printStackTrace();
                String message = "Error invocando la operación";
                listener.onLoadSignsError(message);
            }
        });
    }
}
