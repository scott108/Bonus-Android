package com.example.scott.bonus.httpinterfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by Scott on 15/7/29.
 */
public interface Http {
    @FormUrlEncoded
    @POST("/user/login")
    void userLogin(@Field("email") String email, @Field("password") String password, Callback<JsonObject> response);

    @GET("/user/logout")
    String userLogout(@Header("Cookie") String token);

    @FormUrlEncoded
    @POST("/user/add")
    String userAdd(@Field("userName") String userName,
                   @Field("email") String email,
                   @Field("password") String password,
                   @Field("passwordConfirm") String passwordConfirm);

    @GET("/coupon/all")
    void getAllCoupon(Callback<JsonArray> response);
}
