package ug.global.ecap_code.fragments.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ug.global.ecap_code.database.VisitInfo
import ug.global.ecap_code.databinding.FormVitalsFormBinding
import ug.global.ecap_code.util.PatientDataCallBacks

class VitalsFragment(private var callbacks: PatientDataCallBacks) : Fragment() {
    val binding by lazy { FormVitalsFormBinding.inflate(layoutInflater) }
    var visit = VisitInfo()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.visitInfo = visit
        arrayOf(
            "triage" to binding.triage,
            "attendance" to binding.attendance,
            "specialprogram" to binding.specialProgram,
            "encounterclass" to binding.encounterclass,
            "admissiontype" to binding.admissionType,
            "encoutertype" to binding.encouterTYpe,
            "visitstatus" to binding.visitStatus,
            "modeofarrival" to binding.modeOfArrival,
            "levelofcare" to binding.levelofcare
        ).forEach {
            callbacks.getFillerData(it.first, it.second)
        }
        binding.button.setOnClickListener {
            callbacks.vitalsComplete()
        }
    }
}
