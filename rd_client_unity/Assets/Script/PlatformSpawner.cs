using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlatformSpawner : MonoBehaviour
{
    // Start is called before the first frame update
    GameObject spawnObj;
    void Start()
    {
        GameObject spawn = Instantiate(GameManager.Instance._PlatFormSpawn, transform.position, transform.rotation);
        spawnObj = spawn;
        spawn.transform.SetParent(this.transform);
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void RespawnMethod()
    {
        StartCoroutine(respawn());
    }

    IEnumerator respawn()
    {
        spawnObj.SetActive(false);
        yield return new WaitForSeconds(2f);
        spawnObj.SetActive(true);
    }
}
