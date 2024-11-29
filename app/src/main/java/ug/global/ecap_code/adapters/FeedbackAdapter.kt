package ug.global.ecap_code.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ug.global.ecap_code.R
import ug.global.ecap_code.database.FeedbackItem
import ug.global.ecap_code.databinding.AdapterFeedbackItemBinding

class FeedbackAdapter(private var feedbacks: ArrayList<FeedbackItem>, var context: Context) :
    RecyclerView.Adapter<FeedbackAdapter.ChatsViewHolder>() {
    class ChatsViewHolder(var binding: AdapterFeedbackItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_feedback_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.binding.feedback = feedbacks[position]
    }

    override fun getItemCount() = feedbacks.size

}

