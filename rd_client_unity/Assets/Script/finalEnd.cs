using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class finalEnd : MonoBehaviour
{
    // Start is called before the first frame update
    bool isReached = false;
    public GameObject finalcircle;
    PlayerScript pS;
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    private void OnTriggerEnter(Collider other)
    {
            if(other.tag=="Player")
            {
                pS = other.GetComponentInParent<PlayerScript>();
                if (!isReached)
                {
                    finalcircle.SetActive(true);
                    isReached = true;
                    print("Reached!");
                    pS.endMethod();
                }
                else
                {
                    pS.failMethod();
                }
               
                
            }

          
        if (other.tag == "ai")
        {
            isReached = true;
            aiScript ai = other.GetComponentInParent<aiScript>();
            ai.failMethod();
        }

    }
}
