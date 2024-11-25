package ug.global.ecap_code.sheets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import ug.global.ecap_code.R
import ug.global.ecap_code.util.AppCallBacks
import ug.global.ecap_code.util.MySingleton
import ug.global.ecap_code.util.URLS

class LoginBottomSheet : BottomSheetDialogFragment() {
    private lateinit var callbacks: AppCallBacks
    private lateinit var contextParam: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_layout, container)
    }

    fun show(manager: FragmentManager, tag: String?, callbacks: AppCallBacks, contextParams: Context) {
        show(manager, tag)
        this.callbacks = callbacks
        this.contextParam = contextParams
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginUrl = URLS.LOGIN_URL
        view.findViewById<View>(R.id.signInButton).setOnClickListener {
            view.findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
            view.findViewById<View>(R.id.signInButton).isEnabled = false
            view.findViewById<View>(R.id.userName).isEnabled = false
            view.findViewById<View>(R.id.password).isEnabled = false
            try {
                val jsonObject = JSONObject()
                jsonObject.put("username", (view.findViewById<View>(R.id.userName) as TextInputEditText).editableText.toString().trim())
                jsonObject.put("password", (view.findViewById<View>(R.id.password) as TextInputEditText).editableText.toString().trim())
                val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, loginUrl, jsonObject, { outerResponse: JSONObject ->

                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
                        .putString("token", outerResponse.getString("token"))
                        .putString("user", outerResponse.getJSONObject("user").getString("first_name"))
                        .apply()

                    callbacks.loggedIn(outerResponse)

                }) { error: VolleyError ->
                    Log.e("TAG", "onViewCreated: ", error)
                    view.findViewById<View>(R.id.progressBar).visibility = View.GONE
                    view.findViewById<View>(R.id.signInButton).isEnabled = true
                    view.findViewById<View>(R.id.userName).isEnabled = true
                    view.findViewById<View>(R.id.password).isEnabled = true
                    try {
                        if (error.networkResponse.statusCode == 401) {
                            (view.findViewById<View>(R.id.password) as TextInputEditText).error = "Invalid credentials"
                            view.findViewById<View>(R.id.password).requestFocus()
                        } else {
                            Snackbar.make(
                                view.findViewById(R.id.main),
                                "An error occurred. Please try again later or consult support",
                                BaseTransientBottomBar.LENGTH_LONG
                            ).show()
                        }
                    } catch (ex: NullPointerException) {
                        Snackbar.make(
                            view.findViewById(R.id.main), "Unable to communicate with server. Please try again later or contact support",
                            BaseTransientBottomBar.LENGTH_LONG
                        ).show()
                    }
                }
                jsonObjectRequest.retryPolicy = DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                MySingleton.getInstance(contextParam)?.addToRequestQueue(jsonObjectRequest)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}