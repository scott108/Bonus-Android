package com.example.scott.bonus.utility;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Scott on 15/4/26.
 */
public class utility {


    public static JSONObject Base64ByteToJson(byte[] bytesMessage)
    {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(new String(bytesMessage));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
