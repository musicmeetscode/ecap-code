package ug.global.ecap_code.fragments.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import ug.global.ecap_code.database.Patient
import ug.global.ecap_code.databinding.FormPatientFormBinding
import ug.global.ecap_code.util.Helper
import ug.global.ecap_code.util.PatientDataCallBacks
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewPatientFragment(private var callbacks: PatientDataCallBacks) : Fragment() {
    val binding by lazy { FormPatientFormBinding.inflate(layoutInflater) }
    val patient = Patient()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.patient = patient


        binding.dateLayout.setStartIconOnClickListener {
            val materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker()
            materialDatePickerBuilder.setTitleText("Select patient date of birth")
            val datePicker = materialDatePickerBuilder.setCalendarConstraints(
                CalendarConstraints.Builder().setStart(System.currentTimeMillis() - (60 * 365.242 * 24 * 60 * 60 * 1000).toLong())
                    .setEnd(System.currentTimeMillis() - (18 * 365.242 * 24 * 60 * 60 * 1000).toLong()).build()
            ).build()

            datePicker.show(childFragmentManager, "date_picker")
            datePicker.addOnPositiveButtonClickListener { selection: Long ->
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
                binding.ageField.setText(Helper.calculateAgeWithSimpleDateFormat(date))
                binding.dobField.setText(date)
            }
        }
//        Fetch filler data
        arrayOf(
            "suffix" to binding.suffix,
            "prefix" to binding.prefix,
            "gender" to binding.sex,
            "bloodgroup" to binding.bloocGroup,
            "maritalstatus" to binding.maritalStatus,
            "country" to binding.nationality,
            "district" to binding.district,
            "religion" to binding.religion,
            "tribe" to binding.tribe
        ).forEach {
            callbacks.getFillerData(it.first, it.second)
        }
        binding.button.setOnClickListener {
            callbacks.infoComplete(patient)
        }
    }
}
