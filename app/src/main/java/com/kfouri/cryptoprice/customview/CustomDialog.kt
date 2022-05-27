package com.kfouri.cryptoprice.customview

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.WindowManager
import android.widget.EditText
import com.kfouri.cryptoprice.ext.toEditable

class CustomDialog(context: Context) : AlertDialog.Builder(context) {

    lateinit var onResponse: (r : DialogResponse) -> Unit

    data class DialogResponse(
        val responseType: ResponseType,
        val value: String?
    )

    enum class ResponseType {
        YES, NO, CANCEL
    }

    fun show(title: String, message: String, value: String? = null, listener: (r : DialogResponse) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        onResponse = listener
        val input = EditText(context)

        value?.let {
            input.keyListener = DigitsKeyListener.getInstance("0123456789.")
            input.inputType =
                InputType.TYPE_CLASS_NUMBER or
                InputType.TYPE_NUMBER_FLAG_DECIMAL

            input.text = value.toEditable()
            input.setSelectAllOnFocus(true)
            input.requestFocus()

            builder.setView(input)
        }

        // performing positive action
        builder.setPositiveButton("Yes") { _, _ ->
            onResponse(DialogResponse(ResponseType.YES, input.text.toString()))
        }

        // performing negative action
        builder.setNegativeButton("No") { _, _ ->
            onResponse(DialogResponse(ResponseType.NO, null))
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()

        // Set other dialog properties
        alertDialog.setCancelable(false)
        value?.let {
            alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
        alertDialog.show()
    }
}