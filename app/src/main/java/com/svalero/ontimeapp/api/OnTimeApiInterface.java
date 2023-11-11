package com.svalero.ontimeapp.api;

import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Para definir las operaciones que queremos dar visibilidad en nuestro Aplicacion android provenientes de la API
 */
public interface OnTimeApiInterface {

    /**
     * Users
     */
    @GET("users")
    Call<List<User>> getUsers(); //devuelve una lista de usuarios

    @GET("users/login")
    Call<User> getLogin(@Query("username") String username, @Query("pass") String pass);

    /**
     * Sign
     */
    @POST("users/{userId}/signs")
    Call<Sign> addSign(@Path("userId") long userId, @Body Sign sign);

}
