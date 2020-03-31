package com.gor2.curlingtomorrow;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.gor2.curlingtomorrow.ui.CameraActivity;
import com.gor2.curlingtomorrow.ui.ManualFrag;
import com.gor2.curlingtomorrow.ui.ResultsFrag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView navView;
    Fragment manualFrag, resultsFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final FragmentManager fm = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.navigation_manual){
                    fm.beginTransaction().replace(R.id.nav_host_fragment,manualFrag).commit();
                }
                else if(itemId == R.id.navigation_camera){
                    MainActivity.this.startActivity(new Intent(MainActivity.this, CameraActivity.class));
                    MainActivity.this.navView.setSelectedItemId(R.id.navigation_results);
                    return false;
                }
                else if(itemId == R.id.navigation_results){
                    fm.beginTransaction().replace(R.id.nav_host_fragment,resultsFrag).commit();
                }
                return true;
            }
        });

        FirebaseApp.initializeApp(getApplicationContext());

        manualFrag = new ManualFrag();
        resultsFrag = new ResultsFrag();
        fm.beginTransaction().replace(R.id.nav_host_fragment,manualFrag).commit();

        getSupportActionBar().hide();
    }

}
