package com.gor2.curlingtomorrow;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.preference.PreferenceManager;
import android.text.BoringLayout;
import android.util.Base64;
import android.util.Log;

import com.gor2.curlingtomorrow.dataclass.Result;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class Curlingtomorrow extends Application {
    ArrayList<Result> results = new ArrayList<>();
    final String PREF_KEY="CURLING_RESULT";
    public static Boolean isLoaded = false;

    @Override
    public void onCreate() {
        super.onCreate();
//        Stetho.initializeWithDefaults(this);
//        KakaoSdk.init(this,"b0082583392318e989f91665bc49e630");
        Log.e("KEY",getKeyHash(this));
    }


    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("TAG", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    public ArrayList<Result> GetResultsList(){
        return results;
    }

    public void SaveResult(){
        setStringArrayPref(this,PREF_KEY,results);
    }

    public void LoadResult(){
        results = getStringArrayPref(this,PREF_KEY);
        isLoaded = true;
    }

    public void AddResult(Result result){
        results.add(result);
        SaveResult();
    }

    public void DeleteResult(int index){
        results.remove(index);
        SaveResult();
    }

    private void setStringArrayPref(Context context, String key, ArrayList<Result> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i).toJSONArray());
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    private ArrayList<Result> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<Result> results = new ArrayList<Result>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    Result test = Result.parseJSONArray(a.getJSONArray(i));
                    results.add(test);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
