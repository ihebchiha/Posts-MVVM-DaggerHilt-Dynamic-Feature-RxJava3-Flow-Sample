import extension.addAppModuleDependencies
import extension.addInstrumentationTestDependencies
import extension.addUnitTestDependencies

plugins {
    id(Plugins.ANDROID_APPLICATION_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_PLUGIN)
    id(Plugins.KOTLIN_ANDROID_EXTENSIONS_PLUGIN)
    id(Plugins.KOTLIN_KAPT_PLUGIN)
    id(Plugins.DAGGER_HILT_PLUGIN)
}

android {

    compileSdkVersion(AndroidVersion.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId = AndroidVersion.APPLICATION_ID
        minSdkVersion(AndroidVersion.MIN_SDK_VERSION)
        targetSdkVersion(AndroidVersion.TARGET_SDK_VERSION)
        versionCode = AndroidVersion.VERSION_CODE
        versionName = AndroidVersion.VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // TODO Scheme is  created in data module but with which one, find out
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    //    signingConfigs {
//        create(BuildType.RELEASE) {
//            keyAlias = getLocalProperty("signing.key.alias")
//            keyPassword = getLocalProperty("signing.key.password")
//            storeFile = file(getLocalProperty("signing.store.file"))
//            storePassword = getLocalProperty("signing.store.password")
//        }
//    }

    // Specifies one flavor dimension.
//    flavorDimensions("reactive")
//
//    productFlavors {
//
//        create("rxjava") {
//            dimension = "reactive"
//            applicationIdSuffix = ".rxjava"
//            versionNameSuffix  = "-rxjava"
//        }
//        create("coroutines") {
//            dimension = "reactive"
//            applicationIdSuffix =".coroutines"
//            versionNameSuffix = "-coroutines"
//        }
//    }

//    configurations.all {
//        resolutionStrategy {
//            exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-debug")
//        }
//    }

    packagingOptions {
        exclude("**/attach_hotspot_windows.dll")
        exclude("META-INF/licenses/**")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    android.buildFeatures.dataBinding = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    dynamicFeatures = mutableSetOf(
        Modules.DynamicFeature.POST_DETAIL,
        Modules.DynamicFeature.SEARCH
    )
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Modules.AndroidLibrary.CORE))

    implementation(project(Modules.AndroidLibrary.DOMAIN))
    // TODO Solve Why doesn't work when DATA module is not added to dagger Hilt?
    implementation(project(Modules.AndroidLibrary.DATA))

    addAppModuleDependencies()

    // Unit Tests
    addUnitTestDependencies()
    testImplementation(project(Modules.AndroidLibrary.TEST_UTILS))

    // Instrumentation Tests
    addInstrumentationTestDependencies()
    androidTestImplementation(project(Modules.AndroidLibrary.TEST_UTILS))
}
