package ug.global.ecap_code.fragments.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ug.global.ecap_code.database.Patient
import ug.global.ecap_code.databinding.FormPatientFormBinding
import ug.global.ecap_code.util.PatientDataCallBacks

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
