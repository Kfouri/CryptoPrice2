package com.kfouri.cryptoprice.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kfouri.cryptoprice.R
import com.kfouri.cryptoprice.databinding.ButtonLayoutBinding

class ButtonView(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    private var buttonTextAttr: String

    private val binding = ButtonLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ButtonView)

        buttonTextAttr = attributes.getString(R.styleable.ButtonView_text).toString()
        binding.textViewButton.text = buttonTextAttr
        setStatus(false)
    }

    fun setValue(value: String) {
        binding.textViewButton.text = value
    }

    fun setStatus(value: Boolean) {
        if (value) {
            binding.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            binding.textViewButton.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            binding.statusView.setBackgroundColor(Color.TRANSPARENT)
            binding.textViewButton.setTextColor(ContextCompat.getColor(context, R.color.white_grey))
        }
    }
}