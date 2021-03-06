package sdr.tecqza.homedelivery_deliveryboy.ui

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import retrofit2.Call
import retrofit2.Callback
import sdr.tecqza.homedelivery_deliveryboy.R
import sdr.tecqza.homedelivery_deliveryboy.api.RiderService
import sdr.tecqza.homedelivery_deliveryboy.api.Service
import sdr.tecqza.homedelivery_deliveryboy.model.Check
import sdr.tecqza.homedelivery_deliveryboy.model.CheckUpdate
import technited.minds.androidutils.MD

class Splash : AppCompatActivity() {
    private lateinit var runnable: Runnable
    private lateinit var handler1: Handler
    private lateinit var version: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handler1 = Handler()
        runnable = Runnable {
            val alertBuilder = AlertDialog.Builder(this@Splash)
            alertBuilder.setTitle("Connection")
            alertBuilder.setMessage("Internet connection not available")
            alertBuilder.show()
        }
        handler1.postDelayed(runnable, 3500)
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    val handler = Handler()
                    handler.postDelayed(
                        {
                            handler1.removeCallbacks(runnable)
                            checkUpdate()
                        },
                        3000
                    )
                }
            }
        )

        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            version = pInfo.versionName
            Log.d("asa", "App Version: $version")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("asa", "Error App Version: ${e.printStackTrace()}")
        }
    }


    private fun checkUpdate() {
        val checkUserCall: Call<CheckUpdate> = RiderService.create().checkUpdate()
        checkUserCall.enqueue(object : Callback<CheckUpdate?> {
            override fun onResponse(
                call: Call<CheckUpdate?>,
                response: retrofit2.Response<CheckUpdate?>
            ) {
                if (response.isSuccessful) {
                    val check = response.body()
                    if (check != null) {
                        if (!check.appVer.equals(version)) {
                            MD.update(
                                this@Splash,
                                "Update",
                                "You need to update your app to latest version",
                                "Update",
                                "http://play.google.com/store/apps/details?id=$packageName"
                            )
                        } else {
                            startActivity(Intent(this@Splash, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CheckUpdate?>, t: Throwable) {
                MaterialDialog(this@Splash).show {
                    title(text = "API ERROR")
                    message(text = "Update Checking failed")
                    cornerRadius(16f)
                    positiveButton(text = "Yes") { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        })
    }
}
