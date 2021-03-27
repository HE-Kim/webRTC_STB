package com.example.callapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*


interface CometChatFriendsService {
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    // @POST("/{value}?id={value_id}&passwd=user1234!&role=50&name=김하은100&contact1=010&contact2=3333&contact3=4444")
    @POST("/api/getPairingNum?")
    fun addFriend(
        @Header("apikey") apiKey: String,
        @Header("appid") appID: String,
        @Body params: HashMap<String, List<String>>,
        // @Path("value") value: String,
        @Query("mac") mac: String

    ): Call<Data>
}
class AddActivity : AppCompatActivity() {

    var firebaseRef = Firebase.database.getReference("pairing")
    var username =""
    var pid_str=""
    var jsonObject = JSONObject()
    var mac=""
    var pnum=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        username = intent.getStringExtra("username")!!

        val text: TextView = findViewById(R.id.text1)

        VerifyBtn.setOnClickListener {
          //pnum=
              send()
            //text.setText("$pnum")
/*
            val random = Random()
            var pid = random.nextInt(9999)

           // var pid_str: String?


            if (pid < 10) {
                text.setText("000$pid")
                pid_str = "000$pid"
            } else if (pid < 100) {
                text.setText("00$pid")
                pid_str = "00$pid"
            } else if (pid < 1000) {
                text.setText("0$pid")
                pid_str = "0$pid"
            } else {
                text.setText("$pid")
                pid_str = "$pid"
            }

           // firebaseRef.child(username).child("Pairing").setValue(pid_str)
           // firebaseRef.child(username).child("user").setValue("none")

            firebaseRef.child(pid_str).child("STB").setValue(username)
            firebaseRef.child(pid_str).child("user").setValue("none")
            firebaseRef.child(pid_str).child("success").setValue("false")

            success_pair()*/
        }


    }
    private fun send() {


        //   (SSL_Activity.mContext as SSL_Activity).ssl_raw()
        val cf = CertificateFactory.getInstance("X.509")
        val caInput: InputStream = resources.openRawResource(R.raw.server)
        var ca: Certificate? = null
        try {
            ca = cf.generateCertificate(caInput)
            println("ca=" + (ca as X509Certificate?)!!.subjectDN)
        } catch (e: CertificateException) {
            e.printStackTrace()
        } finally {
            caInput.close()
        }
        val keyStoreType = KeyStore.getDefaultType()
        var keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        if (ca == null) {

        }
        keyStore.setCertificateEntry("ca", ca)



        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)


        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + Arrays.toString(
                trustManagers
            )

        }
        val hostnameVerifier = HostnameVerifier { _, session ->
            HttpsURLConnection.getDefaultHostnameVerifier().run {
                verify("https://13.125.233.161:6443", session)
            }
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory = sslContext.socketFactory
        val client1: OkHttpClient.Builder = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)

        client1.hostnameVerifier(HostnameVerifier { hostname, session -> true })


        val retrofit = Retrofit.Builder()
            .baseUrl("https://13.125.233.161:6443")
            .client(client1.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        //retrofit 객체를 통해 인터페이스 생성
        val service = retrofit.create(CometChatFriendsService::class.java)

        val body = HashMap<String, List<String>>()


        val value="/api/getPairingNum"//"userReg?id=$userID&passwd=$userpw&role=50&name=$username&contact1=$PhoneNum1&contact2=$PhoneNum2&contact3=$PhoneNum3"

        val userpw=userpw
        val userid=username

        mac="33:33:33:33"

        var log_string=""



        val apiKey = "12"
        val appID = "123"
        service.addFriend(apiKey, appID, body, mac)?.enqueue(object : Callback<Data> {
            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.d(
                    "CometChatAPI::", "Failed API call with call: " + call +
                            " + exception: " + t
                )
            }

            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                Log.d("Response:: ", response.body().toString())
                val data: Data? = response.body()
                pnum=response.body()?.pNum.toString()
                println(pnum)
                val text: TextView = findViewById(R.id.text1)
                if(pnum!="")
                    text.setText("$pnum")

            }
        })
        //return pnum

    }

    private fun success() {
        Toast.makeText(this, "등록 완료", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CallActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)

    }
    private fun success_pair() {
        firebaseRef.child(pid_str).child("success").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var value: String
                value = snapshot.value as String
                if (value == "true") {
                    success()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value.")
            }
        })

    }




}