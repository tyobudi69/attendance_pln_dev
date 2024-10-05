package com.example.absensi_pln_dev.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.absensi_pln_dev.data.database.AppDatabase
import com.example.absensi_pln_dev.data.model.User
import com.example.absensi_pln_dev.repo.AttendanceRepository
import com.example.absensi_pln_dev.viewmodel.AttendanceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "attendance-db").build()
        val repository = AttendanceRepository(db.userDao(), db.attendanceDao())
        val attendanceViewModel = AttendanceViewModel(repository)

        // Initialize Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize ActivityResultLauncher for location permissions
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, get location
                requestLocation{ lat, long ->
                    // Handle location updates as needed
                }
            } else {
                // Permission denied, show a message to the user
                // You can add a Snackbar or a Toast here
            }
        }

        setContent {
            AbsensiTheme {
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var isSignedIn by remember { mutableStateOf(false) }
                var latitude by remember { mutableStateOf(0.0) }
                var longitude by remember { mutableStateOf(0.0) }
                var imageUri by remember { mutableStateOf<Uri?>(null) }
                var showHistory by remember { mutableStateOf(false) }

                if (!isSignedIn) {
                    SignInScreen(
                        email = email,
                        onEmailChange = { email = it },
                        password = password,
                        onPasswordChange = { password = it },
                        onSignIn = { email, password ->
                            attendanceViewModel.signIn(email, password, {
                                isSignedIn = true
                            }, {
                                // Handle sign-in failure
                            })
                        },
                        onSignUp = {
                            attendanceViewModel.insertUser(User(email = email, name = "User", password = password))
                        }
                    )
                } else {
                    if (showHistory) {
                        AttendanceHistoryScreen(
                            email = email,
                            attendanceViewModel = attendanceViewModel,
                            onBack = { showHistory = false }
                        )
                    } else {
                        AttendanceScreen(
                            onTimeIn = {
                                requestLocation { lat, long ->
                                    latitude = lat
                                    longitude = long
                                    attendanceViewModel.recordTimeIn(email, lat, long, imageUri.toString())
                                }
                            },
                            onTimeOut = {
                                requestLocation { lat, long ->
                                    latitude = lat
                                    longitude = long
                                    attendanceViewModel.recordTimeOut(email, lat, long, imageUri.toString())
                                }
                            },
                            onLogout = { isSignedIn = false },
                            onViewHistory = { showHistory = true },
                            onSelectImage = { selectImage { uri -> imageUri = uri } }
                        )
                    }
                }
            }
        }
    }

    private fun requestLocation(onLocationReceived: (Double, Double) -> Unit) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener(OnSuccessListener<Location> { location ->
                location?.let {
                    onLocationReceived(it.latitude, it.longitude)
                }
            })
        } else {
            // Request location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun selectImage(onImageSelected: (Uri) -> Unit) {
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { onImageSelected(it) }
        }
        getContent.launch("image/*")
    }
}

@Composable
fun SignInScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSignIn: (String, String) -> Unit,
    onSignUp: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        TextField(value = email, onValueChange = onEmailChange, label = { Text("Email") })
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = { onSignIn(email, password) }) {
            Text("Sign In")
        }
        Button(onClick = { onSignUp() }) {
            Text("Sign Up")
        }
    }
}

@Composable
fun AttendanceScreen(
    onTimeIn: () -> Unit,
    onTimeOut: () -> Unit,
    onLogout: () -> Unit,
    onViewHistory: () -> Unit,
    onSelectImage: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = onSelectImage) {
            Text("Select Image")
        }
        Button(onClick = onTimeIn) {
            Text("Time In")
        }
        Button(onClick = onTimeOut) {
            Text("Time Out")
        }
        Button(onClick = onViewHistory) {
            Text("View History")
        }
        Button(onClick = onLogout) {
            Text("Logout")
        }
    }
}

@Composable
fun AttendanceHistoryScreen(email: String, attendanceViewModel: AttendanceViewModel, onBack: () -> Unit) {
    // Implement a screen that shows the user's attendance history
    // This could be a list of attendance records retrieved from the ViewModel
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text("Attendance History for $email")
        // Display attendance records here

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

private val DarkColorPalette = darkColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF6200EE),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6)
)

@Composable
fun AbsensiTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
