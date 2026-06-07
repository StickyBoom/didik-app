package com.agartha.didik.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentHistoryBinding
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.ui.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            binding.rvHistory.adapter = ReviewAdapter(reviews) {
                // Redirect to Review Form for that application
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FormReviewFragment())
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
