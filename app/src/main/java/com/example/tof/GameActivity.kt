package com.example.tof

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tof.base.model.Question
import com.example.tof.base.provider.IQuestionProvider
import com.example.tof.base.provider.QuestionProvider
import kotlin.math.roundToInt

class GameActivity : AppCompatActivity() {
    private var provider: IQuestionProvider? = null
    private var currentQuestion: Question? = null
    private var currentScore: Int = 0
    private var bestScore: Int = 0
    private val timer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            updateProgressBar(millisUntilFinished)
        }

        override fun onFinish() {
            updateProgressBar(0)
            PopupDialogFragment(
                R.drawable.time,
                currentScore,
                { loadQuestion() },
                { navigateToHome() }).show(
                supportFragmentManager,
                PopupDialogFragment.TAG
            )
            configureButtons(false)
            setScore(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        provider = QuestionProvider(
            this,
            { loadQuestion() },
            { Toast.makeText(this, Constants.SERVICE_NOT_AVAILABLE, Toast.LENGTH_LONG).show(); })
        loadData()
    }

    private fun loadData() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        bestScore = sharedPref.getInt(getString(R.string.pb), 0)
        (findViewById<TextView>(R.id.pbScoreText)).text =
            String.format(Constants.PERSONAL_BEST_FORMAT, bestScore)
    }


    fun trueSelected(view: View) {
        determineAnswer(true)
    }

    fun falseSelected(view: View) {
        determineAnswer(false)
    }

    private fun determineAnswer(answer: Boolean) {
        if (currentQuestion != null) {
            if (answer == currentQuestion!!.answer) {
                currentScore++
                showPopUp()
                updateScore()
                loadQuestion()
            } else {
                PopupDialogFragment(
                    R.drawable.wrong,
                    currentScore,
                    { loadQuestion() },
                    { navigateToHome() }).show(
                    supportFragmentManager,
                    PopupDialogFragment.TAG
                )
                timer.cancel()
                configureButtons(false)
                if (currentScore == bestScore) {
                    saveData()
                }
                setScore(0)
            }
        }
    }

    private fun loadQuestion() {
        currentQuestion = provider?.get()
        (findViewById<TextView>(R.id.categoryText)).text = currentQuestion?.category
        (findViewById<TextView>(R.id.questionText)).text = currentQuestion?.question

        configureButtons(true)
        timer.cancel()
        timer.start()
    }

    private fun updateScore() {
        setScore(currentScore)
    }

    private fun setScore(newScore: Int) {
        currentScore = newScore
        (findViewById<TextView>(R.id.currentScoreText)).text =
            String.format(Constants.CURRENT_SCORE_FORMAT, currentScore)
        if (currentScore > bestScore) {
            bestScore = currentScore
            (findViewById<TextView>(R.id.pbScoreText)).text =
                String.format(Constants.PERSONAL_BEST_FORMAT, bestScore)
        }
    }


    private fun saveData() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(getString(R.string.pb), bestScore)
            apply()
        }
    }

    private fun showPopUp() {
        val imageView: ImageView =
            if (currentScore > bestScore) (findViewById(R.id.pbPopUp)) else (findViewById(R.id.correctPopUp))
        imageView.alpha = 1f
        imageView.animate().alpha(0f).duration = 1200
    }

    private fun updateProgressBar(secondsLeft: Long) {
        val seconds = secondsLeft.toDouble() / Constants.TIME_LIMIT_MILLISECONDS
        val progress = seconds * 100
        findViewById<ProgressBar>(R.id.progressBar).progress = progress.toInt()
        findViewById<TextView>(R.id.progressBarSeconds).text =
            (seconds * 10).roundToInt().toString()
    }

    private fun configureButtons(enable: Boolean) {
        enableButton(findViewById(R.id.trueBtn), enable)
        enableButton(findViewById(R.id.falseBtn), enable)
    }

    private fun enableButton(button: Button, enable: Boolean) {
        button.isEnabled = enable
        button.isClickable = enable
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}