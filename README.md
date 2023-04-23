# MRFIT

Description: This is a workout app that will allow users to track their progress in the gym. It will also allow users to share workouts with friends and create workout templates.

Style Guide: https://developer.android.com/kotlin/style-guide

## External Requirements

If not using an IDE with a built-in emulator such as [Android Studio](https://developer.android.com/studio), it is recommended to download an Android emulator such as [Bluestacks](https://www.bluestacks.com/)

## Setup

Due to integration with Firebase modules, you must make and add the app to a project in the [Firebase Console].

The required modules include:
* Authentication, with the following methods enabled:
  * Email/Password
  * Google
  * Facebook
* Storage
* Firestore Database

# Testing

The unit tests are in `MrFit.app.unitTest`.

The behavioral tests are in `MrFit.app.androidTest`.

## Running Tests in Android Studio

For both types of tests, start by going into *Run* > *Edit Configurations*

For unit tests, create a new JUnit4 test. For the module select `MrFit.app.unitTest`, and then select the class of the unit test you want to run.

To run behavioral tests, ![](../../AppData/Local/Temp/profilepicturev2.jpg)create a new Android Instrumented Test pointed at `MrFit.app.androidTest`, then run this new configuration.

# Authors

Esam Sartawi: esartawi@email.sc.edu

Nicole Calderon: ngc2@email.sc.edu

Nikolas Melendez: melendn@email.sc.edu

Lauren Hadlow: lhadlow@email.sc.edu & lehadlow@gmail.com
