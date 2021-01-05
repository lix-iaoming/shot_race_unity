using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class levelloader : MonoBehaviour
{
    public int levelno;
    void Start()
    {
        levelno = PlayerPrefs.GetInt("level", 1);
        SceneManager.LoadScene(levelno);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
