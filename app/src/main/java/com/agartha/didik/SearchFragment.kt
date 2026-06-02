package com.agartha.didik

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.data.ReviewViewModel
import com.agartha.didik.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]
        
        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterReviews(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Initial show all or empty? Let's show all initially but filter as they type
        filterReviews("")
    }

    private fun filterReviews(query: String) {
        viewModel.reviews.observe(viewLifecycleOwner) { allReviews ->
            val filtered = if (query.isEmpty()) {
                allReviews
            } else {
                allReviews.filter { 
                    it.companyName.contains(query, ignoreCase = true) || 
                    it.position.contains(query, ignoreCase = true) 
                }
            }

            if (filtered.isEmpty()) {
                binding.rvSearchResults.visibility = View.GONE
                binding.layoutEmptySearch.visibility = View.VISIBLE
            } else {
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.layoutEmptySearch.visibility = View.GONE
                
                // Group by company for unique company results
                val uniqueCompanies = filtered.distinctBy { it.companyName }
                
                binding.rvSearchResults.adapter = ReviewAdapter(uniqueCompanies) { selected ->
                    val detailFragment = CompanyDetailFragment()
                    val bundle = Bundle()
                    bundle.putString("company", selected.companyName)
                    bundle.putString("position", selected.position)
                    bundle.putString("category", selected.category)
                    detailFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
