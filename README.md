# Test-Assignment
This Interview assignment will allow you to Sign Up and Sign In using Parse SDK. On main screen you can see Logged In user Image along with the option to edit it.
## Graddle Configuration

apply plugin: 'com.android.application'

android {
    compileSdkVersion 26
    defaultConfig {
        applicationId "com.interviewtest"
        minSdkVersion 21
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:26.1.0'
    compile 'com.android.support:design:26.+'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    compile 'com.parse:parse-android:1.16.3'
    compile 'cn.ziyeyouhu.android:sweet-alert-dialog:1.0'
    compile 'com.makeramen:roundedimageview:2.3.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile project(':simplecropimagelib_')
    compile 'com.karumi:dexter:4.1.0'
}


