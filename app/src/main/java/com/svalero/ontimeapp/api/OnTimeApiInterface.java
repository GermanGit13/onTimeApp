package com.svalero.ontimeapp.api;

import com.svalero.ontimeapp.domain.Dto.SignOutDto;
import com.svalero.ontimeapp.domain.Dto.UserPassDto;
import com.svalero.ontimeapp.domain.Sign;
import com.svalero.ontimeapp.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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
    @PATCH("users/{id}")
    Call<UserPassDto> modifyPassword(@Path("id") long id, @Body UserPassDto userPassDto);

    /**
     * Sign
     */
    @POST("users/{userId}/signs")
    Call<Sign> addSign(@Path("userId") long userId, @Body Sign sign);
    @PATCH("/signs/{id}")
    Call<SignOutDto> signOut(@Path("id") long id, @Body SignOutDto signOutDto);

    @GET("signs")
    Call<List<Sign>> getSigns(@Query("day") String day, @Query("secondDay") String secondDay, @Query("name") String name);

    @GET("signs")
    Call<List<Sign>> getSigns(@Query("userInSign_department") String department, @Query("day") String day, @Query("secondDay") String secondDay, @Query("name") String name);

    @GET("/users/{userId}/signs")
    Call<List<Sign>> findByUserInSign(@Path("userId") String userId, @Query("day") String day, @Query("secondDay") String secondDay);
    @DELETE("/signs/{id}")
    Call<Void> deleteSign(@Path("id") String signId); // Void porque la operación de borrado no devuelve nada
}
