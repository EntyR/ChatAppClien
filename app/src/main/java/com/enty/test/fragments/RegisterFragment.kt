package com.enty.test.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.enty.test.R
import com.enty.test.data.Ktor
import com.enty.test.data.UsersServiceImp
import com.enty.test.databinding.FragmentRegisterBinding
import com.enty.test.utils.*
import com.enty.ui.viewmodel.UserViewModel
import com.entyr.model.RegisterRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    val viewModel: UserViewModel by viewModels()

    lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!getToken(requireContext()).isNullOrBlank() && !getUser(requireContext()).isNullOrBlank())
            findNavController().navigate(R.id.postFragment)



        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btSignUp.setOnClickListener {


            val email = binding.etEmail.text
            val password = binding.etPassword.text
            val passwordRepeat = binding.etPasswordConfirm.text
            val passwordEq = password.toString().equals(passwordRepeat.toString())
            Log.e("TAG", "onCreateView: $passwordEq", )
            val name = binding.etName.text
            val isEmailOk = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            if (!isEmailOk || password.isBlank()|| name.isBlank()||!passwordEq || password.isBlank()) {
                Snackbar.make(view!!, "Specify correct signatures", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val request = RegisterRequest(
                binding.etEmail.text.toString(),
                binding.etName.text.toString(),
                binding.etPassword.text.toString()
            )

            putUser(requireContext(), email.toString())
            viewModel.registry(request)


        }

        viewModel.response.observe(viewLifecycleOwner, Observer {

            when (it){
                is Resource.Success -> {

                    findNavController().navigate(R.id.postFragment)
                }
                is Resource.Error -> Toast.makeText(
                    requireContext(),
                    "${it.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })

        binding.tvSwitchToLogIn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }



        return binding.root
    }


}