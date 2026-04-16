package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agartha.didik.data.ReviewModel
import com.agartha.didik.databinding.FragmentFormReviewBinding

class FormReviewFragment : Fragment() {

    private var _binding: FragmentFormReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Logika Tombol Back (Biar bisa balik ke Dashboard)
        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Logika Tombol Simpan (Send Review)
        binding.btnSave.setOnClickListener {
            val company = binding.etCompanyName.text.toString().trim()
            val position = binding.etPosition.text.toString().trim()
            val jobDesc = binding.etJobDesc.text.toString().trim()
            val reviewText = binding.etReviewText.text.toString().trim()
            val rating = binding.rbReview.rating

            // Validasi: Pastikan semua field terisi dan rating lebih dari 0
            if (company.isNotEmpty() && position.isNotEmpty() && jobDesc.isNotEmpty() && 
                reviewText.isNotEmpty() && rating > 0f) {

                // Ambil nama user yang lagi login dari SharedPreferences
                val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val reviewerName = sharedPref.getString("user_name", "Mahasiswa UPN") ?: "Mahasiswa UPN"
                val viewModel = androidx.lifecycle.ViewModelProvider(requireActivity())[com.agartha.didik.data.ReviewViewModel::class.java]

                // Bungkus ke ReviewModel (ID dibuat random dulu karena belum ada DB)
                val newReview = ReviewModel(
                    id = (0..1000).random(),
                    companyName = company,
                    position = position,
                    reviewerName = reviewerName,
                    jobDesc = jobDesc,
                    reviewText = reviewText,
                    rating = rating
                )

                viewModel.addReview(newReview)

                // Feedback Sukses
                Toast.makeText(requireContext(), "Review untuk $company berhasil terkirim!", Toast.LENGTH_SHORT).show()

                // Balik ke Dashboard setelah simpan
                parentFragmentManager.popBackStack()

            } else {
                // Beri pesan spesifik jika ada yang belum terisi
                val message = when {
                    company.isEmpty() || position.isEmpty() || jobDesc.isEmpty() || reviewText.isEmpty() -> 
                        "Semua kolom teks harus diisi!"
                    rating <= 0f -> "Jangan lupa berikan rating bintangnya ya!"
                    else -> "Mohon lengkapi semua data review!"
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
