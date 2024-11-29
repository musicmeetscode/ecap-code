package ug.global.ecap_code.fragments

import android.annotation.SuppressLint
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
import ug.global.ecap_code.adapters.FeedbackAdapter
import ug.global.ecap_code.database.EcapDatabase
import ug.global.ecap_code.database.FeedbackItem
import ug.global.ecap_code.databinding.FragmentFeedbackBinding


class FeedbackFragment : Fragment() {
    val binding by lazy { FragmentFeedbackBinding.inflate(layoutInflater) }
    private var feedbacks = ArrayList<FeedbackItem>()
    private val adapter by lazy { FeedbackAdapter(feedbacks, requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(IO) {
            EcapDatabase.getInstance(requireContext()).ecapDao().apply {
                getAllPatients().forEach {
                    val assessment = this.getPatientAssessment(this.getPatientVisit(it.id).id)
                    feedbacks.add(FeedbackItem(it.f_name, it.getHash(requireContext()), assessment.matchDifference()))
                }
                MainScope().launch {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}