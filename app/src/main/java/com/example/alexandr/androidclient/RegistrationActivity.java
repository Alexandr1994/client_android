package com.example.alexandr.androidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;

import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final Context context = this;
        //set button cliek listener
        ((Button)findViewById(R.id.signUpBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = ((EditText)findViewById(R.id.login)).getText().toString();
                String email = ((EditText)findViewById(R.id.email)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                String confirmPassword = ((EditText)findViewById(R.id.confirm)).getText().toString();
                //validation
                //login
                if(login.equals("")) {
                    InterfaceHelper.alert(context, "Error!", "Login is empty!", "Okay", -1).show();
                    return;
                }
                //email
                if(email.equals("")) {
                    InterfaceHelper.alert(context, "Error!", "E-mail is empty!", "Okay", -1).show();
                    return;
                }
                //password
                if(password.equals("") || password.length() < 8) {
                    InterfaceHelper.alert(context, "Error!", "Password is empty or too short!", "Okay", -1).show();
                    return;
                }
                if(!password.equals(confirmPassword)) {
                    InterfaceHelper.alert(context, "Error!", "Password and password confirmation isn't equals!", "Okay", -1).show();
                    return;
                }
                //prepare data
                try {
                    JSONObject data = new JSONObject()
                            .put("login", login)
                            .put("email", email)
                            .put("password", password);
                    HttpHelper.postRequest(context, new HttpHelper.Callback() {
                        @Override
                        public void callback(JSONObject data, Context context) {
                            try {
                                if(data.has("success")) {
                                    if(data.getInt("success") == 1) {
                                        //save token
                                        TokenHelper.saveToken(context, data.getJSONObject("data").getString("token"));
                                        startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME));
                                        finish();
                                    }
                                    else {
                                        InterfaceHelper.alert(context, "Error!", data.getJSONObject("error").getString("message"), "Okay", -1).show();
                                    }
                                }
                            }
                            catch(Exception e) {
                                InterfaceHelper.alert(context, "Error", "Error of request of request to server!", "Okay", -1).show();
                                return;
                            }
                        }
                    }, "api/registration", new JSONObject().put("data", data));
                }
                catch(Exception e) {
                    InterfaceHelper.alert(context, "Error", "Something is wrong!", "Okay", -1).show();
                    return;
                }
            }
        });
    }
}
