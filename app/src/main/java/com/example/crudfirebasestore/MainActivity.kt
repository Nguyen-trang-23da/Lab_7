package com.example.crudfirebasestore

import android.annotation.SuppressLint
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState")
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
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "GFG",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = greenColor
                                )
                            )
                        }
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "Thêm dữ liệu",
                                style = TextStyle(fontSize = 18.sp)
                            )
                            FirebaseUI(LocalContext.current)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseUI(context: Context) {

    // State cho các trường nhập liệu
    val courseID = remember { mutableStateOf("") }
    val courseName = remember { mutableStateOf("") }
    val courseDuration = remember { mutableStateOf("") }
    val courseDescription = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Input: Tên khóa học
        TextField(
            value = courseName.value,
            onValueChange = { courseName.value = it },
            placeholder = { Text("Enter your course name") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Input: Thời lượng
        TextField(
            value = courseDuration.value,
            onValueChange = { courseDuration.value = it },
            placeholder = { Text("Enter your course duration") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Input: Mô tả
        TextField(
            value = courseDescription.value,
            onValueChange = { courseDescription.value = it },
            placeholder = { Text("Enter your course description") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Nút thêm dữ liệu
        Button(
            onClick = {
                if (TextUtils.isEmpty(courseName.value)) {
                    Toast.makeText(context, "Please enter course name", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(courseDuration.value)) {
                    Toast.makeText(context, "Please enter course duration", Toast.LENGTH_SHORT).show()
                } else if (TextUtils.isEmpty(courseDescription.value)) {
                    Toast.makeText(context, "Please enter course description", Toast.LENGTH_SHORT).show()
                } else {
                    addDataToFirebase(
                        courseID.value,
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
            Text(text = "Add Data", modifier = Modifier.padding(8.dp))
        }

        // Nút xem danh sách khóa học
        Button(
            onClick = {
                context.startActivity(Intent(context, CourseDetailsActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "View Courses", modifier = Modifier.padding(8.dp))
        }
    }
}

// ✅ Hàm thêm dữ liệu vào Firestore
fun addDataToFirebase(
    courseID: String,
    courseName: String,
    courseDuration: String,
    courseDescription: String,
    context: Context
) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbCourses: CollectionReference = db.collection("Courses")
    val courses = Course(courseID, courseName, courseDuration, courseDescription)

    dbCourses.add(courses)
        .addOnSuccessListener {
            Toast.makeText(
                context,
                "Your Course has been added to Firebase Firestore",
                Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Fail to add course \n$e", Toast.LENGTH_SHORT).show()
        }
}

