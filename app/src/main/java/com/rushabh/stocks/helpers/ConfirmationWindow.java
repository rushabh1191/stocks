package com.rushabh.stocks.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmationWindow {

    private AlertDialog.Builder builder;

    public ConfirmationWindow(Context context, String title, String message, String positiveTitle, String negativeTitle) {
        manageWindow(context, true, title, message, positiveTitle, negativeTitle, "");

    }

    public ConfirmationWindow(Context context, String title, String message, String positiveTitle, String negativeTitle, boolean cancelable) {

        manageWindow(context, cancelable, title, message, positiveTitle, negativeTitle, "");
    }

    public ConfirmationWindow(Context context, String title, String message, String positiveTitle, String negativeTitle, String neutralTitle) {
        manageWindow(context, true, title, message, positiveTitle, negativeTitle, neutralTitle);

    }

    void manageWindow(Context context, boolean cancelable, String title, String message, String positiveTitle,
                      String negativeTitle, String neutralTitle) {
        try {
            builder = new AlertDialog.Builder(context);
            if (!title.equals(""))
                builder.setTitle(title);
            builder.setMessage(message);
            if (!positiveTitle.equals("")) {
                builder.setPositiveButton(positiveTitle, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPositiveResponse();

                    }
                });
            }

            if (!negativeTitle.equals("")) {
                builder.setNegativeButton(negativeTitle, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setNegativeResponse();
                    }
                });
            }
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCancelable(boolean setCancelable) {
        builder.setCancelable(setCancelable);
    }

    protected void setNeutralResponse() {

    }

    protected void setPositiveResponse() {

    }

    protected void setNegativeResponse() {

    }

}
