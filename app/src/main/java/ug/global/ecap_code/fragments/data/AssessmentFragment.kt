package ug.global.ecap_code.fragments.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ug.global.ecap_code.R
import ug.global.ecap_code.database.AssessmentForm
import ug.global.ecap_code.databinding.FormAssessmentFormBinding
import ug.global.ecap_code.util.PatientDataCallBacks

class AssessmentFragment(private var callbacks: PatientDataCallBacks) : Fragment() {
    val binding by lazy { FormAssessmentFormBinding.inflate(layoutInflater) }
    private val asses = AssessmentForm()
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
        val choices = resources.getStringArray(R.array.diagnosis)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, choices)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.diagnosis.setAdapter(adapter)
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
            asses.memoryProblems = binding.memoryGroup.defaultChecked
            asses.keyRoles = binding.activityGroup.defaultChecked
            asses.progressing = binding.progressingGroup.defaultChecked
            asses.anyOf = binding.abruptGroup.defaultChecked
            asses.depress = binding.depressionGroup.defaultChecked
            asses.otherAny = binding.ageGroup.defaultChecked
            asses.anaemia = binding.anaemiaGroup.defaultChecked
            asses.cardio = binding.cardioVascular.defaultChecked
            asses.careIncome = binding.incomeLoss.defaultChecked
            asses.careMood = binding.depressedMood.defaultChecked
            asses.caretaker = binding.strainCode.defaultChecked
            asses.behavior = binding.symptomsGroup.defaultChecked
            asses.hasDementia = binding.diagnosis.editableText.toString()
            asses.management = binding.management.editableText.toString()
            callbacks.assessmentComplete(asses)
        }
    }
}
