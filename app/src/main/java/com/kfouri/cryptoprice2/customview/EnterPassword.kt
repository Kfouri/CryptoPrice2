package com.kfouri.cryptoprice2.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.DotsLayoutBinding
import android.graphics.drawable.GradientDrawable

class EnterPassword(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {

    private val binding = DotsLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private var dotNormalColorAttr: Int
    private var dotFilledColorAttr: Int
    private var password: String = ""

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.EnterPassword)

        dotNormalColorAttr = attributes.getResourceId(
            R.styleable.EnterPassword_dotNormalColor,
            R.color.dotNormalColor
        )

        dotFilledColorAttr = attributes.getResourceId(
            R.styleable.EnterPassword_dotFilledColor,
            R.color.dotFilledColor
        )

        (binding.imageViewDot1.drawable as GradientDrawable).setColor(ContextCompat.getColor(context, dotNormalColorAttr))
        (binding.imageViewDot2.drawable as GradientDrawable).setColor(ContextCompat.getColor(context, dotNormalColorAttr))
        (binding.imageViewDot3.drawable as GradientDrawable).setColor(ContextCompat.getColor(context, dotNormalColorAttr))
        (binding.imageViewDot4.drawable as GradientDrawable).setColor(ContextCompat.getColor(context, dotNormalColorAttr))
        (binding.imageViewDot5.drawable as GradientDrawable).setColor(ContextCompat.getColor(context, dotNormalColorAttr))
        (binding.imageViewDot6.drawable as GradientDrawable).setColor(ContextCompat.getColor(context, dotNormalColorAttr))

    }

    fun setValue(value: String, pos: Int, onCompleted: ((password: String)-> Unit)) {

        val shape: GradientDrawable

        if (value.isEmpty()) {
            password = password.substring(0, password.length - 1)
        } else {
            password += value
        }

        when (pos) {
            1 -> {
                shape = binding.imageViewDot1.drawable as GradientDrawable
                if (value.isEmpty()) {
                    shape.setColor(ContextCompat.getColor(context, dotNormalColorAttr))
                } else {
                    shape.setColor(ContextCompat.getColor(context, dotFilledColorAttr))
                }
            }
            2 -> {
                shape = binding.imageViewDot2.drawable as GradientDrawable
                if (value.isEmpty()) {
                    shape.setColor(ContextCompat.getColor(context, dotNormalColorAttr))
                } else {
                    shape.setColor(ContextCompat.getColor(context, dotFilledColorAttr))
                }
            }
            3 -> {
                shape = binding.imageViewDot3.drawable as GradientDrawable
                if (value.isEmpty()) {
                    shape.setColor(ContextCompat.getColor(context, dotNormalColorAttr))
                } else {
                    shape.setColor(ContextCompat.getColor(context, dotFilledColorAttr))
                }
            }
            4 -> {
                shape = binding.imageViewDot4.drawable as GradientDrawable
                if (value.isEmpty()) {
                    shape.setColor(ContextCompat.getColor(context, dotNormalColorAttr))
                } else {
                    shape.setColor(ContextCompat.getColor(context, dotFilledColorAttr))
                }
            }
            5 -> {
                shape = binding.imageViewDot5.drawable as GradientDrawable
                if (value.isEmpty()) {
                    shape.setColor(ContextCompat.getColor(context, dotNormalColorAttr))
                } else {
                    shape.setColor(ContextCompat.getColor(context, dotFilledColorAttr))
                }
            }
            6 -> {
                shape = binding.imageViewDot6.drawable as GradientDrawable
                if (value.isEmpty()) {
                    shape.setColor(ContextCompat.getColor(context, dotNormalColorAttr))
                } else {
                    shape.setColor(ContextCompat.getColor(context, dotFilledColorAttr))
                }
            }
        }

        if (pos == 6) {
            onCompleted.invoke(password)
        }
    }

    fun getValue(): String {
        return password
    }
}