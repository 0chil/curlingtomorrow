package com.gor2.curlingtomorrow.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gor2.curlingtomorrow.Curlingtomorrow;
import com.gor2.curlingtomorrow.R;
import com.gor2.curlingtomorrow.dataclass.Result;

import java.io.File;

public class ShowResultActivity extends AppCompatActivity{
    ImageView imgTaken;
    FloatingActionButton btnSave;
    FloatingActionButton btnRetry;
    FrameLayout frameLayout;
    public TextView txtScore, txtPlayerRed, txtPlayerYellow;

    Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상태바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 액션바 제거
        getSupportActionBar().hide();

        setContentView(R.layout.activity_showresult);

        //FrameLayout
        frameLayout = findViewById(R.id.frameLayout_showresult);

        //TextViews
        txtScore = findViewById(R.id.txtScore_showresult);
        txtPlayerRed = findViewById(R.id.txtPlayerRed_showresult);
        txtPlayerYellow = findViewById(R.id.txtPlayerYellow_showresult);

        //Get result from Application Class by index
        int index = getIntent().getIntExtra("index",0);
        result = ((Curlingtomorrow)getApplication()).GetResultsList().get(index);

        //Save & Retry Button
        btnSave = findViewById(R.id.btnShare);
        btnRetry = findViewById(R.id.btnDelete);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Read Temp.jpg
        imgTaken = findViewById(R.id.imgTaken_showresult);
        imgTaken.setImageBitmap(GetBitmapFromInternal());

        //Set Name, Score
        txtPlayerRed.setText(result.getPlayerRedName());
        txtPlayerYellow.setText(result.getPlayerYellowName());
        String scoreText = result.getPlayerRedScore()+":"+result.getPlayerYellowScore();
        txtScore.setText(scoreText);
    }

    private Bitmap GetBitmapFromInternal(){
        File storage = getFilesDir();
        File tempFile = new File(storage,result.getImageFileName());
        return BitmapFactory.decodeFile(tempFile.getAbsolutePath());
    }
}
