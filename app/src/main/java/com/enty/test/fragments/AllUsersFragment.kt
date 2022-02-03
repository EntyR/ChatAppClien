package com.enty.test.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enty.test.AllUsersAdapter
import com.enty.test.ContactAdapter
import com.enty.test.R
import com.enty.test.databinding.FragmentAllUsersBinding
import com.enty.test.utils.getToken
import com.enty.test.utils.getUser
import com.enty.ui.viewmodel.LastMessagesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllUsersFragment : Fragment() {


    val viewModel: LastMessagesViewModel by viewModels()

    lateinit var binding: FragmentAllUsersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllUsersBinding.inflate(inflater, container, false)

        val adapter = AllUsersAdapter(requireContext()){
            Log.e("UI", "onCreateView: $it", )
            val bundle = bundleOf("useremail" to it.email, "username" to it.username)
            findNavController().navigate(R.id.dialogFragment, bundle)
        }
        binding.rvLastMessage.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLastMessage.adapter = adapter

        viewModel.messages.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)

        })

        viewModel.getAllContact(getToken(requireContext())!!)


        return binding.root
    }





}