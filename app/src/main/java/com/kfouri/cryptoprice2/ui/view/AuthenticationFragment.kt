package com.kfouri.cryptoprice2.ui.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.FragmentAuthenticationBinding
import com.kfouri.cryptoprice2.ui.view.ConfirmPasswordFragment.Companion.PREF_CONFIRM_PASSWORD
import com.kfouri.cryptoprice2.ui.view.CreatePasswordFragment.Companion.PREF

class AuthenticationFragment: Fragment() {

    private lateinit var binding: FragmentAuthenticationBinding
    private var posValue: Int = 0
    private lateinit var preferences: SharedPreferences

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
            }
        )
    }

}