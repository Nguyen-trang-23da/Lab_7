package com.example.crudfirebasestore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crudfirebasestore.ui.theme.CrudFirebaseStoreTheme
import com.example.crudfirebasestore.ui.theme.greenColor
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrudFirebaseStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = greenColor
                                ),
                                title = {
                                    Text(
                                        text = "Cập nhật khóa học",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Cập nhật dữ liệu.",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium
                            )

                            firebaseUI(
                                LocalContext.current,
                                intent.getStringExtra("courseName"),
                                intent.getStringExtra("courseDuration"),
                                intent.getStringExtra("courseDescription"),
                                intent.getStringExtra("courseID")
                            )
                        }
                    }
                }
            }
        }
    }

    // -------------------- UI cập nhật dữ liệu --------------------
    @Composable
    fun firebaseUI(
        context: Context,
        name: String?,
        duration: String?,
        description: String?,
        courseID: String?
    ) {
        val courseName = remember { mutableStateOf(name ?: "") }
        val courseDuration = remember { mutableStateOf(duration ?: "") }
        val courseDescription = remember { mutableStateOf(description ?: "") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = courseName.value,
                onValueChange = { courseName.value = it },
                placeholder = { Text(text = "Enter course name") },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true
            )

            TextField(
                value = courseDuration.value,
                onValueChange = { courseDuration.value = it },
                placeholder = { Text(text = "Enter course duration") },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true
            )

            TextField(
                value = courseDescription.value,
                onValueChange = { courseDescription.value = it },
                placeholder = { Text(text = "Enter course description") },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (TextUtils.isEmpty(courseName.value)) {
                        Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT).show()
                    } else if (TextUtils.isEmpty(courseDuration.value)) {
                        Toast.makeText(context, "Please enter course duration", Toast.LENGTH_SHORT).show()
                    } else if (TextUtils.isEmpty(courseDescription.value)) {
                        Toast.makeText(context, "Please enter course description", Toast.LENGTH_SHORT).show()
                    } else {
                        updateDataToFirebase(
                            courseID,
                            courseName.value,
                            courseDuration.value,
                            courseDescription.value,
                            context
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Update Data", modifier = Modifier.padding(8.dp))
            }
        }
    }

    // -------------------- Cập nhật Firebase --------------------
    private fun updateDataToFirebase(
        courseID: String?,
        name: String?,
        duration: String?,
        description: String?,
        context: Context
    ) {
        val updatedCourse = Course(courseID, name, duration, description)
        val db = FirebaseFirestore.getInstance()

        db.collection("Courses").document(courseID.toString()).set(updatedCourse)
            .addOnSuccessListener {
                Toast.makeText(context, "Course updated successfully.", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, CourseDetailsActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(context, "Fail to update: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
