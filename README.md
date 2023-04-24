# MRFIT

Description: This is a workout app that will allow users to track their progress in the gym. By creating custom workout templates and exercises, a user can keep track of what they want to do when they hit the gym, and their past progress. They may also create goals to aspire towards, and notifications to keep themselves motivated.

Style Guide: https://developer.android.com/kotlin/style-guide

## External Requirements

If not using an IDE with a built-in emulator such as [Android Studio](https://developer.android.com/studio), it is recommended to download an Android emulator such as [Bluestacks](https://www.bluestacks.com/)

## Setup

Due to integration with Firebase modules, if building this project you must make and add the app to a project in the [Firebase Console].

The required modules include:
* Authentication, with the following methods enabled:
  * Email/Password
  * Google
* Storage
* Firestore Database

# Testing

The unit tests are in `MrFit.app.unitTest`.

The behavioral tests are in `MrFit.app.androidTest`.

# Creating Tests

MrFit should come with the necessary tests, but if needed both tests may be built by going into *Run* > *Edit Configurations*

For the unit test, create a new JUnit test, with `MrFit.app.unitTest` as its module, and `HelperTest` as its class.

For the behavioral test, create a new Android Instrumented Test, with `MrFit.app.androidTest` as its module.

## Running Tests in Android Studio

To run the unit test, select and run the `HelperTest` configuration.

To fun the behavioral test, select and run the `All Behavioral Tests` configuration.

# Authors

Esam Sartawi: esartawi@email.sc.edu

Nicole Calderon: ngc2@email.sc.edu

Nikolas Melendez: melendn@email.sc.edu

Lauren Hadlow: lhadlow@email.sc.edu & lehadlow@gmail.com
