package com.example.mvpdemo.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONObject;

import retrofit2.Response;

public class Utils {
    public static void showErrorFromServer(Response response, Context context) {
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());
            Toast.makeText(context, jsonObject.getString("status_message"), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
