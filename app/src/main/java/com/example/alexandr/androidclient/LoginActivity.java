package com.example.alexandr.androidclient;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.annotation.ElementType;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //activity context
        final Context context = this;
        //login btn
        ((Button)this.findViewById(R.id.loginBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    //format data
                    JSONObject data = new JSONObject()
                            .put("login", (((EditText)findViewById(R.id.login)).getText()))
                            .put("password", (((EditText)findViewById(R.id.password)).getText()));
                    //do request
                    HttpHelper.postRequest(context, new LoginCallback(), "api/login", new JSONObject().put("data", data));;
                }
                catch(Exception e)
                {
                    InterfaceHelper.alert(context, "Error!", "Something happend here...", "Okay", -1).show();
                }
            }
        });
        //registration btn
        ((Button)findViewById(R.id.signupBtn)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegistrationActivity.class));
            }
        });
    }

    /**
     * Login callback
     */
    class LoginCallback implements HttpHelper.Callback
    {
        @Override
        public void callback(JSONObject data, Context context) {
            try
            {
                if(!data.has("success"))
                {
                    throw new Exception("Wrong response from server!");
                }
                if(data.getInt("success") == 1)
                {
                    TokenHelper.saveToken(context, data.getJSONObject("data").getString("token"));
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                }
                else
                {
                    InterfaceHelper.alert(context, "Warning!", data.getJSONObject("error").getString("message"), "Ok", -1).show();
                }
            }
            catch(Exception e)
            {
                InterfaceHelper.alert(context, "Error!", "Wrong response from server...", "Okay", -1).show();
            }
        }
    }
}
