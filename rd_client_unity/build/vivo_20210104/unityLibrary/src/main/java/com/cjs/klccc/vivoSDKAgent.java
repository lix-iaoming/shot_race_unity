package com.cjs.klccc;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qq.e.ads.splash.SplashADListener;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.vivo.ad.model.AdError;
import com.vivo.ad.splash.SplashAdListener;
import com.vivo.ad.video.VideoAdListener;
import com.vivo.mobilead.banner.BannerAdParams;
import com.vivo.mobilead.banner.VivoBannerAd;
import com.vivo.mobilead.interstitial.InterstitialAdParams;
import com.vivo.mobilead.listener.IAdListener;
import com.vivo.mobilead.manager.VivoAdManager;
import com.vivo.mobilead.model.BackUrlInfo;
import com.vivo.mobilead.splash.SplashAdParams;
import com.vivo.mobilead.splash.VivoSplashAd;
import com.vivo.mobilead.unified.banner.UnifiedVivoBannerAd;
import com.vivo.mobilead.unified.banner.UnifiedVivoBannerAdListener;
import com.vivo.mobilead.unified.base.AdParams;
import com.vivo.mobilead.unified.base.VivoAdError;
import com.vivo.mobilead.unified.reward.UnifiedVivoRewardVideoAd;
import com.vivo.mobilead.unified.reward.UnifiedVivoRewardVideoAdListener;
import com.vivo.mobilead.util.VADLog;
import com.vivo.mobilead.util.VOpenLog;
import com.vivo.mobilead.video.VideoAdParams;
import com.vivo.mobilead.video.VivoVideoAd;
import com.vivo.unionsdk.open.VivoExitCallback;
import com.vivo.unionsdk.open.VivoUnionSDK;

import static com.bun.miitmdid.core.JLibrary.context;

public class vivoSDKAgent {
    private static vivoSDKAgent _mInatance = null;
    private Activity _mActivity;
    private UnityPlayerActivity _mActive;
    private FrameLayout mFrameLayout; //广告展示的layout
    private View BannerView;
    private  int adsKay;

    public boolean uiIsComplete = false;

    private  final  int  ADS_TYPE_BANNER = 1; //banner 广告
    private  final  int  ADS_TYPE_INSERT = 2;   //插屏广告
    private  final  int  ADS_TYPE_VOID = 3;    //激励视频
    private  final  int  ADS_TYPE_OPEN = 4;    //开屏广告


    private VivoSplashAd mSplashAd = null; //开屏广告
    private UnifiedVivoBannerAd mBannerAd = null;     //banner
    private InterstitialAdParams mInterstitialAd = null; //插屏
    private VivoVideoAd mRewardVideoAd = null;   //视频激励

    private String APP_ID = "902d7db4d3ff44e4b4dad8fdcf6c2e45";
    private String BANNER_ADS_ID = "04a8a8a10878486db32887d43d7dd6d0";
    private String INSERT_ADS_ID = "273295";
    private String VIDEO_ADS_ID = "5ec9742eaf0940caadb681fd18a45424";
    private String OPEN_ADS_ID = "9b01cb9448e74f9da78574997e9cc876";


    public  static vivoSDKAgent getInstance() {
        if (_mInatance == null) {
            _mInatance = new vivoSDKAgent();
        }
        return _mInatance;
    }

