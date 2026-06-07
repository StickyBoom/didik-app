package com.agartha.didik.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentCompanyDetailBinding
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.ui.review.FormReviewFragment
import com.agartha.didik.ui.review.ReviewViewModel

class CompanyDetailFragment : Fragment() {

    private var _binding: FragmentCompanyDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

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
        val position = arguments?.getString("position") ?: "Internship"
        val category = arguments?.getString("category") ?: "Category"
        
        binding.tvToolbarTitle.text = companyName
        // Asumsi ada tvCompanyPosition dan tvCompanyCategory di layout fragment_company_detail
        // Jika tidak ada, baris di bawah ini bisa dikomentari
        // binding.tvCompanyPosition.text = position
        // binding.tvCompanyCategory.text = category
        
        binding.rvStudentReviews.layoutManager = LinearLayoutManager(requireContext())
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            // Filter ulasan berdasarkan nama perusahaan
            val companyReviews = reviews.filter { it.companyName == companyName }
            binding.rvStudentReviews.adapter = ReviewAdapter(companyReviews) {
                // Handle click
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
            Toast.makeText(requireContext(), "Application submitted!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
