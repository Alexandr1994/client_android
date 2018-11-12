package com.example.alexandr.androidclient;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static int verstion = 1;

    public static int getVersion() {
        return verstion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String token = TokenHelper.loadToken(this);
        if(token == null) {
            //replace MainActivity on LoginActivity
            startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            return;
        }
        this.checkLogin(token);
        //init activity
        setContentView(R.layout.activity_main);
        final Context context = this;
        ((Button)findViewById(R.id.logout)).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    HttpHelper http = new HttpHelper();
                    http.postRequest(context, new HttpHelper.Callback() {
                            @Override
                            public void callback(JSONObject data, Context context) {
                                TokenHelper.deleteToken(context);
                                startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        },"api/logout", new JSONObject().put("token", token)
                    );
                } catch(Exception e) {
                    TokenHelper.deleteToken(context);
                    startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                    return;
                }
            }
        });
    }

    private void checkLogin(String token) {
        try {
            HttpHelper http = new HttpHelper();
            Context context = this;
            http.postRequest(this, new HttpHelper.Callback(){
                @Override
                public void callback(JSONObject data, Context context) {
                    try {
                        if(data.getInt("success") != 1) {
                            throw new Exception("Session is invalid!");
                        }
                    }
                    catch (Exception e) {
                        //clear sessions
                        TokenHelper.deleteToken(context);
                        startActivity(new Intent(context, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME));
                        finish();
                    }
                }
            }, "api/user", new JSONObject().putOpt("token", token) );
        }
        catch(Exception e) {
            InterfaceHelper interfaceHelper = new InterfaceHelper();
            interfaceHelper.alert(this, "Error", "", "Okay", -1).show();
        }
    }
}
