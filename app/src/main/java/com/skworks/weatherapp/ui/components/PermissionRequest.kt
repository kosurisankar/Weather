package com.skworks.weatherapp.ui.components

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
/**
 * Composable function representing for asking permission.
 * @param locationPermissionLauncher
 * @param onPermissionGranted
 * @param onPermissionDenied
 */
@Composable
fun PermissionRequest(
    locationPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    onPermissionGranted: () -> Unit,
    onPermissionDenied:  () -> Unit
) {
    // Launching the permission request when the composable is first launched
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val permissionStatus by remember { mutableStateOf("") }

    // Based on the permission state, calling the respective action
    LaunchedEffect(permissionStatus) {
        when (permissionStatus) {
            "granted" -> onPermissionGranted()
            "denied" -> onPermissionDenied()
        }
    }
}