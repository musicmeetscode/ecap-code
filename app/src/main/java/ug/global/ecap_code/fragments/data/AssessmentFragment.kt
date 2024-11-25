package ug.global.ecap_code.fragments.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ug.global.ecap_code.databinding.AssessmentFormBinding
import ug.global.ecap_code.util.PatientDataCallBacks

class AssessmentFragment(private var callbacks: PatientDataCallBacks) : Fragment() {
    val binding by lazy { AssessmentFormBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            callbacks.assessmentComplete()
        }
    }
}
