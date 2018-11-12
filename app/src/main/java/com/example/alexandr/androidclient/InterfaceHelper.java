package com.example.alexandr.androidclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Icon;

public class InterfaceHelper {
    /**
     * Prepare alert dialog
     * @param context
     * @param title
     * @param message
     * @param closeBtnTitle
     * @param icon
     * @return
     */
    public static AlertDialog alert(Context context, String title, String message, String closeBtnTitle, int icon)
    {
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message);
            if(icon > 0)
            {
                builder.setIcon(icon);
            }
            builder.setNegativeButton(closeBtnTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            return builder.create();
        }
        catch(Exception e)
        {
            return null;
        }
    }



}
