import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

var properties = Properties()
properties.load(FileInputStream("local.properties"))


android {

    buildFeatures {
        buildConfig = true
    }

    namespace = "com.kolown.login"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField(
                "String",
                "GOOGLE_CLIENT_ID",
                properties.getProperty("google_cient_id")
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // credential, auth
    implementation(libs.androidx.credentials)
    implementation(libs.google.android.googleid)
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.androidx.credentials.play.services.auth)

    implementation(libs.bundles.android.compose)

    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.common)
}
