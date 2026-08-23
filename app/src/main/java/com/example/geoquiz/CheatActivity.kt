package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_ANSWER_IS_TRUE = "geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "geoquiz.answer_shown"
private const val KEY_ANSWER_SHOWN = "answer_shown"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false
    private var isAnswerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        val answerTextView: TextView = findViewById(R.id.answer_text_view)
        val showAnswerButton: Button = findViewById(R.id.show_answer_button)

        // Restore cheating state after rotation
        isAnswerShown =
            savedInstanceState?.getBoolean(KEY_ANSWER_SHOWN, false) ?: false

        // If the user already revealed the answer before rotation,
        // show it again and keep the cheating result
        if (isAnswerShown) {
            val answerText =
                if (answerIsTrue) {
                    R.string.true_text
                } else {
                    R.string.false_text
                }

            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }

        showAnswerButton.setOnClickListener {
            val answerText =
                if (answerIsTrue) {
                    R.string.true_text
                } else {
                    R.string.false_text
                }

            answerTextView.setText(answerText)

            isAnswerShown = true
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }

        setResult(Activity.RESULT_OK, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(KEY_ANSWER_SHOWN, isAnswerShown)
    }

    companion object {
        fun newIntent(
            packageContext: Context,
            answerIsTrue: Boolean
        ): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}