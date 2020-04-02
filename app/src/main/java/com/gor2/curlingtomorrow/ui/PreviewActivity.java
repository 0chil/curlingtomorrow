package com.gor2.curlingtomorrow.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.gor2.curlingtomorrow.Curlingtomorrow;
import com.gor2.curlingtomorrow.DialogSave;
import com.gor2.curlingtomorrow.R;
import com.gor2.curlingtomorrow.camera.DrawDetectionsOnTop;
import com.gor2.curlingtomorrow.dataclass.Detection;
import com.gor2.curlingtomorrow.dataclass.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class PreviewActivity extends AppCompatActivity{
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

        // 화면 켜짐 유지
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_preview);

        //FrameLayout
        frameLayout = findViewById(R.id.frameLayout);

        //TextViews
        txtScore = findViewById(R.id.txtScore);
        txtPlayerRed = findViewById(R.id.txtPlayerRed);
        txtPlayerYellow = findViewById(R.id.txtPlayerYellow);

        //Save & Retry Button
        btnSave = findViewById(R.id.btnSave);
        btnRetry = findViewById(R.id.btnRetry);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPlayerRed.getText().toString().isEmpty() || txtPlayerYellow.getText().toString().isEmpty()) return;
                SaveImageAndResult();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //ML Kit Initialize
        try { InitMLKit(); } catch (FirebaseMLException e) { e.printStackTrace(); }

        //Read Temp.jpg
        imgTaken = findViewById(R.id.imgTaken);
        imgTaken.setImageBitmap(GetBitmapFromInternal());

        //Inference
        try { InferenceLocation(GetBitmapFromInternal(),imgTaken); } catch (FirebaseMLException e) { e.printStackTrace(); }

    }

    private void SaveImageAndResult(){
        if(result == null){ Toast.makeText(this,"스톤이 인식되지 않았습니다",Toast.LENGTH_SHORT).show(); return; }
        new SaveImageTask().execute(GetBitmapFromInternal());
    }

    private void AfterDetection(ArrayList<Detection> detections){
        if(!detections.isEmpty()) {
            int redScore, yellowScore;

            // Current Timestamp
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일");
            String formatDate = sdfNow.format(date);


            //Show Dialog for Player Name
            DialogSave dialogSave = new DialogSave(PreviewActivity.this);
            dialogSave.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogSave.setCancelable(false);
            dialogSave.show();

            //Draw Detection Boxes
            DrawDetectionsOnTop overlayDrawer = new DrawDetectionsOnTop(PreviewActivity.this, detections, frameLayout);
            frameLayout.addView(overlayDrawer, new LinearLayout.LayoutParams(imgTaken.getMeasuredWidth(), imgTaken.getMeasuredHeight()));

            //Calculate Distance
            for(Detection detection : detections)
                detection.setDistance(GetDistanceFromCenter(detection.getCenter()));

            //Sort By Distance, Ascending
            detections.sort(new Ascending());

            //Get Continuous count
            int firstStone = detections.get(0).getClassNumber();
            int sameCount = 0;
            for(Detection detection : detections){
                if(detection.getClassNumber() == firstStone)
                    sameCount++;
            }

            //Set Winner
            if(firstStone == 0){
                //Winner : Red
                redScore = sameCount;
                yellowScore = 0;

            }else{
                //Winner : Yellow
                redScore = 0;
                yellowScore = sameCount;
            }
            txtScore.setText(String.format("%d:%d",redScore,yellowScore));
            result = new Result(formatDate,"","",redScore,yellowScore,"");
        }else{
            //No detections Found
            Toast toast = Toast.makeText(this,"스톤이 인식되지 않았습니다\n다시 촬영해주세요",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM,0,120);
            toast.show();
            txtScore.setText("-");
        }
    }

    private Float GetDistanceFromCenter(PointF stone){
        PointF center = new PointF(0.5f,0.5f);
        return center.length(stone.x,stone.y);
    }

    private Bitmap GetBitmapFromInternal(){
        File storage = getFilesDir();
        File tempFile = new File(storage,"temp.jpg");
        return BitmapFactory.decodeFile(tempFile.getAbsolutePath());
    }

    FirebaseModelInterpreter interpreter;
    FirebaseModelInputOutputOptions inputOutputOptions;
    private void InitMLKit() throws FirebaseMLException {
        FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("detect.tflite")
                .build();

        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(localModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            // ...
        }

        inputOutputOptions = new FirebaseModelInputOutputOptions.Builder()
                .setInputFormat(0, FirebaseModelDataType.BYTE, new int[]{1, 320, 320, 3})
                .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 40, 4})
                .setOutputFormat(1, FirebaseModelDataType.FLOAT32, new int[]{1, 40})
                .setOutputFormat(2, FirebaseModelDataType.FLOAT32, new int[]{1, 40})
                .setOutputFormat(3, FirebaseModelDataType.FLOAT32, new int[]{1})
                .build();
    }

    private void InferenceLocation(Bitmap bitmap,final ImageView imgTaken) throws FirebaseMLException {
        int[] intValues = new int[102400];//320*320
        bitmap = Bitmap.createScaledBitmap(bitmap, 320, 320, true);

        ByteBuffer buffer= ByteBuffer.allocateDirect(307200); //1*320*320*3*1
        buffer.order(ByteOrder.nativeOrder());

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int x = 0; x < 320; x++) {
            for (int y = 0; y < 320; y++) {
                int pixelValue = intValues[x * 320 + y];
                buffer.put((byte) ((pixelValue >> 16) & 0xFF));
                buffer.put((byte) ((pixelValue >> 8) & 0xFF));
                buffer.put((byte) (pixelValue & 0xFF));
            }
        }

        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(buffer)  // add() as many input arrays as your model requires
                .build();
        interpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                //On Success
                                ArrayList<Detection> detections = new ArrayList<>();

                                //TFLite Outputs
                                float[][][] detection_boxes = result.getOutput(0);
                                float[][] detection_classes = result.getOutput(1);
                                float[][] detection_scores = result.getOutput(2);
                                float[] num_detections = result.getOutput(3);

                                //Create Detection Class Instances
                                for(int i = 0; i < num_detections[0]; i++) {
                                    if (detection_scores[0][i] < 0.5f) continue;
                                    detections.add(
                                            new Detection(
                                                    (int) detection_classes[0][i],
                                                    new RectF(
                                                            detection_boxes[0][i][1],
                                                            detection_boxes[0][i][0],
                                                            detection_boxes[0][i][3],
                                                            detection_boxes[0][i][2]
                                                    )
                                            )
                                    );

                                    //Debug
                                    Log.e("ML", "scores : " + detection_scores[0][i]);
                                    Log.e("ML", "classes : " + detection_classes[0][i]);
                                }
                                AfterDetection(detections);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //실패
                                Log.e("ML","FAILED");
                                e.printStackTrace();
                            }
                        });
    }
    private String SaveBitmapToJpegInternalTemp(Bitmap bitmap, String fileName){
        File storage = getFilesDir();
        File tempFile = new File(storage, fileName);
        String result = tempFile.getAbsolutePath();
        try{
            tempFile.createNewFile();
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private class SaveImageTask extends AsyncTask<Bitmap, Void, Void> {
        String URL;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            result.setImagePath(URL);
            result.setPlayerRedName(txtPlayerRed.getText().toString());
            result.setPlayerYellowName(txtPlayerYellow.getText().toString());
            ((Curlingtomorrow) getApplication()).AddResult(result);
            setResult(RESULT_OK);
            finish();
        }

        @Override
        protected Void doInBackground(Bitmap... data) {

            Bitmap bitmap = null;
            try {
                bitmap = data[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            URL = SaveBitmapToJpegInternalTemp(bitmap,System.currentTimeMillis()+".jpg");
            return null;
        }

    }
}

class Ascending implements Comparator<Detection>{
    @Override
    public int compare(Detection o1, Detection o2) {
        return o2.getDistance().compareTo(o1.getDistance());
    }
}
