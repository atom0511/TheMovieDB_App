package com.example.mvpdemo.models.share_pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AccountSharePref {
    private static final String SESSION_ID_KEY = "session_id_key";

    private SharedPreferences sharedPreferences;

    public AccountSharePref(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveSessionId(String sessionId) {
        sharedPreferences
                .edit()
                .putString(SESSION_ID_KEY, sessionId)
                .apply();
    }

    public String getSessionId() {
        return sharedPreferences
                .getString(SESSION_ID_KEY, null);
    }
}
