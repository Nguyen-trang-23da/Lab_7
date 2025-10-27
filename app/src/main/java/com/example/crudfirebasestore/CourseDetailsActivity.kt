package com.example.crudfirebasestore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore

class CourseDetailsActivity : ComponentActivity() {
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

                        var courseList = mutableStateListOf<Course?>()
                        val db = FirebaseFirestore.getInstance()

                        db.collection("Courses").get()
                            .addOnSuccessListener { queryDocumentSnapshots ->
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list = queryDocumentSnapshots.documents
                                    for (d in list) {
                                        val c: Course? = d.toObject(Course::class.java)
                                        c?.courseID = d.id
                                        Log.e("TAG", "Course id is : ${c!!.courseID}")
                                        courseList.add(c)
                                    }
                                } else {
                                    Toast.makeText(
                                        this@CourseDetailsActivity,
                                        "No data found in Database",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@CourseDetailsActivity,
                                    "Fail to get the data.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        firebaseUI(LocalContext.current, courseList, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun firebaseUI(
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
                            val i = Intent(context, UpdateCourse::class.java)
                            i.putExtra("courseName", item?.courseName)
                            i.putExtra("courseDuration", item?.courseDuration)
                            i.putExtra("courseDescription", item?.courseDescription)
                            i.putExtra("courseID", item?.courseID)
                            context.startActivity(i)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(6.dp) // ✅ Sửa chỗ lỗi đây
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

                            Spacer(modifier = Modifier.height(5.dp))

                            item?.courseDuration?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            item?.courseDescription?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
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
