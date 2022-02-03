package com.enty.test.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.enty.test.R
import com.enty.test.data.ContactsService
import com.enty.test.data.PhotoService
import com.enty.test.databinding.FragmentUserInfoBinding
import com.enty.test.utils.getToken
import com.enty.test.utils.getUser
import com.enty.ui.PickImageFileContract
import com.enty.ui.viewmodel.UserViewModel
import com.entyr.model.UserInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class UserInfo : Fragment() {


    lateinit var launcher: ActivityResultLauncher<Unit>

    lateinit var binding: FragmentUserInfoBinding

    val viewModel: UserViewModel by viewModels()

    @Inject
    lateinit var contactService: ContactsService

    @Inject
    lateinit var photoService: PhotoService
    private var uriStr: Uri? = null
    private var sendUri: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        binding.tvChangePhoto.setText("Change profile picture")

        val liveData = MutableLiveData<UserInfo>()
        lifecycleScope.launch {
            val userInfo = withContext(Dispatchers.IO) {
                contactService.getUserInfo(
                    token = getToken(requireContext())!!,
                    getUser(requireContext())!!
                )
            }
            Log.e("TAG", "userInfo $userInfo: ")
            liveData.value = userInfo

        }

        liveData.observe(viewLifecycleOwner, { userInfo ->
            binding.tvName.text = userInfo.username
            if (!userInfo.profPic.isBlank())
                loadImg(userInfo.profPic, binding.civProfile)
            binding.tvdescribe.setText(userInfo.desc)
            binding.tvUsernameProfile.setText(userInfo.username)
        })



        binding.cardView2.setOnClickListener {
            launcher.launch(Unit)
        }
        binding.btSubmitProfile.setOnClickListener {

            lifecycleScope.launch {

                if (uriStr != null) {
                    requireContext().contentResolver.openInputStream(uriStr!!)?.use {
                        sendUri = withContext(Dispatchers.IO) {
                            photoService.addPhoto(
                                it.readBytes(),
                                getToken(requireContext())!!
                            )
                        }

                    }

                }
                val userInfo = UserInfo(
                    getUser(requireContext())!!,
                    binding.tvUsernameProfile.text.toString(),
                    sendUri,
                    binding.tvdescribe.text.toString()
                )

                Log.e("TAG", "onCreateView: $userInfo")

                binding.tvChangePhoto.setText("Change profile picture")
                viewModel.updateUser(getToken(requireContext())!!, userInfo)

            }


        }


        return binding.root
    }


    private fun loadImg(url: String, view: ImageView) {
        Glide
            .with(requireContext())
            .load(url)
            .fitCenter()
            .override(100)
            .into(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(PickImageFileContract()) { uri ->

            Log.e("Image", "photo info: " + uri?.path.toString())
            if (!uri?.path.isNullOrEmpty()) {
                uriStr = uri!!
                binding.tvChangePhoto.text = File(uri.path).name

            }
        }
    }

}