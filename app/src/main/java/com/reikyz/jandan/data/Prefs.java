package com.reikyz.jandan.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.reikyz.jandan.MyApp;


/**
 * Created by Reiky on 2016/5/20.
 */
public class Prefs {

    final static String TAG = "Prefs";

    public static void save(String key, Object data) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        prefs.edit().putString(key, String.valueOf(data)).apply();
    }

    public static boolean getBoolean(String key, boolean def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        return Boolean.parseBoolean(prefs.getString(key, String.valueOf(def)));
    }

    public static boolean getBoolean(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        return Boolean.parseBoolean(prefs.getString(key, ""));
    }

    public static int getInt(String key, int def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        try {
            return Integer.parseInt(prefs.getString(key, String.valueOf(def)));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static int getInt(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        try {

            return Integer.parseInt(prefs.getString(key, "-1"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getString(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        return prefs.getString(key, null);
    }
    public static String getString(String key,String def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        return prefs.getString(key, def);
    }

}
