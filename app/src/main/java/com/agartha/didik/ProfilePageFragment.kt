package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.agartha.didik.data.ReviewViewModel
import com.agartha.didik.databinding.FragmentProfilePageBinding

class ProfilePageFragment : Fragment() {

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val name = sharedPref.getString("user_name", "User")
        val email = sharedPref.getString("reg_email", "user@example.com")

        binding.tvNameLarge.text = name
        binding.tvEmailLarge.text = email

        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            val myReviews = reviews.filter { it.reviewerName == name }
            binding.tvReviewCount.text = myReviews.size.toString()
        }

        binding.btnLogout.setOnClickListener {
            val editor = sharedPref.edit()
            editor.remove("user_name")
            editor.apply()

            (activity as? MainActivity)?.showBottomNav(false)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .apply {
                    for (i in 0 until parentFragmentManager.backStackEntryCount) {
                        parentFragmentManager.popBackStack()
                    }
                }
                .commit()

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        binding.btnSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.btnHelp.setOnClickListener {
            Toast.makeText(requireContext(), "Help Center coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
