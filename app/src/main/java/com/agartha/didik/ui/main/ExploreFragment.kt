package com.agartha.didik.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentExploreBinding
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.ui.company.CompanyDetailFragment
import com.agartha.didik.ui.review.ReviewViewModel

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvInternships.layoutManager = LinearLayoutManager(requireContext())
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            binding.rvInternships.adapter = ReviewAdapter(reviews) { review ->
                val fragment = CompanyDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString("company", review.companyName)
                        putString("position", review.position)
                        putString("category", review.category)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
