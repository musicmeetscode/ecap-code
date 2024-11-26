package ug.global.ecap_code.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ug.global.ecap_code.R
import ug.global.ecap_code.database.PatientHolder
import ug.global.ecap_code.databinding.AdapterPatientInfoBinding

class PatientListAdapter(private var patients: ArrayList<PatientHolder>, var context: Context) :
    RecyclerView.Adapter<PatientListAdapter.ChatsViewHolder>() {
    class ChatsViewHolder(var binding: AdapterPatientInfoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_patient_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.binding.patient = patients[position]
    }

    override fun getItemCount() = patients.size

}

