package com.agartha.didik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.data.ReviewViewModel
import com.agartha.didik.databinding.FragmentCompanyDetailBinding

class CompanyDetailFragment : Fragment() {

    private var _binding: FragmentCompanyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val companyName = arguments?.getString("company") ?: "Unknown Company"
        
        binding.tvToolbarTitle.text = companyName
        binding.tvCompanyPosition.text = arguments?.getString("position") ?: "Internship"
        binding.tvCompanyCategory.text = arguments?.getString("category") ?: "Design & Tech"

        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]

        binding.rvStudentReviews.layoutManager = LinearLayoutManager(context)
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            val companyReviews = reviews.filter { it.companyName == companyName }
            binding.rvStudentReviews.adapter = ReviewAdapter(companyReviews) {
                // Handle review click if needed
            }
        }

        binding.ivBackDetail.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnWriteReview.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormReviewFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnApply.setOnClickListener {
            Toast.makeText(requireContext(), "Redirecting to application page...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
