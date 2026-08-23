package com.example.geoquiz
import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val quizViewModel: QuizViewModel by viewModels()
    private var isCheater = false
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView



    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        val cheatButton: Button = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    isCheater =
                        result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
                }
            }

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            resultLauncher.launch(intent)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            isCheater = false
            updateQuestion()

        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            isCheater = false
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion() {
        questionTextView.setText(quizViewModel.currentQuestionText)
    }

    private fun checkAnswer(userAnswer: Boolean) {

        if (isCheater) {
            Toast.makeText(
                this,
                R.string.judgment_toast,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId =
            if (userAnswer == correctAnswer) {
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()
    }
}