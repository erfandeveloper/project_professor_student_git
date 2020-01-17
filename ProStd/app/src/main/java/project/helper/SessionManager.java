package project.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import project.activity.LoginActivity;
import project.model.User;

public class SessionManager {
    private static final String TAG = "SessionManager";
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;

    public static final String PREF_NAME = "LOGIN_IN_APP";
    public static final String LOGIN = "IS_LOGIN";
    public static final String USER = "USER";
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        Log.i(TAG, "SessionManager: " + context);
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(User user) {
        editor.putBoolean(LOGIN, true);
        String userJSON = new Gson().toJson(user);
        editor.putString(USER, userJSON);
        editor.commit();
    }

    public boolean isLoggin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public User getUser() {
        return new Gson().fromJson(sharedPreferences.getString(USER, ""), User.class);
    }

    public void logout() {
        editor.remove(LOGIN);
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

}
