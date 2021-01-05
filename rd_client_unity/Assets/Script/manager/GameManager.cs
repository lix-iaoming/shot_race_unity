using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
public class GameManager : MonoBehaviour
{
    public static GameManager Instance;
    public GameObject _PlatFormSpawn;
    public Animator cameraAnim;
    public Transform finalParent;
    int finalCount = 1;
    public List<Mesh> ai_characters;
    public bool startGame = false;
    [Header("AI")]
    public aiScript[] ai = new aiScript[3];
    public PlayerScript player;
    public int playerCharNo = 0;
    [HideInInspector]
    public List<Mesh> dummyMesh = new List<Mesh>();
    [Header("materials")]
    public Material groundMat;
    public Material downMat,water;
    public Texture[] groundTextures;
    public Texture[] downTextures;
    public Texture[] waterTextures;
    [HideInInspector]
    public int matNo = 0;
    int changeBgNo = 0;
     
    private void Awake()
    {
        if (!Instance)
            Instance = this;
       
    }
    void Start()
    {
        changeBgNo = PlayerPrefs.GetInt("bg", 0);
        matNo = PlayerPrefs.GetInt("matno", 0);
        if (changeBgNo<5)
        {
            changeBgNo++;
            PlayerPrefs.SetInt("bg", changeBgNo);
        }
        else
        {
            if (matNo < 5)
            {
                matNo++;
                PlayerPrefs.SetInt("matno", matNo);
            }
            else
            {
                matNo = 0;
                PlayerPrefs.SetInt("matno", matNo);
            }

            changeBgNo = 0;
            PlayerPrefs.SetInt("bg", changeBgNo);

        }

        groundMat.SetTexture("_EmissionMap", groundTextures[matNo]);
        groundMat.SetTexture("_MainTex", groundTextures[matNo]);
        downMat.SetTexture("_EmissionMap", downTextures[matNo]);
        downMat.SetTexture("_MainTex", downTextures[matNo]);
        water.SetTexture("_EmissionMap", waterTextures[matNo]);
        water.SetTexture("_MainTex", waterTextures[matNo]);
        foreach (Transform parts in finalParent)
        {
            Color c = Random.ColorHSV(0f, 1f, 1f, 1f, 0.5f, 1f);
            parts.GetChild(0).GetComponent<MeshRenderer>().material.color = c;
            parts.name = finalCount.ToString();
            parts.GetChild(1).GetChild(0).GetComponent<TextMeshProUGUI>().text = finalCount+"X";
            finalCount++;
        }
        player = FindObjectOfType<PlayerScript>();
        ai[0] = GameObject.Find("Ai").GetComponent<aiScript>();
        ai[1] = GameObject.Find("Ai_1").GetComponent<aiScript>();
        ai[2] = GameObject.Find("Ai_2").GetComponent<aiScript>();
        ChangeCharacter();
    }

    public void ChangeCharacter()
    {
        playerCharNo = PlayerPrefs.GetInt("charno", 0);
        dummyMesh.Clear();
        foreach (Mesh charMesh in ai_characters)
        {
            dummyMesh.Add(charMesh);
        }
        SkinnedMeshRenderer playerSkin = player.playerAnim.transform.GetChild(1).GetComponent<SkinnedMeshRenderer>();
        playerSkin.sharedMesh = dummyMesh[playerCharNo];
        dummyMesh.Remove(dummyMesh[playerCharNo]);
        foreach (aiScript aS in ai)
        {
            SkinnedMeshRenderer skins = aS.playerAnim.transform.GetChild(1).GetComponent<SkinnedMeshRenderer>();
            skins.sharedMesh = changeSkins();
        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public Mesh changeSkins()
    {
        int random = Random.Range(0, dummyMesh.Count);
        Mesh m = dummyMesh[random];
        dummyMesh.Remove(dummyMesh[random]);
        return m;
    }
    public IEnumerator SpawnLastBrick()
    {
        foreach (Transform parts in finalParent)
        {
            yield return new WaitForSeconds(.2f);
            parts.gameObject.SetActive(true);
        }

            
    }
}
