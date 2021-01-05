

public class global_vars
{

    public static int CHINlD_ID = 102;  //101为 oppo 102 为 vivo
    private static global_vars _mInstance = null;

    public static int ADS_TYPE_BANNER = 1; //banner 广告
    public static int ADS_TYPE_INSERT = 2;   //插屏广告
    public static int ADS_TYPE_VOID = 3;    //激励视频
    public static int ADS_TYPE_OPEN = 4;    //开屏广告

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
}
