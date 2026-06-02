package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.data.ReviewModel
import com.agartha.didik.data.ReviewViewModel
import com.agartha.didik.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel
    private var currentCategory = "All"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val myName = sharedPref.getString("user_name", "") ?: ""

        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]
        
        binding.rvHistory.layoutManager = LinearLayoutManager(context)

        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            currentCategory = when (checkedIds.firstOrNull()) {
                R.id.chipTech -> "Tech"
                R.id.chipDesign -> "Design"
                R.id.chipBusiness -> "Business"
                else -> "All"
            }
            updateList(myName)
        }

        viewModel.reviews.observe(viewLifecycleOwner) { 
            updateList(myName)
        }
    }

    private fun updateList(myName: String) {
        val allReviews = viewModel.reviews.value ?: emptyList()
        val myHistory = allReviews.filter { it.reviewerName == myName }
        
        val filtered = if (currentCategory == "All") {
            myHistory
        } else {
            myHistory.filter { it.category == currentCategory }
        }

        if (filtered.isNotEmpty()) {
            binding.rvHistory.visibility = View.VISIBLE
            binding.layoutEmptyHistory.visibility = View.GONE
            binding.rvHistory.adapter = ReviewAdapter(filtered) { selectedReview ->
                val detailFragment = DetailReviewFragment()
                val bundle = Bundle()
                bundle.putString("company", selectedReview.companyName)
                bundle.putString("position", selectedReview.position)
                bundle.putString("job", selectedReview.jobDesc)
                bundle.putString("review", selectedReview.reviewText)
                bundle.putFloat("rating", selectedReview.rating)
                bundle.putString("reviewer", selectedReview.reviewerName)
                detailFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            binding.rvHistory.visibility = View.GONE
            binding.layoutEmptyHistory.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
