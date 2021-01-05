package com.cjs.klccc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayerActivity;
import com.vivo.ad.model.AdError;
import com.vivo.ad.splash.SplashAdListener;
import com.vivo.mobilead.model.BackUrlInfo;
import com.vivo.mobilead.model.VivoAdError;
import com.vivo.mobilead.splash.SplashAdParams;
import com.vivo.mobilead.splash.VivoSplashAd;
import com.vivo.mobilead.util.VADLog;

public class CloseSplashActivity extends Activity{
    private String OPEN_ADS_ID = "9b01cb9448e74f9da78574997e9cc876";
    private static CloseSplashActivity _mInatance = null;
    private VivoSplashAd mSplashAd = null; //开屏广告
    /**
     * @author taomaogan
     * @Date 2019/8/16
     **/
        private static final String TAG = "CloseSplashActivity";
        protected VivoSplashAd vivoSplashAd;
        protected SplashAdParams.Builder builder;
        public boolean canJump = false;

        private int mStartedCount = 0;
    public  static CloseSplashActivity getInstance() {
        if (_mInatance == null) {
            _mInatance = new CloseSplashActivity();
        }
        return _mInatance;
    }
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vivoSDKAgent.getInstance().initialized(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_splash);
        initSplashAd();
        addView();
        // Get scheme param
        Log.i("shuifeng", "java init get scheme param");
    }

    private void addView() {
        Button button = new Button(CloseSplashActivity.this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(50, 50);
        layoutParams.topMargin = 100;
        button.setText("关闭");
//        button.setBackgroundColor();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSplashAd != null) {
                    mSplashAd.close();
                    Log.i("shuifeng", "onClick: clsoe");
                    endActive();
                    

                }
            }
        });
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        viewGroup.addView(button, layoutParams);
    }

        protected void initSplashAd() {
            SplashAdParams.Builder builder = new SplashAdParams.Builder(OPEN_ADS_ID);
            // 拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取范围[3000, 5000]
            builder.setFetchTimeout(3000);
// 广告下面半屏的应用标题+应用描述:应用标题和应用描述是必传字段，不传将抛出异常
// 标题最长5个中文字符描述最长8个中文字符
            builder.setAppTitle("快乐冲冲冲");
            builder.setAppDesc("冲就完事儿了");
            //backUrl为针对特殊广告主要求而设，接入方可以不设置这块
            String backUrl = "vivobrowser://browser.vivo.com?i=12";
            String btnName = "test";
            builder.setBackUrlInfo(new BackUrlInfo(backUrl, btnName));
            // 3.0.0.0版本新增支持横屏广告，通过如下api设置横竖屏广告，默认竖屏（还需要注意的是，此处设置横竖屏需要和AndroidManifest里面对应的开盘Activity声明的横竖屏一致，否则会报错）
            builder.setSplashOrientation(SplashAdParams.ORIENTATION_PORTRAIT);

            mSplashAd = new VivoSplashAd(this, new mySplashAdListener(), builder.build());
            //一定要主动 load 广告
            mSplashAd.loadAd();
        }

        protected void loadAd(Activity activity, SplashAdListener listener) {
            vivoSplashAd = new VivoSplashAd(activity, listener, builder.build());
            vivoSplashAd.loadAd();
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

    //开屏广告
    public class mySplashAdListener implements SplashAdListener {

        @Override
        public void onADDismissed() {

            Log.d("shuifeng", "onADDismissed：");
            mSplashAd.close();
            endActive();
        }

        @Override
        public void onNoAD(AdError adError) {
            VADLog.d("shuifeng", "onNoAD:" + adError.getErrorMsg());
            Log.d("shuifeng", "没有广告：" + adError.getErrorMsg());
            mSplashAd.close();
            endActive();

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
}
//
////import com.csj.klccc.CloseSplashActivity.R;
//public class SplashActivity extends Activity {
//
//    private static final android.R.attr R = ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_splash);
//
//        // Get scheme param
//        Log.i("hgktexas", "java init get scheme param");
//
//        Intent intent = getIntent();
//        Thread myThread=new Thread(){//创建子线程
//            @Override
//            public void run() {
//                try{
//                    sleep(1000);//使程序休眠五秒
//                    Intent it=new Intent(getApplicationContext(), UnityPlayerActivity.class);//启动MainActivity
//                    startActivity(it);
//                    finish();//关闭当前活动
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        myThread.start();//启动线程
//    }
//
//    @Override
//    public void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        // Get scheme param
//        Log.i("hgktexas", "java onNewIntent get scheme param");
//
//        String action = intent.getAction();
//        if (Intent.ACTION_VIEW.equals(action)) {
//            Uri uri = intent.getData();
//            if(uri != null){
//                String param = intent.getDataString();
//                if(param != null && !param.equals("")){
//                    SchemeMgr.getInstance().setSchemeEvent(param);
//                }else{
//                    Log.i( "hgktexas","onNewIntent param == null or param.equals()" );
//                }
//            }
//        }
//    }
//
//    public static void scaleImage(final Activity activity, final View view, int drawableResId) {
//
//        // 获取屏幕的高宽
//        Point outSize = new Point();
//        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
//
//        // 解析将要被处理的图片
//        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
//
//        if (resourceBitmap == null) {
//            return;
//        }
//
//        // 开始对图片进行拉伸或者缩放
//
//        // 使用图片的缩放比例计算将要放大的图片的高度
//        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());
//
//        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
//        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);
//
//        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                //这里防止图像的重复创建，避免申请不必要的内存空间
//                if (scaledBitmap.isRecycled())
//                    //必须返回true
//                    return true;
//
//
//                // 当UI绘制完毕，我们对图片进行处理
//                int viewHeight = view.getMeasuredHeight();
//
//
//                // 计算将要裁剪的图片的顶部以及底部的偏移量
//                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;
//
//
//                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
//                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
//                        scaledBitmap.getHeight() - offset * 2);
//
//
//                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
//                    scaledBitmap.recycle();
//                    System.gc();
//                }
//
//
//                // 设置图片显示
//                view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), finallyBitmap));
//                return true;
//            }
//        });
//    }
//}