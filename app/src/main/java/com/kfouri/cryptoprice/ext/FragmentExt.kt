package com.kfouri.cryptoprice.ext

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kfouri.cryptoprice.R

fun Fragment.showError(throwable: Throwable?) {
    val error = throwable?.message ?: return
    Snackbar.make(view!!, error, Snackbar.LENGTH_LONG).show()
}

fun Fragment.showInProgress() {
    try {
        loadingView()?.isVisible = true
        requireActivity().window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    } catch (e: Exception) {
        Log.e("requireActivity()", "Fragment.showInProgress()")
    }
}

fun Fragment.hideInProgress() {
    try {
        loadingView()?.isVisible = false
        requireActivity().window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    } catch (e: Exception) {
        Log.e("requireActivity()", "FragmentExt")
    }
}

fun Fragment.showSnackbar(text: String) {
    Snackbar.make(view!!, text, Snackbar.LENGTH_LONG).show()
}

internal fun Fragment.loadingView(): View? {
    try {
        return view?.findViewById(R.id.loadingView)
            ?: requireActivity().findViewById(R.id.loadingView)
    } catch (e: Exception) {
        Log.e("requireActivity()", "Fragment.loadingView()")
    }

    return null
}

fun Fragment.showDialog(
    text: String,
    buttonText: String,
    isCancellable: Boolean = true,
    onOkClick: (() -> Unit)? = null
) {
    AlertDialog.Builder(requireActivity())
            .setMessage(text)
            .setCancelable(isCancellable)
            .setPositiveButton(buttonText){ dialog, _ ->
        onOkClick?.invoke()
        dialog.dismiss()
    }.create().show()
}

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}