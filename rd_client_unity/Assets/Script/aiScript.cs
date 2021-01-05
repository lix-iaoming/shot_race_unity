using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using DG.Tweening;
public class aiScript : MonoBehaviour
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
    [Header("Effects")]
    public ParticleSystem bridgeSpawnEffect;
    public ParticleSystem deadEffect;
    public ParticleSystem fireFx;
    bool isJumped = false;
    [Header("Characters")]
    public SkinnedMeshRenderer orginalSkin;
    [Header("WayPoints")]
    public Transform pointsParent;
    public List<Transform> waypoints;
    float speedCount, brickCount;
    private GameManager gm;
   
    void Start()
    {
        prevPos = _CollectBrickPos;
        foreach (Transform point in pointsParent)
        {
            waypoints.Add(point);
        }
        gm = GameManager.Instance;
        transform.GetChild(7).gameObject.SetActive(false);
    }

    // Update is called once per frame
    private void Update()
    {
        Rotation();
        if (hasBrick && _CollectBrickPos.childCount == 0)
        {
            hasBrick = false;
            playerAnim.SetBool("HasPlanks", false);
            playerAnim.SetFloat("LastDropDelay", 0f);
        }
        rayMethod();
    }

    void rayMethod()
    {
        RaycastHit hit;
        if (Physics.Raycast(rayPos.position, Vector3.down, out hit, 1f))
        {
            if (inGround)
            {
                inGround = false;
                playerAnim.SetBool("InJump", false);
                playerAnim.SetBool("HasPlanks", false);
                playerAnim.SetFloat("LastDropDelay", 0f);
            }
            rb.useGravity = true;
            if (_CollectBrickPos.childCount > 1)
            {
                // hasBrick = true;
                playerAnim.SetBool("HasPlanks", true);
                playerAnim.SetFloat("LastDropDelay", .3f);
            }
            if (speedCount > 0)
            {
                speedCount -= Time.deltaTime;
                speed = 15;
            }
            else
            {
                speedCount = 0;
                speed = Mathf.Lerp(speed, 10f, 4f * Time.deltaTime);
            }

        }
        else
        {
            if (_CollectBrickPos.childCount > 0)
            {
                rb.useGravity = false;
                playerAnim.SetFloat("LastDropDelay", 0f);
            }
            if (_CollectBrickPos.childCount != 0)
                SpawnBridge();
            else if (_CollectBrickPos.childCount == 0 && !inGround)
            {
                inGround = true;
                rb.useGravity = true;
                rb.AddForce(Vector3.up * 5f, ForceMode.Impulse);
                playerAnim.SetBool("InJump", true);
                prevPos = _CollectBrickPos;

                if (isReached)
                    isJumped = true;


            }

            Debug.DrawRay(rayPos.position, Vector3.down, Color.red);
        }
    }
    public void failMethod()
    {
      
        foreach (Transform Rbs in _CollectBrickPos)
        {
            Rigidbody rigid = Rbs.GetComponent<Rigidbody>();
            rigid.isKinematic = false;
            rigid.AddForce(Vector3.forward * 10f, ForceMode.Impulse);
            rigid.AddForce(Vector3.up * 5, ForceMode.Impulse);
        }
        playerAnim.SetTrigger("idle");
        Destroy(this);
    }

    void failPanel()
    {
        canvasManager.Instance.LoosePanel.SetActive(true);
    }
    void SpawnBridge()
    {
        if (!spawnBrick)
        {
            spawnBrick = true;
            spawnedBrick = _CollectBrickPos.GetChild(_CollectBrickPos.childCount - 1);
            spawnedBrick.position = bridgePos[bridgeNo].position;
            bridgeSpawnEffect.transform.position = transform.position;
            bridgeSpawnEffect.Play();
            spawnedBrick.SetParent(null);
            if (_CollectBrickPos.childCount != 0)
                prevPos = _CollectBrickPos.GetChild(_CollectBrickPos.childCount - 1);
            bridgeNo++;
            if (bridgeNo == 2)
            {
                bridgeNo = 0;
            }
            speed = Mathf.Lerp(speed, 15f, 4f * Time.deltaTime);
            speedCount +=  Time.deltaTime;
            if (brickCount > 4)
            {
                if (brickCount == 5)
                {
                    brickCount = 6;
                    fireFx.Play();
                    Invoke("playFire", .1f);

                }
            }
            else
                brickCount++;
            Invoke("RespawnBrick", .03f);
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
        if (startGame && !end)
            MoveCharacter();
    }

    void Rotation()
    {
        if (gm.startGame&&!startGame)
        {
            startGame = true;
            playerAnim.SetFloat("RunningSpeed", 10);
        }


       
    }
    void MoveCharacter()
    {
        if(waypoints.Count>0)
        {
            rb.MovePosition(transform.position = Vector3.MoveTowards(transform.position, waypoints[0].position, speed * Time.deltaTime));
            float distance = Vector3.Distance(transform.position, waypoints[0].position);
            transform.LookAt(waypoints[0]);
            if (distance < .1f)
            {
                waypoints.Remove(waypoints[0]);
            }
        }else
        {
            print("I am Done!");
        }
       
    }

    void winMovement()
    {
        playerAnim.SetTrigger("Win");
    }
    public void endMethod()
    {
        isReached = true;
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "brick")
        {
            Vector3 Pos = new Vector3(prevPos.position.x, prevPos.position.y + addValue, prevPos.position.z);
            GameObject spawn = Instantiate(brickPrefab, Pos, Quaternion.identity);
            prevPos = spawn.transform;
            spawn.transform.SetParent(_CollectBrickPos);
            spawn.transform.localRotation = Quaternion.identity;
            other.transform.parent.parent.GetComponent<PlatformSpawner>().RespawnMethod();
        }

        if (other.tag == "dead")
        {
            transform.position = waypoints[0].position;

        }
      

    }

}
