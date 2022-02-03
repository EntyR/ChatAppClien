package com.enty.test.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.enty.test.R
import com.enty.test.databinding.FragmentLoginBinding
import com.enty.test.databinding.FragmentRegisterBinding
import com.enty.test.utils.Resource
import com.enty.test.utils.getToken
import com.enty.test.utils.putToken
import com.enty.test.utils.putUser
import com.enty.ui.viewmodel.UserViewModel
import com.entyr.model.LoginRequest
import com.entyr.model.RegisterRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern


@AndroidEntryPoint
class LoginFragment: Fragment() {

    val viewModel: UserViewModel by viewModels()

    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (!getToken(requireContext()).isNullOrBlank())
            findNavController().navigate(R.id.postFragment)

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btSignUp.setOnClickListener {


            val email = binding.etName.text
            val password = binding.etPassword.text
            val isEmailOk = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if (!isEmailOk || password.isBlank()) {
                Snackbar.make(view!!, "Specify correct signatures", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val request = LoginRequest(
                binding.etName.text.toString(),
                binding.etPassword.text.toString()
            )
            putUser(requireContext(), email.toString())
            viewModel.login(request)

        }

        viewModel.response.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Success -> {
                    findNavController().navigate(R.id.postFragment)
                }
                is Resource.Error -> {
                    Log.e("TAG", "onCreateView: ${it.message}", )
                    Snackbar.make(view!!, it.message.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })





        binding.tvSwitchToLogIn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }



        return binding.root
    }


}