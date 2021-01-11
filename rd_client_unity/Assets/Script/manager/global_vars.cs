

public class global_vars
{

    public static int CHINlD_ID = 102;  //101为 oppo 102 为 vivo
    private static global_vars _mInstance = null;

    public static int ADS_TYPE_BANNER = 1; //banner 广告
    public static int ADS_TYPE_INSERT = 2;   //插屏广告
    public static int ADS_TYPE_VOID = 3;    //激励视频
    public static int ADS_TYPE_OPEN = 4;    //开屏广告


    public static int AD_KEY_SHOP = 0;    //商城
    public static int AD_KEY_WIN = 1;    //结束的双倍

    public static int DEFAULT_COINS_NUM = 20; // 默认的添加金币数量

    private int ratio = 1;



    public static global_vars getInstance() {
        if (_mInstance == null) {
            _mInstance = new global_vars();
        
        }
        return _mInstance;
    }
    static bool isShow = false;

    public bool getIsShow() {
        return isShow;
    }
    public void setIsShow(bool _isShow_)
    {
        isShow = _isShow_;
    }

    public void setRatio(int _ratio_) {
        ratio = _ratio_;
    }

    public int  getRatio() {
        return ratio;
    }
}
