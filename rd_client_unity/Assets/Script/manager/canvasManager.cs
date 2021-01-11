using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using TMPro;

public class canvasManager : MonoBehaviour
{
    public static canvasManager Instance;
    [Header("Panels")]
    public GameObject tipsPanel;
    public GameObject privacyPanel;
    public GameObject gameExitPanel;
    public GameObject winPanel;
    public GameObject healthNotePanel;
    public GameObject LoosePanel;
    public GameObject gamepanel;
    public RectTransform coinReachPos;
    public ParticleControlScript pS;
    public GameObject TempTitle;
    public GameObject TempShopText_0;
    public GameObject TempShopText_1;
    public GameObject noAd;
    public GameObject moreGameBtn;


    int levelNo = 0;
    int levelShowNo = 1;
    public TextMeshProUGUI levelText,coinText,CoinShopText,noText;
    public Transform shopParent;
    public Sprite[] charIcons;
    public int coins = 0;
    public int coinsNo = 0;


    public bool[] adsReady = new bool[5];

    private void Awake()
    {
        if (!Instance)
            Instance = this;

        TempTitle.SetActive(false);
        TempShopText_0.SetActive(false);
        TempShopText_1.SetActive(false);
        moreGameBtn.SetActive(global_vars.CHINlD_ID == 101 ? true : false);

        for (int i = 0; i < 5; ++i)
        {

            adsReady[i] = false;
        }

    }
    private void Start()
    {
   
        levelNo = SceneManager.GetActiveScene().buildIndex;
        if(AudioManager.instance)
        AudioManager.instance.Play("bg");
        levelShowNo = PlayerPrefs.GetInt("levelshow", 1);
        levelText.text = "Level " + levelShowNo;
        coins = PlayerPrefs.GetInt("coins", 0);
        CoinUpdate();
        tipsPanel.SetActive(true);
       checkAndCloseHealthNoticePanel();

    }
    // Update is called once per frame
    void Update()
    {
        //ExitGame();
    }
    public void CoinUpdate()
    {
        coinText.text = coins.ToString();
        CoinShopText.text = coins.ToString();
        PlayerPrefs.SetInt("coins", coins);
    }
    public void RetryMethod()
    {

        if (getAdsIsReady(global_vars.ADS_TYPE_INSERT) == true)
        {
            showAds(global_vars.ADS_TYPE_INSERT);
        }
        SceneManager.LoadScene(levelNo);
    }

    public void RewardButton()
    {
        //int random = Random.Range(0, shopParent.childCount);
        //shopScript sScript = shopParent.GetChild(random).GetComponent<shopScript>();

        //if (!sScript.isUnlocked)
        //{
        //    sScript.BuyButton();
        //}
        //else
        //    RewardButton();
        coins += 150;
        CoinUpdate();
    }
    public void DoubleCoins()
    {
        if (getAdsIsReady(global_vars.ADS_TYPE_VOID) == true)
        {
            showAds(global_vars.ADS_TYPE_VOID, global_vars.AD_KEY_WIN);
        }
        else
        {
            noAd.SetActive(true);
            Invoke("closeAd", 1.0f);
        }
       

        
    }

    public void UnlockRandom()
    {
        if(coins>=150)
        {
            int random = Random.Range(0, shopParent.childCount);
            shopScript sScript = shopParent.GetChild(random).GetComponent<shopScript>();

            if (!sScript.isUnlocked)
            {
                sScript.BuyButton();
            }
            else
                UnlockRandom();
            if(coins>=150)
            {
                coins -= 150;
                PlayerPrefs.SetInt("coins", coins);
                CoinUpdate();
            }
            
        }
        
    }
    public void NextLevel()
    {

        if (getAdsIsReady(global_vars.ADS_TYPE_INSERT) == true) {
            showAds(global_vars.ADS_TYPE_INSERT);
        }
        levelNo++;
        levelShowNo++;
        PlayerPrefs.SetInt("level", levelNo);
        PlayerPrefs.SetInt("levelshow", levelShowNo);
        SceneManager.LoadScene(levelNo);
    }
    public void FailMethod()
    {
        AudioManager.instance.Pause("bg");
        gamepanel.SetActive(false);
        LoosePanel.SetActive(true);
    }
    public void WinMethod()
    {
        AudioManager.instance.Pause("bg");
        pS.coinsCount = coinsNo;
        pS.PlayControlledParticles(new Vector2(Screen.width / 2, Screen.height / 2), coinReachPos);
        gamepanel.SetActive(false);
        winPanel.SetActive(true);
        


    }

    public void playButton()
    {
        if (getAdsIsReady(global_vars.ADS_TYPE_BANNER) == true)
            Debug.Log("shuifeng: C# Banner state is " + getAdsIsReady(global_vars.ADS_TYPE_BANNER));
        {
            showAds(global_vars.ADS_TYPE_BANNER);
        }

        GameManager.Instance.startGame = true;

       
    }

    public void VideoUnlock()
    {
        if (getAdsIsReady(global_vars.ADS_TYPE_VOID) == true)
        {
            showAds(global_vars.ADS_TYPE_VOID, global_vars.AD_KEY_SHOP);
        }
        else
        {
            noAd.SetActive(true);
            Invoke("closeAd", 1.0f);
        }

    }
    public void closeAd() {
        noAd.SetActive(false);
    }

