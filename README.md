# viewer-android-studio-sample
Sample on Workflow of Forge Viewer by Android Studio

[![Android Studio](https://img.shields.io/badge/Android%20Studio-3.0.1-green.svg)](https://developer.android.com/studio/index.html/)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-27-red.svg)](https://developer.android.com/sdk/download.html)

[![OAuth2](https://img.shields.io/badge/OAuth2-v1-green.svg)](http://developer.autodesk.com/)
[![OSS](https://img.shields.io/badge/OSS-v2-green.svg)](https://developer.autodesk.com/en/docs/data/v2/)
[![Model-Derivative](https://img.shields.io/badge/Model%20Derivative-v2-green.svg)](http://developer.autodesk.com/)
[![Forge Viewer](https://img.shields.io/badge/Forge%20Viewer-3.3-yellow.svg)](https://developer.autodesk.com/en/docs/viewer/v2/)

[![License](http://img.shields.io/:license-mit-blue.svg)](http://opensource.org/licenses/MIT)


## Description

This is a sample for Android devices demoing how to translate 2D/3D file by Forge platform, and view them in the browser of mobile.

* Get an access token by [Forge Authentication API] (https://developer.autodesk.com/en/docs/oauth/v2)
* Create a bucket by [Forge Data Management API](https://developer.autodesk.com/en/docs/data/v2)
* Upload a 2D/3D design file / model by [Forge Data Management API](https://developer.autodesk.com/en/docs/data/v2)
* Post a job to translate the model to the Forge Viewer format by [Forge Model Derivative API] (https://developer.autodesk.com/en/docs/model-derivative/v2)
* Download the model thumbnail by [Forge Model Derivative API] (https://developer.autodesk.com/en/docs/model-derivative/v2)
* Display the model in your browser by the workflow of [Forge Viewer](https://developer.autodesk.com/en/docs/viewer/v2/)

## Setup

* Install [Adroind Studio] (https://developer.android.com/studio/index.html/) and the neccesary API SDK. In this test, [Android API SDK](https://developer.android.com/sdk/download.html) version 28 is installed. 
* Download and build Forge library by [Forge JAVA SDK](https://github.com/Autodesk-Forge/forge-api-java-client). Copy all *    .jar (except httpcore-4.4.1.jar and httpclient-4.5.jar) in the target>>lib folder to [app>>lib folder](app/libs)of this sample. For convenience, these .jar are already included in the project. However please syncronize with [Forge JAVA SDK](https://github.com/Autodesk-Forge/forge-api-java-client) if it has new commits.
* Make sure to provide test models in the extension storage of the emulator or physical mobile. To copy a file to emulator, [Android Device Monitor](https://developer.android.com/studio/profile/monitor.html) can be used. Or, copy by [adb in command line](https://stackoverflow.com/questions/30434451/how-to-push-files-to-an-emulator-instance-using-android-studio). A test model has been provided a model at [demomodel/RevitNative.rvt].  
* Visit the [Forge Developer Portal](https://developer.autodesk.com), sign up for an account, then [create an app](https://developer.autodesk.com/myapps/create).Copy API key and secret to line 15 and line 16 in [Global.java](app/src/main/java/com/autodesk/forge/forgeviewer_android_sample/Global.java)
* Build the app and fix any errors

## APK Demo

* Download [ForgeAndroidTestView.apk_]()
  and rename it to ForgeAndroidTestView.apk.
* Install it on your Android device.
* Run the app 'ForgeAndroidTestView'.

## Usage
* Open the App on android device or emulator, click [Get Token] >> token should appear in the text box.
* Input bucket name in text near box under [create bucket], click [Create Bucket] >> wait for the success info. The bucket key (i.e. bucket name) must match “^[-_.a-z0-9]{3,128}$”. That is bucket must be between 3 to 128 characters long and contain only lower-case letters, numbers and the symbols . _ –.  And bucket keys must be unique within the data center or region in which they were created. Best practice is to incorporate your company name/domain name or the client Id into the bucket name. If you prefer to use the client Id(should convert to lower-case first) as part of bucket name, please note that it is the consumer key, not the secret key.  Please also pay attention to the length of the whole bucket name, which should be less than 128.
* Click [Browser Model] to show available test file list, choose one of them
* Click [Upload Model] to upload the model file, the URN appears in the text box  once file is uploaded.
* Click [Post Job] to send a job to translate the model, status shows up in text box.
* Once the translation status is completed, click [Show thumbnail] to get the thumbnail and display it in the image box blow. You may see "get thumbnail failed" if translation is still not in progress, you can try again latter. 
* Click [Launch Viewer] to launch the viewer in a WebGL enabled browser. It is tested on the default browser of the device. If you have problems with this step, you can look into console of browser with USB debugging.  

## License

This sample is licensed under the terms of the [MIT License](http://opensource.org/licenses/MIT). 
Please see the [LICENSE](LICENSE) file for full details.


## Written by 

[Xiaodong Liang](https://twitter.com/coldwood) <br />
Developer Advocate <br />
Autodesk Forge Partner Development

