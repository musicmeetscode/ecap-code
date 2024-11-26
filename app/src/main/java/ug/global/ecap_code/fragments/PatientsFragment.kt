package ug.global.ecap_code.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import ug.global.ecap_code.ActivityQuiz
import ug.global.ecap_code.DataActivity
import ug.global.ecap_code.adapters.PatientListAdapter
import ug.global.ecap_code.database.PatientHolder
import ug.global.ecap_code.databinding.FragmentNewUserBinding
import ug.global.ecap_code.databinding.FragmentPatientsBinding

class PatientsFragment : Fragment() {
    val binding by lazy { FragmentPatientsBinding.inflate(layoutInflater) }
    val binding_new by lazy { FragmentNewUserBinding.inflate(layoutInflater) }
    var patients = ArrayList<PatientHolder>()
    val adapter by lazy { PatientListAdapter(patients, requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = if (!PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("not_quizzed", true)) {
        binding.root
    } else {
        binding_new.root
    }
    // Inflate the

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding_new.quizBUtton.setOnClickListener {
            startActivity(Intent(requireContext(), ActivityQuiz::class.java))
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        (0 until 8).forEach {
            patients.add(PatientHolder("Male - 24 years", "#SJHAPA23", "14th July 2020", 2))
        }
        adapter.notifyDataSetChanged()
        binding.floatingActionButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        startActivity(Intent(requireContext(), DataActivity::class.java))
    }
}