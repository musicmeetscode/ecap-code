package ug.global.ecap_code.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ug.global.ecap_code.ActivityQuiz
import ug.global.ecap_code.database.EcapDatabase
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
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit().remove("not_trained").apply()
            requireContext().startActivity(Intent(requireContext(), ActivityQuiz::class.java))
        }
        lifecycleScope.launch(IO) {
            val quizzes = EcapDatabase.getInstance(requireContext()).ecapDao().getQuiz()
            val preAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
            val postAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
            quizzes.forEachIndexed { index, it ->
                val pass = it.getPassString()
                val score = it.calculateScores()
                preAdapter.add(
                    "Question ${index.plus(1)}: ${score.first}% - ${pass.first}"
                )
                postAdapter.add(
                    "Question ${index.plus(1)}: ${score.second}% - ${pass.second}"
                )
            }
            MainScope().launch {
                binding.listView.adapter = preAdapter
                binding.listView2.adapter = postAdapter
            }
        }


    }
}