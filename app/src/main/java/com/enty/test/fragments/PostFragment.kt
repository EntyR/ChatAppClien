package com.enty.test.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.enty.test.R
import com.enty.test.databinding.FragmentPostBinding
import com.enty.ui.PickImageFileContract
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enty.test.PostAdapter
import com.enty.test.data.PhotoService
import com.enty.test.data.PostService
import com.enty.test.utils.checkToken
import com.enty.test.utils.getToken
import com.enty.test.utils.getUser
import com.enty.test.utils.putToken
import com.enty.ui.viewmodel.PostsViewModel
import com.enty.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.features.*
import javax.inject.Inject


@AndroidEntryPoint
class PostFragment : Fragment() {


    val viewModel: PostsViewModel by viewModels()
    @Inject
    lateinit var photoService: PhotoService
    @Inject
    lateinit var postService: PostService



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.e("OncreatePost", "onCreate: ", )

        val permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

        }
        val permissionsToRequest = mutableListOf<String>()

            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)


            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)

        permissionsLauncher.launch(permissionsToRequest.toTypedArray())

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(inflater, container, false)

        binding.fabAddPost.setOnClickListener {
            val dialog = CreatePostFragment()
            dialog.show(parentFragmentManager, "create")
        }
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        val postAdapter = PostAdapter(requireContext())
        binding.rvPosts.adapter = postAdapter
        viewModel.post.observe(viewLifecycleOwner, Observer {
            postAdapter.submitList(it)
        })

        val token = getToken(requireContext())
        if (token.isNullOrBlank())
            findNavController().navigate(R.id.loginFragment)

        Log.e("TAG", "onCreateView Token: ${getUser(requireContext())}", )
        try {
            viewModel.getAllPost(token!!)

        }catch (e: ClientRequestException) {
            putToken(requireContext(), "")
            findNavController().navigate(R.id.loginFragment)
        }




        return binding.root
    }


}