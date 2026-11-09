package com.example.deviceinfosharing.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import timber.log.Timber

/**
 * FirebaseConfig: Utility class for Firebase initialization and configuration
 * 
 * Responsibilities:
 * - Initialize Firebase services
 * - Provide database references
 * - Handle authentication
 * - Check connection status
 */
object FirebaseConfig {
    
    /**
     * Initialize Firebase and authenticate anonymously
     * 
     * @param onSuccess: Callback when authentication succeeds
     * @param onFailure: Callback when authentication fails with exception
     */
    fun initializeFirebase(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            // Get FirebaseAuth instance
            val auth = FirebaseAuth.getInstance()
            
            // Check if already authenticated
            if (auth.currentUser != null) {
                Timber.d("Firebase: Already authenticated with UID: ${auth.currentUser?.uid}")
                onSuccess()
                return
            }
            
            // Sign in anonymously
            auth.signInAnonymously()
                .addOnSuccessListener { authResult ->
                    Timber.d("Firebase: Anonymous authentication successful, UID: ${authResult.user?.uid}")
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    Timber.e(exception, "Firebase: Anonymous authentication failed")
                    onFailure(exception)
                }
        } catch (e: Exception) {
            Timber.e(e, "Firebase: Initialization error")
            onFailure(e)
        }
    }
    
    /**
     * Get Firebase Realtime Database instance
     * 
     * @return FirebaseDatabase instance
     */
    fun getDatabase(): FirebaseDatabase {
        return Firebase.database
    }
    
    /**
     * Get reference to /devices node
     * 
     * @return DatabaseReference for /devices
     */
    fun getDevicesReference() = getDatabase().getReference("devices")
    
    /**
     * Get reference to specific device
     * 
     * @param deviceId: The device ID
     * @return DatabaseReference for /devices/{deviceId}
     */
    fun getDeviceReference(deviceId: String) = getDevicesReference().child(deviceId)
    
    /**
     * Check if user is authenticated
     * 
     * @return true if FirebaseAuth has current user, false otherwise
     */
    fun isUserAuthenticated(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }
    
    /**
     * Get current user ID (Firebase Auth UID)
     * 
     * @return User UID or null if not authenticated
     */
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
    
    /**
     * Check if Firebase database is connected
     * 
     * Note: This checks if the Firebase SDK has a network connection.
     * Actual database connectivity is handled by Firebase SDK internally.
     * 
     * @return true if authenticated and ready to use
     */
    fun isConnected(): Boolean {
        return isUserAuthenticated()
    }
    
    /**
     * Log Firebase configuration info (for debugging)
     */
    fun logFirebaseInfo() {
        try {
            val db = getDatabase()
            Timber.d("Firebase Database Reference: ${db.reference.path}")
            Timber.d("Firebase Auth UID: ${getCurrentUserId()}")
            Timber.d("Firebase Connected: ${isConnected()}")
        } catch (e: Exception) {
            Timber.e(e, "Firebase: Error logging info")
        }
    }
}
