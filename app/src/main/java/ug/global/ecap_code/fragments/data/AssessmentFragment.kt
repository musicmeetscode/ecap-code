package ug.global.ecap_code.fragments.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ug.global.ecap_code.databinding.FormAssessmentFormBinding
import ug.global.ecap_code.util.PatientDataCallBacks

class AssessmentFragment(private var callbacks: PatientDataCallBacks) : Fragment() {
    val binding by lazy { FormAssessmentFormBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val views = arrayOf(
            binding.memoryGroup to binding.group,
            binding.activityGroup to binding.group2,
            binding.abruptGroup to binding.group3,
            binding.depressionGroup to binding.group4,
            binding.ageGroup to binding.group5,
        )
        views.forEach {
            it.first.setOnCheckedChangeListener { show ->
                val pos = views.indexOf(it)
                it.second.isVisible = show

                views.filterIndexed { index, _ -> index > pos }.forEach { remainder ->
                    remainder.second.isVisible = false
                }
            }
        }
        binding.button.setOnClickListener {
            callbacks.assessmentComplete()
        }
    }
}
