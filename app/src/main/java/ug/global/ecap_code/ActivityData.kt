package ug.global.ecap_code

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ug.global.ecap_code.database.EcapDatabase
import ug.global.ecap_code.database.FillerData
import ug.global.ecap_code.databinding.FragmentDataBinding
import ug.global.ecap_code.fragments.data.AssessmentFragment
import ug.global.ecap_code.fragments.data.NewPatientFragment
import ug.global.ecap_code.fragments.data.VitalsFragment
import ug.global.ecap_code.util.PatientDataCallBacks

class DataActivity : AppCompatActivity(), PatientDataCallBacks {
    val binding by lazy { FragmentDataBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        onBackPressedDispatcher.addCallback {
            if (binding.viewPager.currentItem == 0) {
                val alert = MaterialAlertDialogBuilder(this@DataActivity).create()
                alert.setIcon(
                    ContextCompat.getDrawable(
                        this@DataActivity,
                        R.drawable.noun_none_6212303
                    )
                )
                alert.setTitle(getString(R.string.warning_form_closing))
                alert.setMessage(getString(R.string.are_you_sure_you_want_to_exit_this_form_you_will_lose_all_and_any_data_that_you_had_already_filled))
                alert.setButton(BUTTON_POSITIVE, getString(R.string.no_stay_on_page)) { _, _ -> }
                alert.setButton(BUTTON_NEGATIVE, getString(R.string.yes_leave)) { _, _ ->
                    finish()
                }
                alert.show()
            } else {
                binding.viewPager.currentItem -= 1
            }

        }
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = PagerAdapter(this@DataActivity, this)
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> supportActionBar?.title = "Patient information"
                    1 -> supportActionBar?.title = "Patient vitals"
                    2 -> supportActionBar?.title = "Dementia Assessment"
                }
            }
        })
    }

    override fun infoComplete() {
        binding.viewPager.currentItem = 1
    }

    override fun vitalsComplete() {
        binding.viewPager.currentItem = 2
    }

    override fun assessmentComplete() {
        TODO("Not yet implemented")
    }

    override fun getFillerData(type: String, view: AutoCompleteTextView) {
        val items = ArrayList<FillerData>()
        lifecycleScope.launch(IO) {
            val inner = EcapDatabase.getInstance(this@DataActivity).ecapDao().getFillerData(type)
            items.addAll(inner)
            MainScope().launch {
                val adapter = ArrayAdapter(this@DataActivity, android.R.layout.simple_list_item_1, items)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                view.setAdapter(adapter)
                adapter.notifyDataSetChanged()
            }
        }
    }

}

class PagerAdapter(fa: FragmentActivity, private var patientCallBacks: PatientDataCallBacks) :
    FragmentStateAdapter(fa) {
    override fun getItemCount() = 3


    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment = NewPatientFragment(patientCallBacks)
        when (position) {
            0 -> fragment = NewPatientFragment(patientCallBacks)
            1 -> fragment = VitalsFragment(patientCallBacks)
            2 -> fragment = AssessmentFragment(patientCallBacks)
        }
        return fragment
    }
}

