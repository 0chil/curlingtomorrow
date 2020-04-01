package com.gor2.curlingtomorrow.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
import com.gor2.curlingtomorrow.DialogSave;
import com.gor2.curlingtomorrow.R;
import com.gor2.curlingtomorrow.camera.DrawDetectionsOnTop;
import com.gor2.curlingtomorrow.detection.Detection;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;

public class PreviewActivity extends AppCompatActivity{
    ImageView imgTaken;
    FloatingActionButton btnSave;
    FloatingActionButton btnRetry;
    FrameLayout frameLayout;
    public TextView txtScore, txtPlayerRed, txtPlayerYellow;

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

        //Save & Retry Button
        btnSave = findViewById(R.id.btnSave);
        btnRetry = findViewById(R.id.btnRetry);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //FrameLayout
        frameLayout = findViewById(R.id.frameLayout);

        //TextViews
        txtScore = findViewById(R.id.txtScore);
        txtPlayerRed = findViewById(R.id.txtPlayerRed);
        txtPlayerYellow = findViewById(R.id.txtPlayerYellow);

        //ML Kit Initialize
        try { InitMLKit(); } catch (FirebaseMLException e) { e.printStackTrace(); }

        //Read Temp.jpg
        imgTaken = findViewById(R.id.imgTaken);
        imgTaken.setImageBitmap(GetBitmapFromInternal());

        //Inference
        try { InferenceLocation(GetBitmapFromInternal(),imgTaken); } catch (FirebaseMLException e) { e.printStackTrace(); }

    }

    private void AfterDetection(ArrayList<Detection> detections){
        if(!detections.isEmpty()) {
            //Show Dialog for Player Name
            DialogSave dialogSave = new DialogSave(PreviewActivity.this);
            dialogSave.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogSave.setCancelable(false);
            dialogSave.show();

            DrawDetectionsOnTop overlayDrawer = new DrawDetectionsOnTop(PreviewActivity.this, detections, frameLayout);
            frameLayout.addView(overlayDrawer, new LinearLayout.LayoutParams(imgTaken.getMeasuredWidth(), imgTaken.getMeasuredHeight()));
//            frameLayout.addView(overlayDrawer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            for(Detection detection : detections)
                detection.setDistance(GetDistanceFromCenter(detection.getCenter()));

            detections.sort(new Ascending());

            int firstStone = detections.get(0).getClassNumber();
            int sameCount = 0;
            for(Detection detection : detections){
                if(detection.getClassNumber() == firstStone)
                    sameCount++;
            }

            if(firstStone == 0){
                //Winner : Red
                txtScore.setText(String.format("%d:%d",sameCount,0));
                Log.e("Result", String.format("Red : %d , Yellow : %d",sameCount,0));
            }else{
                //Winner : Yellow
                txtScore.setText(String.format("%d:%d",0,sameCount));
                Log.e("Result", String.format("Red : %d , Yellow : %d",0,sameCount));
            }
        }else{
            //No detections Found
            Toast.makeText(this,"스톤이 인식되지 않았습니다\n다시 촬영해주세요",Toast.LENGTH_SHORT).show();
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
}

class Ascending implements Comparator<Detection>{
    @Override
    public int compare(Detection o1, Detection o2) {
        return o2.getDistance().compareTo(o1.getDistance());
    }
}
