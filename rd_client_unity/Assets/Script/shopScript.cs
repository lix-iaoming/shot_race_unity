using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
public class shopScript : MonoBehaviour
{
    public Image charimage;
    public bool isUnlocked = false;
    // Start is called before the first frame update
    void Start()
    {
       
        int _No = PlayerPrefs.GetInt(transform.name, 0);
        if(_No==1)
        {
            isUnlocked = true;
            transform.GetChild(0).gameObject.SetActive(true);
            transform.GetChild(2).gameObject.SetActive(true);
        }
        if (GameManager.Instance.playerCharNo == transform.GetSiblingIndex())
        {
            transform.GetChild(1).gameObject.SetActive(true);
        }
        charimage = transform.GetChild(2).GetComponent<Image>();
        charimage.sprite = canvasManager.Instance.charIcons[transform.GetSiblingIndex()];
    }

 
    public void SelectButton()
    {
        foreach (Transform sS in transform.parent)
        {
            shopScript shop= sS.GetComponent<shopScript>();
            shop.DeactiveScene();
        }
        if (isUnlocked)
        {
            transform.GetChild(1).gameObject.SetActive(true);
            GameManager.Instance.playerCharNo = transform.GetSiblingIndex();
            PlayerPrefs.SetInt("charno", transform.GetSiblingIndex());
        }
        GameManager.Instance.ChangeCharacter();
    }

    public void DeactiveScene()
    {
        transform.GetChild(1).gameObject.SetActive(false);
    }
    public void BuyButton()
    {
        isUnlocked = true;
        PlayerPrefs.SetInt(transform.name, 1);
        transform.GetChild(0).gameObject.SetActive(true);
        transform.GetChild(1).gameObject.SetActive(true);
        transform.GetChild(2).gameObject.SetActive(true);
        SelectButton();
    }
}
