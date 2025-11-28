package github.detrig.weatherapp.findcity.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import github.detrig.weatherapp.R

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

//@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun GetUserLocationScreenWrapper(
    onLocationProvided: (Double, Double) -> Unit,
    onFailed: (String) -> Unit
) {
    val context = LocalContext.current
    val geoError = context.getString(R.string.geo_error)
    val permissionError = context.getString(R.string.error_granted_permission)

    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        10_000L
    )
        .setWaitForAccurateLocation(false)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            if (location != null) {
                fusedLocationClient.removeLocationUpdates(this)
                onLocationProvided(location.latitude, location.longitude)
            } else {
                fusedLocationClient.removeLocationUpdates(this)
                onFailed("Не удалось получить координаты")
            }
        }
    }

    fun startLocationRequest() {
        //Явная проверка прав — то, чего хочет Lint
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineGranted && !coarseGranted) {
            onFailed(permissionError)
            return
        }

        if (!isLocationEnabled(context)) {
            onFailed(geoError) // например: "Геолокация отключена в настройках устройства"
            return
        }

        val tokenSource = CancellationTokenSource()

        fusedLocationClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                tokenSource.token
            )
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationProvided(location.latitude, location.longitude)
                } else {
                    // fallback: уже после проверки прав
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
            .addOnFailureListener { e ->
                onFailed(e.message ?: geoError)
            }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        val isGranted =
            permission[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (isGranted) {
            // Разрешения запрошены → пробуем стартовать
            startLocationRequest()
        } else {
            onFailed(permissionError)
        }
    }



    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            startLocationRequest()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

