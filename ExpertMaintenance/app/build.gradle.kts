plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.expertmaintenance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expertmaintenance"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    implementation(libs.volley)
    implementation(libs.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.appcompat:appcompat:1.6.1")// Updated appcompat version
    implementation("com.google.android.material:material:1.9.0")// Updated Material components version
    implementation("androidx.activity:activity-ktx:1.7.2") // Add correct version for activity
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Add correct version for constraint layout
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.github.gcacace:signature-pad:1.3.1")
    implementation(libs.material.v121)
    implementation(libs.viewpager2)
    implementation(libs.material.v180)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

}