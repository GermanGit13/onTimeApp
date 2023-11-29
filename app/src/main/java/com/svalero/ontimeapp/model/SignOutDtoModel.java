package com.svalero.ontimeapp.model;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.svalero.ontimeapp.api.OnTimeApi;
import com.svalero.ontimeapp.api.OnTimeApiInterface;
import com.svalero.ontimeapp.contract.SignOutDtoContract;
import com.svalero.ontimeapp.domain.Dto.SignOutDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignOutDtoModel implements SignOutDtoContract.Model {
    @Override
    public void singOut(long id, SignOutDto signOutDto, OnSingOutListener listener) {
        try {
            OnTimeApiInterface onTimeApi = OnTimeApi.buildInstance();
            Call<SignOutDto> callSign = onTimeApi.signOut(id, signOutDto);
            Log.d("Register Sign Out", "Llamada desde el model"); //Para depurar errores y ver si avanza o donde se para
            callSign.enqueue(new Callback<SignOutDto>() {
                @Override
                public void onResponse(Call<SignOutDto> call, Response<SignOutDto> response) {
                    Log.d("Register Sign Out", "Llamada desde el model OK"); //Para depurar errores y ver si avanza o donde se para
                    SignOutDto signOutDto = response.body();
                    listener.OnSingOutSucess(signOutDto); // recibimos el fichaje por el listener
                }

                @Override
                public void onFailure(Call<SignOutDto> call, Throwable t) {
                    Log.d("Register Sign Out", "Llamada desde el model ERROR"); //Para depurar errores y ver si avanza o donde se para
                    t.printStackTrace();
                    String message = "Error invocando a la operación";
                    listener.OnSingOutError(message);

                }
            });
        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
        }
    }
}
