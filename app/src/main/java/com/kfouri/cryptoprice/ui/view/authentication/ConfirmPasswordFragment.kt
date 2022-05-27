package com.kfouri.cryptoprice.ui.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kfouri.cryptoprice.R
import com.kfouri.cryptoprice.databinding.FragmentConfirmPasswordBinding

class ConfirmPasswordFragment: Fragment() {

    companion object {
        const val PREF = "pref"
        const val PREF_PASSWORD = "password"
        const val PREF_CONFIRM_PASSWORD = "confirm_password"
    }

    private lateinit var binding: FragmentConfirmPasswordBinding
    private var posValue: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.numericPadView.numericPad(
            { value ->
                posValue++
                binding.enterPasswordView.setValue(value.toString(), posValue) { pass ->

                    activity?.let {
                        val pref = it.getSharedPreferences(
                            PREF,
                            Context.MODE_PRIVATE
                        )
                        val editor = pref.edit()
                        editor.putInt(PREF_CONFIRM_PASSWORD, pass.toInt())
                        editor.apply()

                        val prefPass = pref.getInt(PREF_PASSWORD, -1)
                        val prefConfirmPass = pref.getInt(PREF_CONFIRM_PASSWORD, -1)

                        if (prefPass > 0 && prefConfirmPass > 0 && prefPass == prefConfirmPass) {
                            findNavController().navigate(ConfirmPasswordFragmentDirections.actionPasswordCreated())
                        } else {
                            Toast.makeText(context, getString(R.string.password_not_match_error), Toast.LENGTH_LONG).show()
                        }
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
            }
        )
    }
}