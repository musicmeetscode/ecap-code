package ug.global.ecap_code.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ug.global.ecap_code.DataActivity
import ug.global.ecap_code.adapters.PatientListAdapter
import ug.global.ecap_code.database.EcapDatabase
import ug.global.ecap_code.database.Patient
import ug.global.ecap_code.database.PatientHolder
import ug.global.ecap_code.databinding.FragmentPatientsBinding
import ug.global.ecap_code.util.PatientCallBacksFinish
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PatientsFragment : Fragment(), PatientCallBacksFinish {
    val binding by lazy { FragmentPatientsBinding.inflate(layoutInflater) }
    private var patients = ArrayList<PatientHolder>()
    private val adapter by lazy { PatientListAdapter(patients, requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    // Inflate the

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(IO) {
            EcapDatabase.getInstance(requireContext()).ecapDao().getAllPatients().forEach {
                newPatientAdded(it)
            }
        }
        adapter.notifyDataSetChanged()
        binding.floatingActionButton.setOnClickListener {
            showDialog()
        }
    }


    private fun showDialog() {
        startActivity(Intent(requireContext(), DataActivity::class.java))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun newPatientAdded(patient: Patient) {
        patients.add(
            PatientHolder(
                "${patient.gender} - ${patient.age} years",
                "#SJHAPA${patient.id}",
                SimpleDateFormat("y-m-dd", Locale.getDefault()).format(Date(patient.timeStamp)),
                patient.id
            )
        )
        MainScope().launch {
            adapter.notifyDataSetChanged()
        }

    }
}