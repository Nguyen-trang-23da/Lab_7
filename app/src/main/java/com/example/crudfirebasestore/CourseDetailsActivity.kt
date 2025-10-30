package com.example.crudfirebasestore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crudfirebasestore.ui.theme.CrudFirebaseStoreTheme
import com.example.crudfirebasestore.ui.theme.greenColor
import com.google.firebase.database.*

class CourseDetailsActivity : ComponentActivity() {

    private lateinit var database: DatabaseReference

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
                                        text = "Danh sách khóa học",
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

                        val courseList = mutableStateListOf<Course?>()

                        // ✅ Lấy dữ liệu từ Realtime Database
                        database = FirebaseDatabase.getInstance().getReference("Courses")

                        database.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                courseList.clear()
                                if (snapshot.exists()) {
                                    for (courseSnap in snapshot.children) {
                                        val course = courseSnap.getValue(Course::class.java)
                                        courseList.add(course)
                                    }
                                } else {
                                    Toast.makeText(
                                        this@CourseDetailsActivity,
                                        "Không có dữ liệu trong Realtime Database",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@CourseDetailsActivity,
                                    "Lỗi khi tải dữ liệu: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                        FirebaseListUI(LocalContext.current, courseList, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    // ✅ Giao diện hiển thị danh sách
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FirebaseListUI(
        context: Context,
        courseList: SnapshotStateList<Course?>,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                itemsIndexed(courseList) { index, item ->
                    Card(
                        onClick = {
                            val intent = Intent(context, UpdateCourse::class.java)
                            intent.putExtra("courseID", item?.courseID)
                            intent.putExtra("courseName", item?.courseName)
                            intent.putExtra("courseDuration", item?.courseDuration)
                            intent.putExtra("courseDescription", item?.courseDescription)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            item?.courseName?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = greenColor,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            item?.courseDuration?.let {
                                Text(
                                    text = "Duration: $it",
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }

                            item?.courseDescription?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
