package com.project.commoditiesCurrentPrice.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.project.commoditiesCurrentPrice.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Test for connection
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkCapabilities mCapabilities = mConnectivityManager.getNetworkCapabilities(mConnectivityManager.getActiveNetwork());
            return mCapabilities != null &&
                    (mCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            mCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        else {
            NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
    }

    // Showing the status in Snackbar
    public static void showSnack(View view, boolean isError, String message) {
        int color = Color.WHITE;
        if (isError)
            color = Color.RED;

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month-1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public static String getDefaultDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()-24*60*60*1000));
    }

    public static String getCurrentDateAndTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
    }
}
