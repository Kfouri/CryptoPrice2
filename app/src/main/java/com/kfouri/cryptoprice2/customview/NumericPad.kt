package com.kfouri.cryptoprice2.customview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.NumericPadLayoutBinding

class NumericPad(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    private val binding = NumericPadLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val extraDrawableAttr: Int
    private val showExtraButtonAttr: Boolean

    init {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.NumericPad)

        extraDrawableAttr = attributes.getResourceId(
            R.styleable.NumericPad_extraButtonDrawable,
            R.drawable.ic_fingerprint
        )

        showExtraButtonAttr = attributes.getBoolean(
            R.styleable.NumericPad_showExtraButton,
            false
        )

        binding.buttonExtra.visibility = if (showExtraButtonAttr) View.VISIBLE else View.GONE
        binding.buttonExtra.setImageDrawable(AppCompatResources.getDrawable(context, extraDrawableAttr))
    }

    fun numericPad(onNumberClick: ((value: Int)-> Unit),
                   onRemoveClick: (() -> Unit),
                   onExtraClick: (() -> Unit)) {

        binding.buttonOne.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonTwo.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonThree.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonFour.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonFive.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonSix.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonSeven.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonEight.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonNine.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonZero.setOnClickListener {
            onNumberClick.invoke((it as Button).text.toString().toInt())
            vibrate()
        }

        binding.buttonRemove.setOnClickListener {
            onRemoveClick.invoke()
            vibrate()
        }

        binding.buttonExtra.setOnClickListener {
            onExtraClick.invoke()
            vibrate()
        }
    }

    private fun vibrate() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}