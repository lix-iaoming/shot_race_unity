using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SliderScript : MonoBehaviour
{
    // [HideInInspector]
    public Image filler;
    public Slider mainSlider;
    public float platformDistance, Progress;
    public Transform PlatformA, PlatformB, Player;
    public bool isPlayer;
    private void Awake()
    {
     

    }
    private void Start()
    {

        PlatformA = GameObject.Find("Start").transform;
        PlatformB = GameObject.Find("End").transform;
        transform.name = Player.name;
        mainSlider.minValue = 0;
        mainSlider.maxValue = 100;
        mainSlider.wholeNumbers = true;
        mainSlider.wholeNumbers = false;

      
    }
    public void Update()
    {

        platformDistance = Vector3.Distance(PlatformA.position, PlatformB.position);
        Progress = Vector3.Distance(Player.position, PlatformA.position) / platformDistance;
        mainSlider.value = Progress * mainSlider.maxValue;
        filler.fillAmount = mainSlider.value / 100;




    }

}
