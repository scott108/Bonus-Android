package com.example.scott.bonus.interfaces;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Scott on 15/8/5.
 */
public interface SignUp {
    @FormUrlEncoded
    @POST("/user/add")
    String userAdd(@Field("userName") String userName,
                   @Field("email") String email,
                   @Field("password") String password,
                   @Field("passwordConfirm") String passwordConfirm);
}
