package ug.global.ecap_code

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ug.global.ecap_code.adapters.QuizAdapter
import ug.global.ecap_code.database.EcapDatabase
import ug.global.ecap_code.database.QuizItem
import ug.global.ecap_code.databinding.ActivityQuizBinding

class ActivityQuiz : AppCompatActivity() {
    val binding by lazy { ActivityQuizBinding.inflate(layoutInflater) }
    val quizzes = arrayListOf<QuizItem>()
    val quizAdapter by lazy { QuizAdapter(quizzes, this, lifecycleScope) }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Quiz time: 20min"
        binding.recyclerView.adapter = quizAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.closeQuiz.setOnClickListener {

            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("not_quizzed_", true)) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("not_quizzed", false).apply()
                finish()
                startActivity(Intent(this, ActivityMhGap::class.java))
            } else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("not_trained", true)) {
                finish()
                startActivity(Intent(this, DataActivity::class.java))
            }
        }
        lifecycleScope.launch(IO) {
            val quizzesInner = EcapDatabase.getInstance(this@ActivityQuiz).ecapDao().getQuiz()
            quizzes.addAll(quizzesInner)
            MainScope().launch {
                quizAdapter.notifyDataSetChanged()
            }
        }
    }
}