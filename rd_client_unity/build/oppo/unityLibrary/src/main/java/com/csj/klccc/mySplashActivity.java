package com.csj.klccc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.heytap.msp.mobad.api.ad.SplashAd;
import com.heytap.msp.mobad.api.listener.ISplashAdListener;
import com.heytap.msp.mobad.api.params.SplashAdParams;
import com.nearme.game.sdk.GameCenterSDK;
import com.unity3d.player.UnityPlayerActivity;

public class mySplashActivity extends Activity implements ISplashAdListener {
    private String OPEN_ADS_ID = "273296";
    private static mySplashActivity _mInatance = null;
    private SplashAd mSplashAd = null; //开屏广告
    /**
     * @author taomaogan
     * @Date 2019/8/16
     **/
    private static final String TAG = "CloseSplashActivity";
    protected SplashAd vivoSplashAd;



    private int mStartedCount = 0;
    public  static mySplashActivity getInstance() {
        if (_mInatance == null) {
            _mInatance = new mySplashActivity();
        }
        return _mInatance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       oppoSDKAgent.getInstance().initialized(this);
        String appSecret = "25a2b9f742cb46d1b42f5908cd257096";
        GameCenterSDK.init(appSecret, this);
        initSplashAd();
        //addView();
        // Get scheme param
        Log.i("shuifeng", "java init get scheme param");
    }

    private void addView() {
        Button button = new Button(mySplashActivity.this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(100, 100);
        layoutParams.topMargin = 100;
        button.setText("关闭");
        button.setBackgroundColor(Color.argb(180, 100,100,100));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSplashAd != null) {
                    Log.i("shuifeng", "onClick: clsoe");
                    endActive();
                }
            }
        });
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        viewGroup.addView(button, layoutParams);
    }

    protected void initSplashAd() {
        try {
            SplashAdParams splashAdParams = new SplashAdParams.Builder()
                    .setFetchTimeout(3000)
                    .setTitle("快乐冲冲冲")
                    .setDesc("冲就完事儿了")
                    .build();
//            mSplashAd =  mSplashAd = new SplashAd(this, OPEN_ADS_ID, new mySplashAdListener(), splashAdParams);
            mSplashAd =  mSplashAd = new SplashAd(this, OPEN_ADS_ID, this, splashAdParams);

        } catch (Exception err) {
            Log.e("shuifeng", "initSplashAd: err  is " + err );
            mSplashAd = null;
            endActive();
        }
    }

    public void endActive() {
        try{
            Intent it=new Intent(getApplicationContext(), UnityPlayerActivity.class);//启动MainActivity
            startActivity(it);
            finish();//关闭当前活动
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAdDismissed() {
        Log.d("shuifeng", "onADDismissed：");
        endActive();
    }
    @Override
    public void onAdShow() {

    }
    @Override
    public void onAdFailed(String s) {

    }
    @Override
    public void onAdFailed(int i, String s) {
        Log.d("shuifeng", "onADDismissed：" + i + "  " + s);
        endActive();
    }
    @Override
    public void onAdClick() {

    }

//    //开屏广告
//    public class mySplashAdListener implements ISplashAdListener {
//        @Override
//        public void onAdDismissed() {
//            Log.d("shuifeng", "onADDismissed：");
//            endActive();
//        }
//        @Override
//        public void onAdShow() {
//
//        }
//        @Override
//        public void onAdFailed(String s) {
//
//        }
//        @Override
//        public void onAdFailed(int i, String s) {
//            Log.d("shuifeng", "onADDismissed：" + i + "  " + s);
//            endActive();
//        }
//        @Override
//        public void onAdClick() {
//
//        }
//    }
}

