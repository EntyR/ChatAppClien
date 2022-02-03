package com.enty.test.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enty.test.ChatAdapter
import com.enty.test.R
import com.enty.test.databinding.FragmentDialogBinding
import com.enty.test.utils.getToken
import com.enty.ui.PickImageFileContract
import com.enty.ui.viewmodel.DialogsViewModel
import com.entyr.model.AddMessageRequest
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.date.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DialogFragment : Fragment() {

    lateinit var launcher: ActivityResultLauncher<Unit>
    lateinit var binding: FragmentDialogBinding

    val viewModel: DialogsViewModel by viewModels()

    var userEmail: String = ""

    var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userEmail = (arguments?.get("useremail") ?: "") as String
        userName = (arguments?.get("username") ?: "") as String

        if (getToken(requireContext()) == null)
            findNavController().navigate(R.id.loginFragment)

        launcher = registerForActivityResult(PickImageFileContract()) { uri ->

            lifecycleScope.launch {
                if (!uri?.path.isNullOrEmpty()) {

                    requireContext().contentResolver.openInputStream(uri!!)?.use {
                        val uri = viewModel.photoService.addPhoto(
                            it.readBytes(),
                            getToken(requireContext())!!
                        )
                        viewModel.sendMessage(
                            AddMessageRequest(
                                userEmail, "", getTimeMillis(), 1, 1, uri, false
                            ), getToken(requireContext())!!
                        )
                    }

                }
            }
            Log.e("Image", "photo info: " + uri?.path.toString())

        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            com.enty.test.databinding.FragmentDialogBinding.inflate(inflater, container, false)

        val adapter = ChatAdapter(requireContext())

        viewModel.post.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.connectToChat(getToken(requireContext())!!, userEmail)
        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
        binding.topAppBar.title = userName
        binding.topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivSend.setOnClickListener {

            val message = AddMessageRequest(
                userEmail,
                binding.tvMessage.text.toString(),
                getTimeMillis(),
                1, 1,
                " ", false
            )
            binding.tvMessage.setText("")
            Log.e("MEssage", "onCreateView: $message")
            viewModel.sendMessage(message, getToken(requireContext())!!)
        }
        binding.imageView.setOnClickListener {
            launcher.launch(Unit)
        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearSocket()
    }
}