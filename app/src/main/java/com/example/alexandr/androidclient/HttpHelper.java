package com.example.alexandr.androidclient;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import android.content.Context;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Set;

public class HttpHelper {

    private static String baseUrl = "http://10.0.0.254/";
    private static RequestQueue queue;

    /**
     * Http resopnse callback
     */
    public interface Callback
    {
        public void callback(JSONObject data, Context context);
    }

    /**
     * POST request to server
     * @param context
     * @param callback
     * @param url
     * @param args
     */
    public static void postRequest(Context context, Callback callback, String url, JSONObject args) {
        doRequest(context, callback,baseUrl + url, Request.Method.POST, args);
    }

    /**
     * GET request to server
     * @param context
     * @param callback
     * @param url
     * @param args
     */
    public static void getRequest(Context context, Callback callback, String url, HashMap<String, String> args) {
        if(!args.isEmpty())
        {
            url += '?';
            String[] keys = new String[args.size()];
            keys = args.keySet().toArray(keys);
            for(int i = 0; i < keys.length; i ++)
            {
                if(i > 0)
                {
                    url += '&';
                }
                url += keys[i] + "=" + args.get(keys[i]);
            }
        }
        doRequest(context, callback,baseUrl + url, Request.Method.GET, null);
    }

    /**
     * Complete HTTP request to server
     * @param context
     * @param callback
     * @param url
     * @param method
     * @param data
     */
    private static void doRequest(final Context context, final Callback callback, String url, int method, JSONObject data) {
        //checking requests queue
        if(queue == null)
        {
            queue = Volley.newRequestQueue(context);
        }
        JsonRequest request = new JsonObjectRequest(method, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.callback(response, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ;
            }
        });
        queue.add(request);
    }
}
