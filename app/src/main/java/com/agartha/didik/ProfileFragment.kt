package com.agartha.didik

import android.content.Context
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
import com.agartha.didik.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ambil nama user yang login
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val myName = sharedPref.getString("user_name", "Nadilah Nusa") ?: "Nadilah Nusa"

        // Tampilkan nama di header profile
        binding.tvProfileName.text = myName

        // 2. Hubungkan ke ViewModel yang SAMA
        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]

        // 3. Setup RecyclerView Riwayat
        binding.rvMyHistory.layoutManager = LinearLayoutManager(context)

        // 4. Pantau data dan FILTER berdasarkan nama
        viewModel.reviews.observe(viewLifecycleOwner) { allReviews ->
            // Kita cuma ambil review yang reviewerName-nya sama dengan nama kita
            val myHistory = allReviews.filter { it.reviewerName == myName }

            if (myHistory.isNotEmpty()) {
                binding.rvMyHistory.visibility = View.VISIBLE
                binding.layoutEmptyProfile.visibility = View.GONE
                
                binding.rvMyHistory.adapter = ReviewAdapter(myHistory) { selectedReview ->
                    // Apa yang terjadi pas kartu di-klik?
                    val detailFragment = DetailReviewFragment()

                    // Kirim data pake Bundle
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
                binding.rvMyHistory.visibility = View.GONE
                binding.layoutEmptyProfile.visibility = View.VISIBLE
            }
        }

        // 5. Logika Tombol Logout
        binding.btnLogout.setOnClickListener {
            // A. Hapus data di SharedPreferences
            val editor = sharedPref.edit()
            editor.remove("user_name") // Hapus session login saja
            editor.apply()

            // B. Pindah ke LoginFragment (Sesuai arsitektur Fragment saat ini)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                // Hapus semua stack back agar tidak bisa balik ke Profile lagi dengan tombol back
                .apply {
                    for (i in 0 until parentFragmentManager.backStackEntryCount) {
                        parentFragmentManager.popBackStack()
                    }
                }
                .commit()

            // C. Kasih notifikasi dikit biar sopan
            Toast.makeText(requireContext(), "Berhasil keluar. Sampai jumpa!", Toast.LENGTH_SHORT).show()
        }

        // 6. Logika Tombol Panah Back
        binding.ivBack.setOnClickListener {
            // Balik ke fragment sebelumnya (Dashboard)
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
