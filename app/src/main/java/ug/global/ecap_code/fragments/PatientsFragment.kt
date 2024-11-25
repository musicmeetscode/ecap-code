package ug.global.ecap_code.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ug.global.ecap_code.DataActivity
import ug.global.ecap_code.databinding.FragmentPatientsBinding

class PatientsFragment : Fragment() {
    val binding by lazy { FragmentPatientsBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root
    // Inflate the

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        startActivity(Intent(requireContext(), DataActivity::class.java))
    }
}