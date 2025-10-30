package com.example.crudfirebasestore

// ✅ Data class dùng cho Realtime Database
data class Course(
    var courseID: String? = "",
    var courseName: String? = "",
    var courseDuration: String? = "",
    var courseDescription: String? = ""
)
