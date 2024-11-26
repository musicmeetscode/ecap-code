package ug.global.ecap_code.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ug.global.ecap_code.ActivityQuiz
import ug.global.ecap_code.databinding.FragmentTrainingBinding


class TrainingFragment : Fragment() {
    val binding by lazy { FragmentTrainingBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quizBUtton.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), ActivityQuiz::class.java))
        }
    }
}