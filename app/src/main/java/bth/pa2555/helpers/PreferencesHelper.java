package bth.pa2555.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public final class PreferencesHelper {

    public static void setToken(Context c, String token) {
        getEditor(c).putString("token", token).commit();
    }

    public static String getToken(Context c){
        return getPrefs(c).getString("token", "");
    }

    public static void setName(Context c, String fullName) {
        getEditor(c).putString("full_name", fullName).commit();
    }

    public static String getName(Context c) {
        return getPrefs(c).getString("full_name", "");
    }

    public static void setNumber(Context c, String number) {
        getEditor(c).putString("phone_number", number).commit();
    }

    public static void setRatingList(Context c, Set<String> ratingList) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("RATING", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().putStringSet("rating_list", ratingList).apply();
    }

    public static Set<String> getRatingList(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("RATING", Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet("rating_list", null);
    }

    public static String getNumber(Context c){
        return getPrefs(c).getString("phone_number", "");
    }

    private static SharedPreferences getPrefs(Context c){
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    private static SharedPreferences.Editor getEditor (Context c){
        return getPrefs(c).edit();
    }


}
