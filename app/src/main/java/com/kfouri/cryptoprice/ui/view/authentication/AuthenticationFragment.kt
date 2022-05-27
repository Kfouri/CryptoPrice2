package com.kfouri.cryptoprice.ui.view.authentication

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kfouri.cryptoprice.R
import com.kfouri.cryptoprice.databinding.FragmentAuthenticationBinding
import com.kfouri.cryptoprice.ui.view.authentication.ConfirmPasswordFragment.Companion.PREF_CONFIRM_PASSWORD
import com.kfouri.cryptoprice.ui.view.authentication.CreatePasswordFragment.Companion.PREF
import java.util.concurrent.Executor

class AuthenticationFragment: Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding
    private var posValue: Int = 0
    private lateinit var preferences: SharedPreferences

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d("Kafu", "error en huella: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("Kafu", "Exito")
                    findNavController().navigate(AuthenticationFragmentDirections.actionPasswordSuccessful())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d("Kafu", "Falló")
                }
            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_authentication, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            preferences = it.getSharedPreferences(PREF, MODE_PRIVATE)
            if (preferences.getInt(PREF_CONFIRM_PASSWORD, -1) == -1) {
                findNavController().navigate(AuthenticationFragmentDirections.actionCreatePassword())
            }
        }

        binding.numericPadView.numericPad(
            { value ->
                posValue++
                binding.enterPasswordView.setValue(value.toString(), posValue) { pass ->
                    if (preferences.getInt(PREF_CONFIRM_PASSWORD, -1) == pass.toInt()) {
                        findNavController().navigate(AuthenticationFragmentDirections.actionPasswordSuccessful())
                    } else if (posValue == 6) {
                        Toast.makeText(activity, "Contraseña incorrecta!", Toast.LENGTH_LONG).show()
                    }
                }
            },
            {
                if (posValue > 0) {
                    binding.enterPasswordView.setValue("", posValue) {}
                    posValue--
                }
            },
            {
                Log.d("Kafu", "Extra Button")
                biometricPrompt.authenticate(promptInfo)
            }
        )

        executor = activity?.applicationContext?.let { getMainExecutor(it) }!!

        biometricPrompt = BiometricPrompt(this, executor, authenticationCallback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Utilizá tus datos biométricos para desbloquear")
            .setNegativeButtonText("Cancel")
            .build()

        binding.numericPadView.setExtraVisibility(checkBiometricSupport())

    }

    private fun checkBiometricSupport(): Boolean {

        val biometricManager = activity?.let { BiometricManager.from(it.applicationContext) }

        return when (biometricManager?.canAuthenticate(BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("Kafu","Fingerprint BIOMETRIC_ERROR_NO_HARDWARE")
                false
            }
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("Kafu","Fingerprint BIOMETRIC_SUCCESS")
                if (preferences.getInt(PREF_CONFIRM_PASSWORD, -1) > -1) {
                    biometricPrompt.authenticate(promptInfo)
                }
                true
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("Kafu","Fingerprint BIOMETRIC_ERROR_HW_UNAVAILABLE")
                false
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.d("Kafu","Fingerprint BIOMETRIC_STATUS_UNKNOWN")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.d("Kafu","Fingerprint BIOMETRIC_ERROR_UNSUPPORTED")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d("Kafu","Fingerprint BIOMETRIC_ERROR_NONE_ENROLLED")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.d("Kafu","Fingerprint BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED")
                false
            }
            else -> {
                Log.d("Kafu","Fingerprint ELSE")
                false
            }
        }
    }
}