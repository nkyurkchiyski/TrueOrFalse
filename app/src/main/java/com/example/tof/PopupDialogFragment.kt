package com.example.tof

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class PopupDialogFragment(
    private val imageRes: Int,
    private val score: Int,
    private val successConsumer: () -> Unit,
    private val errorConsumer: () -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_popup, container)
        view.findViewById<ImageView>(R.id.popupImg).setImageResource(imageRes);
        view.findViewById<Button>(R.id.closeBtn).setOnClickListener {
            dismiss()
            errorConsumer.invoke()
        }

        view.findViewById<Button>(R.id.okBtn).setOnClickListener {
            dismiss()
            successConsumer.invoke()
        }
        view.findViewById<TextView>(R.id.finalScoreText).text =
            String.format(Constants.FINAL_SCORE_FORMAT, score);
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    companion object {
        const val TAG = "PopupDialog"
    }
}