    //初始化接口
    public void initialized(Activity mActivity) {
        _mActivity = mActivity;
        String appId = "105329301";
        VivoUnionSDK.initSdk(_mActivity,  "105329301", false);
        Log.i("shuifeng", "initialized: ");

        VivoAdManager.getInstance().init(_mActivity.getApplication(),APP_ID);
        //开启日志输出
        VOpenLog.setEnableLog(true);
       // initSplashAd();
        if (uiIsComplete == true) {

            initVideoAds();
            initBannerAds();
            initInsertAds();
        }

    }

//    private void fetchSplashAD(Activity activity, String posId, SplashADListener listener) {
//        SplashAdParams.Builder builder = new SplashAdParams.Builder(posId);
//        // 拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取值范围[3000, 5000]
//        builder.setFetchTimeout(3000);
//        builder.setAppTitle("csj");
//        builder.setAppDesc(" ");
//        // 3.0.0.0版本新增支持横屏广告，通过如下api设置横竖屏广告，默认竖屏（还需要注意的是，此
//        // 处设置横竖屏需要和AndroidManifest里面对应的开盘Activity声明的横竖屏一致，否则会报错）
//        builder.setSplashOrientation(SplashAdParams.ORIENTATION_LANDSCAPE);
//        vivoSplashAd= new VivoSplashAd(activity, new mySplashAdListener(), builder.build());
//        vivoSplashAd.loadAd();
//    }


    public void gameExitGame(){

        VivoUnionSDK.exit(_mActivity, new myVivoExitCallback());
    }
    public void initBannerAds () {
        Log.i("shuifeng ", "initBannerAds: "+ mBannerAd);
        if (mBannerAd == null) {

            Log.i("shuifeng ", "initBannerAds: mBannerAd is " + mBannerAd);

            AdParams.Builder builder = new AdParams.Builder(BANNER_ADS_ID);
            //设置 banner 刷新频率（推荐使用）
            builder.setRefreshIntervalSeconds(30);

            mBannerAd = new UnifiedVivoBannerAd(_mActivity, builder.build(), new myUnifiedVivoBannerAdListener());
        }
        mBannerAd.loadAd();
    }
    public void initInsertAds () {
    }
    public void initVideoAds() {
        Log.i("shuifeng","initVideoAds" );
        if (mRewardVideoAd == null) {
            VideoAdParams.Builder builder = new VideoAdParams.Builder(VIDEO_ADS_ID);
            mRewardVideoAd = new VivoVideoAd(_mActivity, builder.build(), new myVideoAdListener());
        }
        mRewardVideoAd.loadAd();
//        VideoAdParams = = new UnifiedVivoRewardVideoAd(_mActivity,
//                new AdParams.Builder())
    }

    public void initSplashAd() {

        SplashAdParams.Builder builder = new SplashAdParams.Builder(OPEN_ADS_ID);
        // 拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取范围[3000, 5000]
        builder.setFetchTimeout(3000);
// 广告下面半屏的应用标题+应用描述:应用标题和应用描述是必传字段，不传将抛出异常
// 标题最长5个中文字符描述最长8个中文字符
        builder.setAppTitle("广告联盟");
        builder.setAppDesc("娱乐休闲首选游戏");
        //backUrl为针对特殊广告主要求而设，接入方可以不设置这块
        String backUrl = "vivobrowser://browser.vivo.com?i=12";
        String btnName = "test";
        builder.setBackUrlInfo(new BackUrlInfo(backUrl, btnName));
    // 3.0.0.0版本新增支持横屏广告，通过如下api设置横竖屏广告，默认竖屏（还需要注意的是，此处设置横竖屏需要和AndroidManifest里面对应的开盘Activity声明的横竖屏一致，否则会报错）
        builder.setSplashOrientation(SplashAdParams.ORIENTATION_PORTRAIT);

        mSplashAd = new VivoSplashAd(_mActivity, new mySplashAdListener(), builder.build());
        //一定要主动 load 广告
        mSplashAd.loadAd();
    }

    public void  onAdsReady(int _adstype_) {
        Log.i("shuifeng", "java onAdsReady _adstype_ " + _adstype_);


        Log.i("shuifeng", "java onAdsRead 0" );
        UnityPlayer.UnitySendMessage("Canvas", "onAdsReady", ""+_adstype_);
        Log.i("shuifeng", "java onAdsRead 1" );



    }

