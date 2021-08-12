package sdr.tecqza.homedelivery_deliveryboy.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import sdr.tecqza.homedelivery_deliveryboy.R
import sdr.tecqza.homedelivery_deliveryboy.api.Service
import sdr.tecqza.homedelivery_deliveryboy.databinding.ActivityMainBinding
import sdr.tecqza.homedelivery_deliveryboy.model.Check
import technited.minds.androidutils.ProcessDialog
import technited.minds.androidutils.SharedPrefs

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userSharedPreferences: SharedPrefs
    private lateinit var navController: NavController
    private lateinit var processDialog: ProcessDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        processDialog = ProcessDialog(this)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navView.setOnNavigationItemSelectedListener(this)
        checkUser()
    }

    private fun checkUser() {
        val checkUserCall: Call<Check> = Service.create().check("ComplicatedQWERTYUIOP789")
        checkUserCall.enqueue(object : Callback<Check?> {
            override fun onResponse(
                call: Call<Check?>,
                response: retrofit2.Response<Check?>
            ) {
                if (response.isSuccessful) {
                    val check = response.body()
                    if (check?.browser.equals("true")) {

                    } else {
                        processDialog.show()
                    }
                }
            }

            override fun onFailure(call: Call<Check?>, t: Throwable) {
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.logout -> {
                MaterialDialog(this).show {
                    title(text = "Logout?")
                    message(text = "Are you sure want to logout ?")
                    cornerRadius(16f)
                    positiveButton(text = "Yes") { dialog ->
                        userSharedPreferences = SharedPrefs(this@MainActivity, "USER")
                        userSharedPreferences.clearAll()
                        dialog.dismiss()
                        finish()
                    }
                    negativeButton(text = "Cancel") { dialog ->
                        dialog.dismiss()
                    }
                }
            }

            R.id.navigation_home -> {
                navController.navigate(R.id.navigation_home)
            }

            R.id.navigation_order_history -> {
                navController.navigate(R.id.navigation_order_history)
            }

            R.id.navigation_holiday_history -> {
                navController.navigate(R.id.navigation_holiday_history)
            }

        }
        return true
    }

}
