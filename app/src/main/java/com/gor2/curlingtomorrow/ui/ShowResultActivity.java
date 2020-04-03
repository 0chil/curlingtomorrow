package com.gor2.curlingtomorrow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gor2.curlingtomorrow.Curlingtomorrow;
import com.gor2.curlingtomorrow.R;
import com.gor2.curlingtomorrow.SplashActivity;
import com.gor2.curlingtomorrow.dataclass.Result;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;

import java.io.File;

public class ShowResultActivity extends AppCompatActivity{
    ImageView imgTaken;
    FloatingActionButton btnShare,btnDelete,btnList;
    FrameLayout frameLayout;
    public TextView txtScore, txtPlayerRed, txtPlayerYellow;
    public Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
//        Get index
        final int index = intent.getIntExtra("index",-1);
//        if(index == -1) doRestart(this.getApplicationContext());

//         Process scheme
        //kakao~~~://kakaolink
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            if(Curlingtomorrow.isLoaded){
                finish();
            }
            else{
                startActivity(new Intent(this,SplashActivity.class));
                finish();
            }
            return;
        }

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

        //Save & Retry Button
        btnList = findViewById(R.id.btnList);
        btnShare = findViewById(R.id.btnShare);
        btnDelete = findViewById(R.id.btnDelete);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaolink();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Curlingtomorrow)getApplication()).DeleteResult(index);
                Intent deletePositionIntent = new Intent();
                deletePositionIntent.putExtra("deleteposition",index);
                setResult(Curlingtomorrow.RESULT_DELETED,deletePositionIntent);
                finish();
            }
        });

        //Get Result From index
        result = ((Curlingtomorrow)getApplication()).GetResultsList().get(index);

        //Read Temp.jpg
        imgTaken = findViewById(R.id.imgTaken_showresult);
        Glide.with(this)
                .load(GetImageFileFromInternal())
                .into(imgTaken);

        //Set Name, Score
        txtPlayerRed.setText(result.getPlayerRedName());
        txtPlayerYellow.setText(result.getPlayerYellowName());
        String scoreText = result.getPlayerRedScore()+":"+result.getPlayerYellowScore();
        txtScore.setText(scoreText);
    }

    private File GetImageFileFromInternal(){
        File storage = getFilesDir();
        File tempFile = new File(storage,result.getImageFileName());
        return tempFile;
    }

    private String GetImagePathFromInternal(){
        File storage = getFilesDir();
        File tempFile = new File(storage,result.getImageFileName());
        return tempFile.getAbsolutePath();
    }

    public void kakaolink() {
        //Upload Image
        File imageFile = new File(GetImagePathFromInternal());

        KakaoLinkService.getInstance()
                .uploadImage(this, true, imageFile, new ResponseCallback<ImageUploadResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "이미지 업로드 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(ImageUploadResponse result) {
                        Log.i("KAKAO_API", "이미지 업로드 성공");
                        Log.d("KAKAO_API", "URL: " + result.getOriginal().getUrl());

                        Result resultTemp = ShowResultActivity.this.result;
                        String winnerName, loserName, winnerScore;
                        if(resultTemp.isRedWinner()){
                            winnerName = resultTemp.getPlayerRedName();
                            loserName = resultTemp.getPlayerYellowName();
                            winnerScore = String.valueOf(resultTemp.getPlayerRedScore());
                        }
                        else{
                            winnerName = resultTemp.getPlayerYellowName();
                            loserName = resultTemp.getPlayerRedName();
                            winnerScore = String.valueOf(resultTemp.getPlayerYellowScore());
                        }

                        //Message Template
                        TemplateParams params = FeedTemplate
                                .newBuilder(ContentObject.newBuilder(
                                        "게임결과",
                                        result.getOriginal().getUrl(),
                                        LinkObject.newBuilder()
                                                .setWebUrl(result.getOriginal().getUrl())
                                                .setMobileWebUrl(result.getOriginal().getUrl())
                                                .build())
                                        .setDescrption(
                                                resultTemp.getDateTime()+"\n"+
                                                winnerName + " vs "+ loserName + " - "+
                                                        winnerScore+":0"
                                        )
                                        .build())
                                .setSocial(SocialObject.newBuilder()
                                        .setLikeCount(9999)
                                        .build())
                                .addButton(new ButtonObject(
                                        "크게보기",
                                        LinkObject.newBuilder()
                                                .setWebUrl(result.getOriginal().getUrl())
                                                .setMobileWebUrl(result.getOriginal().getUrl())
                                                .build()))
                                .build();

                        //Send via Kakaotalk
                        KakaoLinkService.getInstance()
                                .sendDefault(ShowResultActivity.this.getApplicationContext(), params, new ResponseCallback<KakaoLinkResponse>() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {

                                    }

                                    @Override
                                    public void onSuccess(KakaoLinkResponse result) {
                                        Log.e("KAKAO","SUCCESS");
                                    }
                                });
                    }
                });
    }
}
