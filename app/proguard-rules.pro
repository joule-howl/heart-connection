# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-verbose

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Preserve annotation classes
-keepattributes *Annotation*

# Preserve enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Preserve custom application classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.app.Fragment
-keep public class * extends androidx.fragment.app.Fragment

# Preserve native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve custom parcelables
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Preserve serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

##########################################################################
# Firebase Keep Rules
##########################################################################

# Firebase Core
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firebase Database
-keep class com.google.firebase.database.** { *; }
-keep class com.google.firebase.database.collection.** { *; }
-keepclassmembers class com.google.firebase.database.** {
    *;
}

# Firebase Auth
-keep class com.google.firebase.auth.** { *; }
-keepclassmembers class com.google.firebase.auth.** {
    *;
}

# Firebase Analytics
-keep class com.google.firebase.analytics.** { *; }

##########################################################################
# Retrofit Keep Rules
##########################################################################

# Retrofit core library
-keep class retrofit2.** { *; }
-keepclassmembers class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Retrofit interfaces
-keep interface retrofit2.** { *; }

# Keep all retrofit interfaces in our app
-keep interface com.example.deviceinfosharing.data.api.** { *; }

# Retrofit adapters
-keep class retrofit2.adapters.** { *; }

# Retrofit converters (Gson)
-keep class retrofit2.converter.gson.** { *; }

##########################################################################
# Gson Keep Rules
##########################################################################

# Gson serialization
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Keep all classes with @SerializedName
-keepclassmembers class ** {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep all domain models and DTOs for serialization
-keep class com.example.deviceinfosharing.domain.model.** { *; }
-keep class com.example.deviceinfosharing.data.firebase.** { *; }

# Keep Gson JsonElement
-keep class com.google.gson.JsonElement { *; }
-keep class com.google.gson.JsonObject { *; }
-keep class com.google.gson.JsonArray { *; }

##########################################################################
# Hilt Dependency Injection Keep Rules
##########################################################################

# Hilt core library
-keep class dagger.hilt.** { *; }
-keep class com.google.dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Hilt annotations
-keepattributes *Annotation*
-keepattributes Signature

# Keep Hilt generated components
-keep class **_Factory { *; }
-keep class **_Factory$* { *; }
-keep class **_Provide* { *; }
-keep class **_Provide*Factory { *; }
-keep class **_Hilt_* { *; }
-keep class **$$Hilt { *; }

# Keep all classes with Hilt annotations
-keep class * {
    @dagger.hilt.android.AndroidEntryPoint *;
}
-keep class * {
    @dagger.hilt.android.HiltAndroidApp *;
}

# Keep classes with @Module and @InstallIn
-keep class * {
    @dagger.Module <fields>;
    @dagger.Module <methods>;
}
-keep class * {
    @dagger.hilt.InstallIn <methods>;
    @dagger.hilt.InstallIn <fields>;
}

# Keep Hilt generated modules
-keep class **_HiltModules { *; }
-keep class **_HiltModules$* { *; }

##########################################################################
# OkHttp Keep Rules
##########################################################################

# OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# OkHttp logging interceptor
-keep class okhttp3.logging.** { *; }
-keepclassmembers class okhttp3.logging.** { *; }

# Okio (used by OkHttp)
-keep class okio.** { *; }

##########################################################################
# Android Jetpack & Lifecycle
##########################################################################

# Lifecycle library
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class androidx.lifecycle.** { *; }

# Jetpack Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

##########################################################################
# Domain Models and DTOs
##########################################################################

# Keep all domain model classes
-keep class com.example.deviceinfosharing.domain.model.** { *; }
-keepclassmembers class com.example.deviceinfosharing.domain.model.** {
    <init>(...);
    *** get*(...);
    void set*(...);
    boolean is*();
}

# Keep all repository interfaces
-keep interface com.example.deviceinfosharing.domain.repository.** { *; }
-keep class com.example.deviceinfosharing.data.repository.** { *; }

# Keep all DTOs for Firebase serialization
-keep class com.example.deviceinfosharing.data.firebase.** { *; }
-keepclassmembers class com.example.deviceinfosharing.data.firebase.** {
    <init>(...);
    *** get*(...);
    void set*(...);
    boolean is*();
}

# Keep API service interfaces
-keep interface com.example.deviceinfosharing.data.api.** { *; }

##########################################################################
# Kotlin Keep Rules
##########################################################################

# Keep Kotlin classes with @Serializable
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable <fields>;
}

# Kotlin default arguments
-keepclassmembers class ** {
    *** *(...);
}

# Keep Kotlin synthetic classes
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keepclassmembers class kotlinx.coroutines.** {
    *;
}

##########################################################################
# BuildConfig and App Config
##########################################################################

# Keep BuildConfig
-keep class com.example.deviceinfosharing.BuildConfig { 
    public static *** VERSION_CODE;
    public static *** VERSION_NAME;
    public static *** APPLICATION_ID;
}

# Keep app application class
-keep class com.example.deviceinfosharing.MyApplication { *; }
-keep class com.example.deviceinfosharing.HiltApplication { *; }

##########################################################################
# Logger Keep Rules
##########################################################################

# Timber logging library
-keep class timber.log.** { *; }
-keep class timber.log.Timber { *; }
-keepclassmembers class timber.log.Timber {
    public static *** *(...);
}

##########################################################################
# Additional Optimizations
##########################################################################

# Allow optimizations for better app size
-optimizationpasses 5

# Keep the methods used by reflection
-keepclasseswithmembernames class * {
    native <methods>;
}

# Remove verbose logging
-assumenosideeffects class timber.log.Timber {
    public static *** v(...);
    public static *** d(...);
}
