package com.enty.test.fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.enty.test.R
import com.enty.test.data.PhotoService
import com.enty.test.data.PostService
import com.enty.test.databinding.FragmentCreatePostBinding
import com.enty.test.utils.Resource
import com.enty.test.utils.getToken
import com.enty.ui.PickImageFileContract
import com.entyr.model.PostRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.date.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : DialogFragment() {

    lateinit var launcher: ActivityResultLauncher<Unit>

    @Inject
    lateinit var photoService: PhotoService

    @Inject
    lateinit var postService: PostService
    var imageUri: Uri? = null
    lateinit var binding: FragmentCreatePostBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        binding.btSubmit.setOnClickListener {
            if (verify() is Resource.Success) {

                lifecycleScope.launch {
                    val stream = requireContext().contentResolver.openInputStream(imageUri!!)
                        ?.use { stream ->
                            val url = photoService.addPhoto(
                                stream.readBytes(),
                                getToken(requireContext())!!
                            )
                            Log.e("Url", "onCreateView: $url")
                            val postRequest = PostRequest(
                                url,
                                getTimeMillis(),
                                binding.tvUsernameProfile.text.toString(),
                                binding.tvCPTitle.text.toString(), "",
                                1,
                                1
                            )
                            postService.createPost(getToken(requireContext())!!, postRequest)

                        }
                    dialog?.dismiss()
                }
            } else {
                Snackbar.make(view!!, verify().message.toString(), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.clPhoto.setOnClickListener {
            launcher.launch(Unit)
        }

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = registerForActivityResult(PickImageFileContract()) { uri ->

            Log.e("Image", "photo info: " + uri?.path.toString())
            if (!uri?.path.isNullOrEmpty()) {
                imageUri = uri!!
                binding.tvFileName.text = File(uri.path).name

            }
        }

        val id = resources.getIdentifier(
            "Theme.MaterialComponents.Light.Dialog",
            "style",
            context?.packageName
        )
        setStyle(STYLE_NORMAL, id)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
            it.window?.setGravity(Gravity.BOTTOM)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                it.window?.setDecorFitsSystemWindows(false)

                binding.root.setOnApplyWindowInsetsListener { view, windowInsets ->
                    val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                    binding.root.setPadding(0, 0, 0, imeHeight)
                    windowInsets
                }
            }
        }
    }

    fun verify(): Resource<Unit> {
        if (imageUri == null)
            return Resource.Error(null, "Please specify an Image for Post")
        if (binding.tvCPTitle.text.isNullOrBlank())
            return Resource.Error(null, "Please enter post title")
        if (binding.tvUsernameProfile.text.isNullOrBlank())
            return Resource.Error(null, "Please enter post description")

        return Resource.Success(
            null
//            PostRequest(
//            imageUri.toString(),
//            getTimeMillis(),
//            binding.tvCreatePostDesk.text.toString(),
//            binding.tvCPTitle.text.toString(),"",
//            1,
//            1
        )
    }


}
