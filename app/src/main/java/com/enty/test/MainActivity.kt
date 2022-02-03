package com.enty.test

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.enty.test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val callback: OnBackPressedCallback =
//            object : OnBackPressedCallback(true) {
//
//                override fun handleOnBackPressed() {
//                    val id = findNavController(R.id.fcContainer).currentBackStackEntry?.destination?.id
//                    id?.let { dest ->
//                        findNavController(R.id.fcContainer).navigate(dest)
//                    }
//
//                }
//            }
//
//
//        onBackPressedDispatcher.addCallback(this, callback)

        binding.bnvNavigation.setOnItemSelectedListener {
            val navController = findNavController(R.id.fcContainer)
            when (it.itemId) {
                R.id.bnMessages -> navController.navigate(R.id.allUsersFragment)
                R.id.bnPosts -> navController.navigate(R.id.postFragment)
                R.id.bnContacts -> navController.navigate(R.id.contactFragment)
            }
            true
        }

        binding.fbProfile.setOnClickListener {
            findNavController(R.id.fcContainer).navigate(R.id.userInfo)
        }
        findNavController(R.id.fcContainer).addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.registerFragment
                || destination.id == R.id.loginFragment
                || destination.id == R.id.dialogFragment
                || destination.id == R.id.userInfo
            ) {
                binding.fbProfile.visibility = GONE
                binding.bnvNavigation.visibility = GONE
            } else {
                binding.fbProfile.visibility = VISIBLE
                binding.bnvNavigation.visibility = VISIBLE
            }
        }
    }
}