package com.example.scott.bonus.interfaces;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Scott on 15/7/29.
 */
public interface Login {
    @FormUrlEncoded
    @POST("/user/login")
    String userLogin(@Field("email") String email, @Field("password") String password);
}
