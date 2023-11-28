package com.svalero.ontimeapp.model;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.svalero.ontimeapp.api.OnTimeApi;
import com.svalero.ontimeapp.api.OnTimeApiInterface;
import com.svalero.ontimeapp.contract.UserPassDtoContract;
import com.svalero.ontimeapp.domain.Dto.UserPassDto;
import com.svalero.ontimeapp.domain.Sign;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPassDtoModel implements UserPassDtoContract.Model {
    @Override
    public void updatePass(long id, UserPassDto userPassDto, OnUpdatePassListener listener) {
        try {
            OnTimeApiInterface onTimeApi = OnTimeApi.buildInstance();
            Call<UserPassDto> callUser = onTimeApi.modifyPassword(id, userPassDto);
            Log.d("Register User Update Pass", "Llamada desde el model"); //Para depurar errores y ver si avanza o donde se para
            callUser.enqueue(new Callback<UserPassDto>() {
                @Override
                public void onResponse(Call<UserPassDto> call, Response<UserPassDto> response) {
                    Log.d("Register User Update Pass", "Llamada desde el model OK"); //Para depurar errores y ver si avanza o donde se para
                    UserPassDto userPassDto = response.body();
                    listener.onUpdatePassSuccess(userPassDto);
                }

                @Override
                public void onFailure(Call<UserPassDto> call, Throwable t) {
                    Log.d("Register User Update Pass", "Llamada desde el model ERROR"); //Para depurar errores y ver si avanza o donde se para
                    t.printStackTrace();
                    String message = "Error invocando a la operación";
                    listener.onUpdatePassError(message);

                }
            });
        } catch (SQLiteConstraintException sce) {
            sce.printStackTrace();
        }

    }
}
