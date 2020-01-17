package project.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import ir.erfan.prostd.R;
import project.data.DB;
import project.helper.SessionManager;
import project.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputEditText edt_username, edt_password;
    private TextInputLayout til_password;
    private MaterialButton btn_login;

    private JSONObjectRequestListener listener;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        initViews();
    }

    private void initViews() {
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        til_password = findViewById(R.id.til_password);
        btn_login = findViewById(R.id.btn_login);

        Typeface vazir = Typeface.createFromAsset(getAssets(), "fonts/vazir_fd.ttf");
        edt_password.setTypeface(vazir);
        til_password.setTypeface(vazir);

        initListeners();
    }

    private void initListeners() {
        listener = new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt("success");

                    switch (success) {
                        case 1:
                            JSONObject login = response.getJSONObject("login");
                            String uid      = login.getString("uid");
                            String username = login.getString("username");
                            String password = login.getString("password");
                            String role     = login.getString("role");

                            User user = new User(uid, username, password, role);

                            sessionManager.createSession(user);

                            Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();


                            break;
                        case 0:
                            TastyToast.makeText(LoginActivity.this, getResources().getString(R.string.username_password_not_match), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            edt_password.setText("");
                            edt_password.requestFocus();
                            break;
                        default:
                            TastyToast.makeText(LoginActivity.this, getResources().getString(R.string.username_not_exsits), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                            edt_username.setText("");
                            edt_password.setText("");
                            edt_username.requestFocus();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: " + e.getMessage());
                    TastyToast.makeText(LoginActivity.this, e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.i(TAG, "onError: " + anError.getLocalizedMessage());
                TastyToast.makeText(LoginActivity.this, anError.getErrorDetail(), TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        };

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(edt_username.getText().toString().equals(""))) {
                    if (!(edt_password.getText().toString().equals(""))) {
                        DB.login(edt_username.getText().toString(), edt_password.getText().toString(), listener);



                    } else {
                        edt_password.setError(getResources().getString(R.string.enter_password));
                    }
                } else {
                    edt_username.setError(getResources().getString(R.string.enter_username));
                }
            }
        });
    }
}
