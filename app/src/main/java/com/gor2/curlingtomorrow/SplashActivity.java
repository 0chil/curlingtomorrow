package com.gor2.curlingtomorrow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.gor2.curlingtomorrow.dataclass.Result;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Curlingtomorrow)getApplication()).LoadResult();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Intent intent2 = new Intent(this, NoticeActivity.class);
        startActivity(intent2);

    }
}
