package ug.global.ecap_code.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import ug.global.ecap_code.R
import ug.global.ecap_code.database.EcapDatabase
import ug.global.ecap_code.database.QuizItem
import ug.global.ecap_code.databinding.AdapterQuizItemBinding

class QuizAdapter(private var quizzes: ArrayList<QuizItem>, var context: Context, var scope: LifecycleCoroutineScope) :
    RecyclerView.Adapter<QuizAdapter.ChatsViewHolder>() {
    class ChatsViewHolder(var binding: AdapterQuizItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_quiz_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.binding.quiz = quiz
        holder.binding.ecapTextHeader2.setHeader(quiz.getQId())
        val checked = arrayListOf<String>()
        if (!quiz.isAnswerChecked) {
            try {
                var crr = "Incorrect"
                var color = Color.RED
//                if (quiz.checked.toInt()+1 == quiz.answer) {
//                    crr = "Correct"
//                    color = Color.GREEN
//                }
                holder.binding.answerText.text = context.getString(R.string.your_anser_was_1_d_2_s, quiz.checked.toInt() + 1, crr)
                holder.binding.answerText.setTextColor(color)
                holder.binding.customRadioGroup.setOnCheckedChangeListener(null)
                holder.binding.customRadioGroup.check(
                    arrayOf(
                        holder.binding.radioYes1.id,
                        holder.binding.radioYes2.id,
                        holder.binding.radioYes3.id,
                        holder.binding.radioYes4.id,
                    )[quiz.checked.toInt()]
                )
            } catch (_: Exception) {
            }
        } else {
            holder.binding.answerText.isVisible = false
            holder.binding.answerText.text = null
        }
        val ids = arrayOf(
            holder.binding.radioYes1,
            holder.binding.radioYes2,
            holder.binding.radioYes3,
            holder.binding.radioYes4,
            holder.binding.radioYes5,
            holder.binding.radioYes6,
            holder.binding.radioYes7,
        )
        ids.forEach { materialCheck ->
            materialCheck.setOnClickListener {
                if (materialCheck.isChecked) {
                    checked.add((ids.indexOf(materialCheck) + 1).toString())
                } else {
                    checked.remove((ids.indexOf(materialCheck) + 1).toString())
                }

                if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("not_trained", true)) {
                    quiz.checked = checked.toString()
                } else {
                    quiz.postChecked = checked.toString()
                }
                scope.launch(IO) {
                    EcapDatabase.getInstance(context).ecapDao().addQuiz(quiz)
                }
            }
        }

    }

    override fun getItemCount() = quizzes.size

}

