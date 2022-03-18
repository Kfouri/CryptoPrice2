package com.kfouri.cryptoprice2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.databinding.FragmentCreatePasswordBinding
import android.content.Context.MODE_PRIVATE

class CreatePasswordFragment: Fragment() {

    companion object {
        const val PREF = "pref"
        const val PREF_PASSWORD = "password"
    }

    private lateinit var binding: FragmentCreatePasswordBinding
    private var posValue: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.numericPadView.numericPad(
            { value ->
                posValue++
                binding.enterPasswordView.setValue(value.toString(), posValue) { pass ->

                    activity?.let {
                        val editor = it.getSharedPreferences(ConfirmPasswordFragment.PREF, MODE_PRIVATE).edit()
                        editor.putInt(ConfirmPasswordFragment.PREF_PASSWORD, pass.toInt())
                        editor.apply()
                        findNavController().navigate(CreatePasswordFragmentDirections.actionConfirmPassword())
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