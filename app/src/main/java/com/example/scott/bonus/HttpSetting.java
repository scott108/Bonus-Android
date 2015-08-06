package com.example.scott.bonus;

import com.example.scott.bonus.httpinterfaces.Http;

import retrofit.RestAdapter;

/**
 * Created by Scott on 15/8/6.
 */
public class HttpSetting {
    private static final String serverURL = "http://140.120.15.80:8080/iBonus-server";
    private static RestAdapter restAdapter;
    private static HttpSetting httpSetting = new HttpSetting();
    private static Http http;

    private HttpSetting() {
        restAdapter = new RestAdapter.Builder().setEndpoint(serverURL).build();
        http = restAdapter.create(Http.class);
    }

    public static HttpSetting getInstance() {
        return httpSetting;
    }

    public Http getHttp() {
        return http;
    }


}
