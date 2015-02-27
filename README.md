#Autodesk View and Data API workflow sample for Android


##Description

This is a sample for Android devices demoing workflow of using Autodesk View and Data API:

* Get token and set token
* Create bucket
* Upload model
* Start translation (registration)
* Get thumbnail of model
* Display model in the browser

##Dependencies

* Make sure the necessary *.jar in libs are available, which are already included in the project.
* Make sure to provide test models in res\raw before building the app. The package in this repository has provided a model called testmodel.nwd. These files will be copied to the storage of the mobile at <SD card root>/ADNAndroidTestView when the app is being launched. 

##Setup/Usage Instructions

* Download and install Eclipse and [Android Development Tool-kit](http://developer.android.com/sdk/installing/installing-adt.html#Configure);
* Download and install Android SDK API 18 or above from Android SDK Manager, this sample targets API 18.
* Import the source code from "existing Android code into Workspaces" in Eclipse by clicking file -> Import, browse to the project folder, make sure the "project to Import" appears, and click "finish" button to import the project. 
* Get your consumer key and secret key from http://developer.autodesk.com
* Set the API keys in \src\main\Credentials.java
* Build the project to generate the *.apk, deploy the *.apk to a mobile device. 
* If you prefer to use an emulator, you can create any Android virtual device from Android Virtual Device Manager, please be sure to use API level 18 or above and choose a device with bigger screen.
* Open the App on android device or emulator, click [Get Token] >> token should appear in the text box below
* Input bucket name in text box under [create bucket], click [Create Bucket] >> wait for the success info. The bucket key (i.e. bucket name) must match “^[-_.a-z0-9]{3,128}$”. That is bucket must be between 3 to 128 characters long and contain only lower-case letters, numbers and the symbols . _ –.  And bucket keys must be unique within the data center or region in which they were created. Best practice is to incorporate your company name/domain name or the client Id into the bucket name. If you prefer to use the client Id(should convert to lower-case first) as part of bucket name, please note that it is the consumer key, not the secret key.  Please also pay attention to the length of the whole bucket name, which should be less than 128.
* Click [Browser Model], the files available in the storage folder will appear >> select one of them
* Click [Upload Model], wait for the success info >> the urn will appear in the text box below
* Click [Register Model] >> wait for the success info >> the text below will show the register status
* Click [show thumbnail] >> the thumbnail will appear in the image box. sometimes you would need to wait a moment and click it again as transition might not have completed.
* Click [Launch Viewer] >> select a browser, the model will be displayed in it. This test uses Chrome (version 38.0.2125.114), while you can try to load the model using any browser that is WebGL compatible.

 
## License

This sample is licensed under the terms of the [MIT License](http://opensource.org/licenses/MIT). Please see the [LICENSE](LICENSE) file for full details.

##Written by 

Xiaodong Liang

