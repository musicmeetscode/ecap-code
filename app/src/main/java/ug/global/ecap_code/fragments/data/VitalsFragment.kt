package ug.global.ecap_code.fragments.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ug.global.ecap_code.databinding.VitalsFormBinding
import ug.global.ecap_code.util.PatientDataCallBacks

class VitalsFragment(private var callbacks: PatientDataCallBacks) : Fragment() {
    val binding by lazy { VitalsFormBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            callbacks.vitalsComplete()
        }
    }
}