    public void showAds(int _type_, int _adKey_) {
        switch (_type_) {
            case ADS_TYPE_BANNER: {
                Log.i("shuifeng", "showBanner : ");
                if (mBannerAd == null ) {
                    Log.i("shuifeng", "showBanner : load");
                    initBannerAds();
                }else  {
                    Log.i("shuifeng", "showBanner : show");
                }
            }break;
            case ADS_TYPE_INSERT: {
                if (mInterstitialAd == null) {
                    initInsertAds();
                }
            }break;
            case ADS_TYPE_VOID: {
                if (mRewardVideoAd == null ) {
                    initVideoAds();
                }
                adsKay = _adKey_;
                mRewardVideoAd.showAd(_mActivity);

            }break;
            case ADS_TYPE_OPEN: {

            }break;
            default: {
                Log.e("shuifeng", "showAds: "+ "not support ads type is " + _type_);
            }

        }
    }


    public void loadAgain(int _type_) {
        Log.i("shuifeng","java loadAgain");
        switch (_type_) {
            case ADS_TYPE_BANNER:{
                initBannerAds();
            }  break;
            case ADS_TYPE_INSERT:{
                initInsertAds();
            }  break;
            case ADS_TYPE_VOID:{
                initVideoAds();
            }  break;
            default: {
                Log.e("shuifeng ", "loadAgain: not suppprt ads _type_ is  "+ _type_);
            }
        }
    }
    class myVivoExitCallback implements VivoExitCallback {

        @Override
        public void onExitCancel() {
            //
            Log.i("shuifeng", "onExitCancel: ");
        }

        @Override
        public void onExitConfirm() {
            //退出的处理
            _mActivity.finish();
            Log.i("shuifeng", "onExitConfirm: ");
        }
    }
    //开屏广告
    public class mySplashAdListener implements SplashAdListener {

        @Override
        public void onADDismissed() {

            Log.d("shuifeng", "onADDismissed：");
            mSplashAd.close();

        }

        @Override
        public void onNoAD(AdError adError) {
            VADLog.d("shuifeng", "onNoAD:" + adError.getErrorMsg());
            Log.d("shuifeng", "没有广告：" + adError.getErrorMsg());
            mSplashAd.close();

        }

        @Override
        public void onADPresent() {
            Log.i("shuifeng", "onADPresent: ");
        }

        @Override
        public void onADClicked() {
            mSplashAd.close();
        }
    }
    //banner监听
    class myUnifiedVivoBannerAdListener implements UnifiedVivoBannerAdListener {
        @Override
        public void onAdShow() {
            Log.i("shuifeng","Banner onAdShow");
        }

        @Override
        public void onAdFailed(VivoAdError vivoAdError) {
            Log.e("shuifeng","Banner onAdFailed " + vivoAdError);
        }

        @Override
        public void onAdReady(View view) {
            Log.i("shuifeng"," Banner onAdReady");
            onAdsReady(ADS_TYPE_BANNER);
            Log.i("shuifeng"," Banner onAdReady");
            BannerView= view;
            if (null != BannerView) {
//                        mFrameLayout.addView(adView);

                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                tvParams.gravity = Gravity.BOTTOM | Gravity.START;

                WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int tempHeight = size.y - 100;
                Log.e("shuifeng", "onAdReady: tempHeight == "+tempHeight );
                tvParams.setMargins(0, tempHeight, tvParams.rightMargin, tvParams.bottomMargin);
                _mActivity.addContentView(BannerView, tvParams);
            }
        }

        @Override
        public void onAdClick() {
            Log.i("shuifeng"," Banner onAdClick");
        }

        @Override
        public void onAdClose() {
            Log.i("shuifeng"," Banner onAdClose");
            mBannerAd.destroy();

        }

//        @Override
//        public void onAdShow() {
//            Log.i("shuifeng","Banner onAdShow");
//        }
//
//        @Override
//        public void onAdFailed(VivoAdError vivoAdError) {
//            Log.e("shuifeng","Banner onAdFailed " + vivoAdError);
//        }
//
//        @Override
//        public void onAdReady() {
//            onAdsReady(ADS_TYPE_BANNER);
//            Log.i("shuifeng"," BanneronAdReady");
//        }
//
//        @Override
//        public void onAdClick() {
//            Log.i("shuifeng","Banner onAdClick");
//        }
//
//        @Override
//        public void onAdClosed() {
//            Log.i("shuifeng","Banner onAdClosed");
//        }
    }
    //视频激励广告的监听
    class myVideoAdListener implements VideoAdListener {

