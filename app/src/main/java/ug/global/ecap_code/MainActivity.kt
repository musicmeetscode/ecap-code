package ug.global.ecap_code

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject
import ug.global.ecap_code.database.EcapDatabase
import ug.global.ecap_code.database.FillerData
import ug.global.ecap_code.databinding.MainActivityBinding
import ug.global.ecap_code.sheets.LoginBottomSheet
import ug.global.ecap_code.util.AppCallBacks

class MainActivity : AppCompatActivity(), AppCallBacks {
    val binding by lazy { MainActivityBinding.inflate(layoutInflater) }

    private val loginBottomSheet by lazy { LoginBottomSheet() }
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        if (PreferenceManager.getDefaultSharedPreferences(this).getString("token", "none") == "none") {
            loginBottomSheet.show(supportFragmentManager, "login_sheet", this, this@MainActivity)
            loginBottomSheet.isCancelable = false
        }
    }

    override fun loggedIn(json: JSONObject) {
        val keys = json.getJSONObject("data").keys()
        while (keys.hasNext()) {
            val key = keys.next()
            lifecycleScope.launch(IO) {
                val value = json.getJSONObject("data").getJSONArray(key)
                for (i in 0 until value.length()) {
                    val inner = value.getJSONObject(i)
                    Log.e("TAG", "onViewCreated: " + inner)
                    EcapDatabase.getInstance(this@MainActivity).ecapDao().addFillerData(
                        FillerData(
                            inner.getInt("id"),
                            inner.getString("name"),
                            key.lowercase(),
                            key.lowercase(),
                        )
                    )
                }
            }
        }
        loginBottomSheet.dismiss()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_account -> {
                val logout = MaterialAlertDialogBuilder(this).create()
                logout.setTitle("Logout")
                logout.setMessage(getString(R.string.logout_warning))
                logout.setButton(BUTTON_POSITIVE, getString(R.string.yes_logout)) { _, _ ->
                    PreferenceManager.getDefaultSharedPreferences(this).edit().remove("token").remove("user")
                        .apply()
                    lifecycleScope.launch(IO) {
                        EcapDatabase.getInstance(this@MainActivity).ecapDao().deleteFillers()
                    }
                    recreate()
                }
                logout.setButton(BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ -> }
                logout.show()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}