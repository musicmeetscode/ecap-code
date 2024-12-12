package ug.global.ecap_code.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.jsibbold.zoomage.ZoomageView
import ug.global.ecap_code.R

class ReadingAdapter(var context: Context, var items: ArrayList<String>) : Adapter<ReadingAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var counter: TextView = itemView.findViewById(R.id.page)
        val page: ZoomageView = itemView.findViewById(R.id.pdf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_reading_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.counter.text = context.getString(R.string.page_1_of_12, position + 1, items.size)

    }
}