        @Override
        public void onAdLoad() {
            onAdsReady(ADS_TYPE_VOID);
            Log.i("shuifeng"," myVideoAdListener onAdLoad");
        }

        @Override
        public void onAdFailed(String s) {
            Log.i("shuifeng"," myVideoAdListener onAdFailed" + s);
        }

        @Override
        public void onVideoStart() {
            Log.i("shuifeng","myVideoAdListener onVideoStart");
        }

        @Override
        public void onVideoCompletion() {
            Log.i("shuifeng","myVideoAdListener onVideoCompletion");

        }

        @Override
        public void onVideoClose(int i) {
            Log.i("shuifeng","myVideoAdListener onVideoClose");
        }

        @Override
        public void onVideoCloseAfterComplete() {
            Thread myThread=new Thread(){//创建子线程
                @Override
                public void run() {
                    try{
                        sleep(1000);//使程序休眠1秒
                        Log.i("shuifeng", "onVideoPlayComplete: ");
                        UnityPlayer.UnitySendMessage("Canvas", "isReworldVideoComplete", "0," + adsKay);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            myThread.start();//启动线程

            //通知unity 发奖励

        }

        @Override
        public void onVideoError(String s) {
            Log.i("shuifeng","myVideoAdListener onVideoError  " + s);
        }

        @Override
        public void onFrequency() {
            Log.i("shuifeng","myVideoAdListener onFrequency  ");
        }

        @Override
        public void onNetError(String s) {
            Log.i("shuifeng","myVideoAdListener onNetError  " + s);
        }

        @Override
        public void onRequestLimit() {
            Log.i("shuifeng","myVideoAdListener onRequestLimit  ");
        }
    }
}

/*
<?xml version="1.0" encoding="utf-8"?>
<!-- GENERATED BY UNITY. REMOVE THIS COMMENT TO PREVENT OVERWRITING WHEN EXPORTING AGAIN-->
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.unity3d.player" xmlns:tools="http://schemas.android.com/tools">

  <!--联运SDK跳转游戏中心-->
  <uses-permission android:name="vivo.game.permission.OPEN_JUMP_INTENTS"/>
  <!--联运SDK监听网络状态变化，在支付登录过程中做一些异常处理-->
  <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
  <!--允许程序访问Wi-Fi网络状态信息-->
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
  <!--允许程序打开网络套接字-->
  <uses-permission android:name="android.permission.INTERNET"/>
  <!--允许程序访问有关GSM网络信息-->
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
  <!--读取imei 这个要动态获取 否则数据统计会异常-->
  <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
  <!--联运sdk写入内容到sd卡-->
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
  <!--强制安装联运APK时，需要从SDK中读取APK文件-->
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
  <!--判断游戏是否是在主进程初始化，避免初始化进程错误导致功能不可用-->
  <uses-permission android:name="android.permission.GET_TASKS"/>
  <!--获取安装权限-->
  <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>

  <!-- SDK 必须的权限 -->
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <!--此权限一定要加，否则下载类广告不会进入安装流程，影响后向体验-->
  <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
  <!--可选权限-->
  <!--如果需要精确定位的话请加上此权限-->
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <!-- 如果接入了视频相关的广告, 请务必添加否则黑屏 -->
  <uses-permission android:name="android.permission.WAKE_LOCK" />

  <application>
    <!-- 激励视频需要开启硬件加速 -->
    android:hardwareAccelerated="true"
    <meta-data
        android:name="vivo_union_sdk"
        android:value="4.6.0.1" />

    <activity
        android:name="com.unity3d.player.UnityPlayerActivity"
        android:theme="@style/UnityThemeSelector"
        android:screenOrientation="sensorPortrait"
        android:launchMode="singleTask"
        android:configChanges="mcc|mnc|locale|touchscreen|keyboard|keyboardHidden|navigation|orientation|screenLayout|uiMode|screenSize|smallestScreenSize|fontScale|layoutDirection|density"
        android:hardwareAccelerated="false">
<!--        android:theme="@style/Theme.notAnimation" -->


    </activity>

    <activity
        android:name="com.cjs.klccc.CloseSplashActivity"
        android:screenOrientation="sensorPortrait"
        android:configChanges="orientation|keyboardHidden|screenSize"
        android:label="@string/app_name"
        tools:ignore="DuplicateActivity">


      <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
      <meta-data android:name="unityplayer.UnityActivity" android:value="true" />
      <meta-data android:name="android.notch_support" android:value="true" />
    </activity>
    <meta-data android:name="unity.splash-mode" android:value="0" />
    <meta-data android:name="unity.splash-enable" android:value="True" />
    <meta-data android:name="notch.config" android:value="portrait|landscape" />
    <meta-data android:name="unity.build-id" android:value="20cdc116-1139-4f96-87df-f32c7f210446" />
    <!-- vivo sdk componets start -->

    <!--vivo sdk的Activity-->
    <activity android:name="com.vivo.unionsdk.ui.UnionActivity"
        android:configChanges="orientation|keyboardHidden|navigation|screenSize"
        android:theme="@android:style/Theme.Dialog"
        android:exported="false"
        tools:ignore="AppLinkUrlError">
      <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data
            android:scheme="vivounion"
            android:host="union.vivo.com"
            android:path="/openjump"/>
      </intent-filter>
    </activity>
    <!-- vivo sdk componets end -->




    <!-- SDK webview 页面声明 -->
    <activity
        android:name="com.vivo.mobilead.web.VivoADSDKWebView"
        android:configChanges="orientation|keyboardHidden|screenSize">
    </activity>
    <!-- SDK webview 页面声明-->
    <!--如果接入了旧版激励视频，这个必须配置，否则应用会崩溃 -->
    <activity
        android:name="com.vivo.mobilead.video.RewardVideoActivity"
        android:hardwareAccelerated="true"
        android:screenOrientation="portrait"
        android:configChanges="orientation|keyboardHidden|screenSize" />
    <!--如果接入了插屏视频，这个必须配置，否则应用会崩溃 -->
    <activity
        android:name="com.vivo.mobilead.unified.interstitial.InterstitialVideoActivity"
        android:hardwareAccelerated="true"
        android:screenOrientation="landscape"
        android:configChanges="orientation|keyboardHidden|screenSize" />

    <!--用于判断 sdk 版本，android:value 指为 sdk 版本号-->
    <meta-data android:name="vivo_ad_version_code" android:value="4424"/>



  </application>
  <uses-feature android:glEsVersion="0x00020000" />
  <supports-gl-texture android:name="GL_KHR_texture_compression_astc_ldr" />
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-feature android:name="android.hardware.touchscreen" android:required="false" />
  <uses-feature android:name="android.hardware.touchscreen.multitouch" android:required="false" />
  <uses-feature android:name="android.hardware.touchscreen.multitouch.distinct" android:required="false" />
</manifest>


public void showAds(String _adsType_) {
        Log.d("shuifeng", "showAds: " + _adsType_);
        int tempIndex = Integer.parseInt(_adsType_);
        vivoSDKAgent.getInstance().showAds(tempIndex);
    }

public void loadAgain(String _adsType_) {
        Log.d("shuifeng", "showAds: " + _adsType_);
        int tempIndex = Integer.parseInt(_adsType_);
        vivoSDKAgent.getInstance().loadAgain(tempIndex);
    }

    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
            vivoSDKAgent.getInstance().gameExitGame();
        }

        vivoSDKAgent.getInstance().initialized(this);

 */
