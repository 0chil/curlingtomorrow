package com.gor2.curlingtomorrow;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gor2.curlingtomorrow.dataclass.Result;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Curlingtomorrow extends Application {
    ArrayList<Result> results = new ArrayList<>();
    final String PREF_KEY="CURLING_RESULT";

    @Override
    public void onCreate() {
        super.onCreate();
//        Stetho.initializeWithDefaults(this);
    }

    public ArrayList<Result> GetResultsList(){
        return results;
    }

    public void SaveResult(){
        setStringArrayPref(this,PREF_KEY,results);
    }

    public void LoadResult(){
        results = getStringArrayPref(this,PREF_KEY);
    }

    public void AddResult(Result result){
        results.add(result);
        SaveResult();
    }

    public void DeleteResult(int index){
        results.remove(index);
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
