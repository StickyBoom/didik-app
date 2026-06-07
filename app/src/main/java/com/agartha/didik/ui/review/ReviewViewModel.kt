package com.agartha.didik.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agartha.didik.data.repository.ReviewRepository
import kotlinx.coroutines.launch

// 🌟 STRUKTUR DATA MODEL YANG DICARI ADAPTER & ACTIVITY TEMENMU
data class ReviewModel(
    val companyName: String,
    val position: String,
    val category: String,
    val jobDesc: String,
    val reviewText: String,
    val rating: Float,
    val reviewerName: String
)

class ReviewViewModel(private val reviewRepository: ReviewRepository) : ViewModel() {

    private val _reviews = MutableLiveData<List<ReviewModel>>()
    val reviews: LiveData<List<ReviewModel>> get() = _reviews

    init {
        loadReviewsFromDatabase()
    }

    private fun loadReviewsFromDatabase() {
        viewModelScope.launch {
            // Kita akan ambil data asli dari Database via Repository
            // dan memetakan (mapping) ke ReviewModel
            val reviewModels = reviewRepository.getAllReviewsWithDetails()
            _reviews.value = reviewModels
        }
    }

    // Fungsi lama buat backup jika database kosong (Optional)
    private fun loadDummyReviews() {
        // ... (tetap ada atau dihapus)
    }
}