    public void loopmethod()
    {
        levelNo = 1;
        levelShowNo++;
        PlayerPrefs.SetInt("level", levelNo);
        PlayerPrefs.SetInt("levelshow", levelShowNo);
        //AdManager.instance.showInterstitial();
        SceneManager.LoadScene(levelNo);
    }
    public void onClickConfirmBtn() {
        Debug.Log("shuifeng: onClickConfirmBtn");

        tipsPanel.SetActive(false);

        Invoke("delayClsoePanel", 2.0f);


    }
    public void onClickPrivacyBtn() {
        privacyPanel.SetActive(true);
    }
    public void onClickExitBtn() {
        privacyPanel.SetActive(false);
    }
    public void ExitGame()
    {
        //这个监听在update里面做
        if (Input.GetKeyDown(KeyCode.Escape) || Input.GetKeyDown(KeyCode.Home))
        {
            //gameExitPanel.SetActive(true); //展示退出游戏的面板
            Debug.Log("1");
        }
    }
    public void confirmExitGmae()
    {
        //退出游戏
        Application.Quit();
    }

    public void cancleExitGame()
    {
       // gameExitPanel.SetActive(false );//隐藏退出游戏的面板
    }
    private void delayClsoePanel()
    {

        healthNotePanel.SetActive(false);
        
    }
    //检测隐私的提示框
    private bool checkIsFirstShowTips()
    {
        bool isFirst = PlayerPrefs.HasKey("is_first");

        if (isFirst == true) {
            return false;
        }
        PlayerPrefs.SetInt("is_first", 1);
        return true;
    }

    private void checkAndCloseHealthNoticePanel() {
        //第一次打开游戏的时候回有有隐私政策的提示框
        //然后也有健康公告
        if (checkIsFirstShowTips() == true)
        {
            healthNotePanel.SetActive(true);
            tipsPanel.SetActive(true);
            global_vars.getInstance().setIsShow(true);
        }
        //不是第一次进游戏，但是要检测是否显示过健康公告
        else
        {
            tipsPanel.SetActive(false);
            if (global_vars.getInstance().getIsShow() != true)
            {
                healthNotePanel.SetActive(true);
                Invoke("delayClsoePanel", 1.5f);
                global_vars.getInstance().setIsShow(true);
            }
            else {
                healthNotePanel.SetActive(false);
            }
            

        }
    }

    public void onClickMoreGameBtn() {
        Debug.Log("shuifeng: canvase onClickMoreGameBtn");
        callJaveTools.getInstance().onCallJavaMenthod("onClickMoreGameBtn");
    }

    public void testFunc() {
        Debug.Log("shuifeng: C# canvase testFunc");
    }


    ///////////////////////////////////
    ///
    public void onAdsReady(string _adsType_)
    {
        Debug.Log("shuifeng: C# admanger onAdsReady _adsType_ is " + _adsType_);
        int tempIndex = int.Parse(_adsType_);
        adsReady[tempIndex] = true;
        Debug.Log("shuifeng: C#  admanger onAdsReady _adsType_ is " + adsReady[tempIndex]);
    }

    public void showAds(int _adsType_, int adkey = 0 )
    {
        Debug.Log("shuifeng: admanger showAds _adsType_ is " + _adsType_);
        if (adsReady[_adsType_] == true)
        {
            string tempAdtype = _adsType_.ToString();
            string tempadKey = adkey.ToString();

            string tempStr = tempAdtype + "," + tempadKey;

            callJaveTools.getInstance().onCallJavaMenthod("showAds", tempStr);
            adsReady[_adsType_] = false;
        }
        else
        {
            Debug.Log("shuifeng: C# admanger  loadAgain is " + _adsType_);
            callJaveTools.getInstance().onCallJavaMenthod("loadAgain", "" + _adsType_);
        }
    }

    public bool getAdsIsReady(int _adsType_)
    {
        Debug.Log("shuifeng:  C# admanger getAdsIsReady _adsType_ is " + _adsType_);
        if (_adsType_ < global_vars.ADS_TYPE_BANNER || _adsType_ > global_vars.ADS_TYPE_OPEN)
        {
            Debug.Log("shuifeng:  C# not suuppot _adsType_" + _adsType_);
            return false;
        }

        Debug.Log("shuifeng:  C# admanger getAdsIsReady _adsType_ is " + adsReady[_adsType_]);
        if (adsReady[_adsType_] == false)
        {
            Debug.Log("shuifeng: C# admanger  loadAgain is " + _adsType_);
            callJaveTools.getInstance().onCallJavaMenthod("loadAgain", "" + _adsType_);
        }
        return adsReady[_adsType_];
    }

    //激励视频广告是否播放完成的回调
    public void isReworldVideoComplete(string _str_)
    {

        string[] tempArray = _str_.Split(',');
        int tempCode = int.Parse(tempArray[0]);
        int tempAdKey = int.Parse(tempArray[1]);
        Debug.Log("shuifeng: C# isReworldVideoComplete  tempCode is " + _str_);
        if (tempCode == 0)
        {
            int tempNum = 150;
            if (tempAdKey == global_vars.AD_KEY_SHOP)
            {
                tempNum = 150;
                coins += tempNum;
                CoinUpdate();
                Debug.Log("shuifeng: C# isReworldVideoComplete money is " + coins);
            }
            else if (tempAdKey == global_vars.AD_KEY_WIN) {
                int tempRatio = global_vars.getInstance().getRatio();
                tempNum =  tempRatio * global_vars.DEFAULT_COINS_NUM;
                int tempMoney = PlayerPrefs.GetInt("coins");
                tempMoney += tempNum;
                PlayerPrefs.SetInt("coins", tempMoney);
                NextLevel();
                Debug.Log("shuifeng: C# isReworldVideoComplete money is " + tempMoney);
            }

        }
    }
}
