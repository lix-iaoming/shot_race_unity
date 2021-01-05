using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using DG.Tweening;
public class PlayerScript : MonoBehaviour
{
    // Start is called before the first frame update
    public Rigidbody rb;
    public Animator playerAnim;
    public float speed = 5f, rotSpeed = 2f;
    bool  end = false, startGame;
    public Transform _CollectBrickPos;
    public float addValue;
    public GameObject brickPrefab;
    Transform prevPos;
    bool hasBrick = false;
    public Transform rayPos;
    public Transform[] bridgePos;
    int bridgeNo = 0;
    Transform spawnedBrick;
    bool inGround = false;
    bool spawnBrick = false;
    bool isReached = false;
    [HideInInspector]
    public Transform lastPos;
    public Transform cameraPos;
    [Header("Effects")]
    public ParticleSystem bridgeSpawnEffect;
    public ParticleSystem deadEffect;
    public ParticleSystem fireFx;
    bool isJumped = false;
    [Header("Characters")]
    public Mesh[] characters;
    public SkinnedMeshRenderer orginalSkin;
    float  speedCount = 0,brickCount;
    private GameManager gm;
    float timer = 0;
    void Start()
    {
        prevPos = _CollectBrickPos;
        gm = GameManager.Instance;
        lastPos = GameManager.Instance.finalParent.GetChild(0);
    }

    // Update is called once per frame
    private void Update()
    {
        Rotation();
         if(hasBrick&& _CollectBrickPos.childCount == 0)
         {
            hasBrick = false;
            playerAnim.SetBool("HasPlanks", false );
            playerAnim.SetFloat("LastDropDelay", 0f);
         }
        rayMethod();
    }

    void rayMethod()
    {
        RaycastHit hit;
        if(Physics.Raycast(rayPos.position,Vector3.down,out hit,1f))
        {
            if (inGround)
            {
                inGround = false;
                playerAnim.SetBool("InJump", false);
                playerAnim.SetBool("HasPlanks", false);
                playerAnim.SetFloat("LastDropDelay", 0f);
            }
            rb.useGravity = false;
            if (_CollectBrickPos.childCount > 1 )
            {
               // hasBrick = true;
                playerAnim.SetBool("HasPlanks", true);
                playerAnim.SetFloat("LastDropDelay", .3f);
            }
            brickCount = 0;
            if (speedCount > 0)
            {
                speedCount -= Time.deltaTime;
                speed = 15;
            }else
            {
                speedCount = 0;
                speed = Mathf.Lerp(speed, 10f, 1f * Time.deltaTime);
            }
        }
        else
        {
            if(_CollectBrickPos.childCount > 0)
            {
                rb.useGravity = false;
                playerAnim.SetFloat("LastDropDelay", 0f);
            }
            if (_CollectBrickPos.childCount!=0)
            SpawnBridge();
            else if(_CollectBrickPos.childCount==0&&!inGround)
            {
                inGround = true;
                rb.useGravity = true;
                rb.AddForce(Vector3.up * 5f, ForceMode.Impulse);
                playerAnim.SetBool("InJump", true);
                prevPos = _CollectBrickPos;
                AudioManager.instance.Play("jump");
                if (isReached)
                    isJumped = true;
            }
           
            Debug.DrawRay(rayPos.position, Vector3.down, Color.red);
        }
    }

    void SpawnBridge()
    {
        if(!spawnBrick)
        {
            spawnBrick = true;
            spawnedBrick = _CollectBrickPos.GetChild(_CollectBrickPos.childCount - 1);
            spawnedBrick.position = bridgePos[bridgeNo].position;
            bridgeSpawnEffect.transform.position = transform.position;
            bridgeSpawnEffect.Play();
            AudioManager.instance.Play("brick");
            spawnedBrick.SetParent(null);
            if (_CollectBrickPos.childCount != 0)
                prevPos = _CollectBrickPos.GetChild(_CollectBrickPos.childCount - 1);
            bridgeNo++;
            if (bridgeNo == 2)
            {
                bridgeNo = 0;
            }
            speed = Mathf.Lerp(speed, 15f, 4f * Time.deltaTime);
            Invoke("RespawnBrick", .03f);
            speedCount = 2f;
            if(brickCount>4)
            {
                if(brickCount==5)
                {
                    brickCount = 6;
                    fireFx.Play();
                    Invoke("playFire", .1f);
                    
                }
            }
            else
                brickCount++;

        }


    }

