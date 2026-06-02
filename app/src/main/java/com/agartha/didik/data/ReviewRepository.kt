package com.agartha.didik.data

/**
 * Repository sederhana yang bertindak sebagai database sementara (In-Memory).
 * Mengelola daftar review yang ada di dalam aplikasi selama aplikasi berjalan.
 */
object ReviewRepository {
    // List privat untuk menampung data review
    private val reviewList = mutableListOf<ReviewModel>(
        ReviewModel(1, "TechCorp", "Product Design Intern", "Design", "Designing mobile interfaces", "John Doe", "Incredible Growth But High Pressure. The mentorship here is second to none.", 4.5f),
        ReviewModel(2, "TechCorp", "Frontend Intern", "Tech", "Building web components", "Alice Smith", "Great culture and amazing team!", 4.0f),
        ReviewModel(3, "FinBank", "Data Analyst Intern", "Tech", "Analyzing financial data", "John Doe", "Learned a lot about SQL and Python.", 5.0f),
        ReviewModel(4, "Designly", "UI/UX Intern", "Design", "Prototyping new features", "Bob Brown", "Very creative environment.", 4.5f),
        ReviewModel(5, "HealthPlus", "Marketing Intern", "Business", "Managing social media", "Charlie Davis", "Fast paced but rewarding.", 3.5f)
    )

    /**
     * Mengambil semua daftar review yang tersimpan.
     */
    fun getAllReviews(): List<ReviewModel> = reviewList

    /**
     * Menambahkan review baru ke dalam daftar.
     */
    fun addReview(review: ReviewModel) {
        reviewList.add(review)
    }
}
