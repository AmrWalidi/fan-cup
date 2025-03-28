package com.amrwalidi.android.fancup.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.ui.fragment.QuestionFragment

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val questionId = intent?.getLongExtra("QUESTION_ID", -1L)
        val questionList = intent.getParcelableArrayListExtra<Question>("QUESTION_LIST")

        val questionFragment = QuestionFragment()

        val bundle = Bundle()
        questionId?.let { bundle.putLong("QUESTION_ID", it) }
        questionList.let { bundle.putParcelableArrayList("QUESTION_LIST", it) }
        questionFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.game_container, questionFragment)
            .commit()
    }
}