    void playFire()
    {
       
        brickCount = 5;
    }

    void RespawnBrick()
    {
        spawnBrick = false;
    }


    void FixedUpdate()
    {
        if(startGame&&!end)
        MoveCharacter();
    }

    void Rotation()
    {
        if(gm.startGame&&!startGame)
        {
            startGame = true;
            playerAnim.SetFloat("RunningSpeed", 10);
            GameManager.Instance.cameraAnim.SetTrigger("Go");
        }


        if (Input.GetMouseButton(0) && startGame && timer > .05f)
            transform.Rotate(0.0f, Input.GetAxis("Mouse X") * rotSpeed, 0.0f);
        else if(Input.GetMouseButton(0))
            timer += Time.deltaTime;

        if (Input.GetMouseButtonUp(0))
            timer = 0;
    }
    void MoveCharacter()
    {
        rb.MovePosition(transform.position + transform.forward * speed * Time.deltaTime);
    }

    void winMovement()
    {
        playerAnim.SetTrigger("Win");
        canvasManager.Instance.WinMethod();
    }
    public void endMethod()
    {
        isReached = true;
    }

    public void failMethod()
    {
        speed = 0;
        foreach (Transform Rbs in _CollectBrickPos)
        {
           Rigidbody rigid =  Rbs.GetComponent<Rigidbody>();
            rigid.isKinematic = false;
            rigid.AddForce(Vector3.forward * 10f, ForceMode.Impulse);
            rigid.AddForce(Vector3.up * 5, ForceMode.Impulse);
        }
        playerAnim.SetTrigger("idle");
        // Destroy(this);
        end = true;
        Invoke("failPanel", .4f);
    }

    void failPanel()
    {
        canvasManager.Instance.LoosePanel.SetActive(true);
    }

    private void OnTriggerEnter(Collider other)
    {
        if(other.tag=="brick")
        {
            AudioManager.instance.Play("collect");
            Vector3 Pos = new Vector3(prevPos.position.x, prevPos.position.y + addValue, prevPos.position.z);
            GameObject spawn = Instantiate(brickPrefab, Pos, Quaternion.identity);
            prevPos = spawn.transform;
            spawn.transform.SetParent(_CollectBrickPos);
            spawn.transform.localRotation = Quaternion.identity;
            other.transform.parent.parent.GetComponent<PlatformSpawner>().RespawnMethod();
        }

        if(other.tag=="dead")
        {
            end = true;
            if(isReached)
            {
              transform.DOMove(lastPos.position, .5f).OnComplete(winMovement);
            }else
            {
                print("GameOver!!");
                cameraPos.SetParent(null);
                deadEffect.transform.position = this.transform.position;
                deadEffect.Play();
                this.gameObject.SetActive(false);
                canvasManager.Instance.FailMethod();
                AudioManager.instance.Play("dead");
            }
           
        }
        if(other.tag=="final")
        {
            print("addPos!");
            lastPos = other.transform;
            AudioManager.instance.Play("final");
            string s = other.name;
            print(s);
            int roundNo = (int.Parse)(s);
            int coins =  roundNo * 50;
            canvasManager.Instance.noText.text = "X" + coins;
            coins = coins + canvasManager.Instance.coins;
            print(coins);
            PlayerPrefs.SetInt("coins", coins);
            if (isJumped)
            {
                end = true;
                rb.isKinematic = true;
                transform.DOMove(lastPos.position, .5f).OnComplete(winMovement);
            }
        }
        
    }
   
}
