# Twist on Time
Twist on Time is a plugin that allows users to run multiple timers at the same time in the ATAK
application. This project was developed by Georgia Tech Junior Design Team 0336 in the Spring
2021 semester in collaboration with the 75th Ranger Regiment.

The current release notes, an install guide, and an installation walkthrough are listed below.

There is also a detailed design document in this repo. That document goes into more detail
about the plugin and our design choices.

# Release Notes
## Features in this Release

- Users can create custom and preset timers, with preset timers being persistent and
    remaining across sessions
- Users can start, pause, reset, or delete running timers
- Users can run multiple timers at the same time
- Users can give timers custom notifications
- Users can set preset timers

## Bug Fixes

- Recent bug fixes include being able to delete preset timers, and timer name getting
    cutoff.

## Known Bugs

Currently there is 1 known bug. It is listed below.

- When the timer is name is comprised of large letters such as 10 W’s, the timer’s name
    may overlap with the timer duration on the home and preset screens.


# Install Guide
Various components detailing the project setup is listed below. A full walkthrough detailing
how to setup the plugin is listed below.

## Project Pre-requisites:

- Install Android Studio
- Download an Android Virtual Device Manager (AVD) with API level 21

## Project Dependent Libraries 
- Download the ATAK.apk file from this link (http://bit.ly/atakapk) and place it
       somewhere you can remember.
    o Within Android Studio, navigate to your preferences and ensure that the Android
       SDK is set to API Level 21.
- Clone the TwistOnTime repository from Github to your computer. You can
       navigate to your desired folder through Terminal, and type "git clone
       https://github.com/FuadYoussef/TwistOnTime.git".
## Project Build/Install Instructions
- Within Android Studio, open the following project: atak-civ/plugin-
       examples/plugintemplate
- Within Android Studio, navigate to the Android Virtual Device Manager (AVD)
       and create a new virtual device. Use the (Phone) Pixel 2 API 28 as your virtual
       device.
- In the AVD, select the play button to launch the Pixel 2 AVD in the emulator.
       Now, drag the ATAK.apk file you downloaded in step 2 onto the Pixel 2 screen to
       install the ATAK application.
- In the emulator, open the newly downloaded ATAK application, and be sure to
       agree to all permissions. You might be asked to type in a passcode. NOTE: keep
       this passcode in a safe place!
- Keep your emulator running. Now we are ready to begin the installation process
       of the Timer plugin. Go back to Android Studio and select "Gradle" on the right-
       side of your screen. Navigate to and double-click on
       plugintemplate/Tasks/install/uninstallCivDebug. Then, navigate to and double-
       click on plugintemplate/Tasks/install/installCivDebug. After a few moments, you
       should see an alert on the emulator's screen asking "Would you like to load this
       installed plugin into ATAK?" -- be sure to select "OK".
    o Now, your current ATAK application is running on your virtual device!
## Project Run instructions:
- In the ATAK app, select the three vertical dots on the top-right of the screen, and
       scroll down to the "Twist on Time" plugin. Half of the screen will be populated
       with the new timer plugin.
## Troubleshooting:
- If you receive a version downgrade error when trying to install the plugin, first
       run uninstallCivDebug under the Gradle tab, and then after the process is
       completed, run installCivDebug under the Gradle tab.



## Installation Walkthrough

1. Clone the TwistOnTime repository from Github to your computer. You can navigate to
    your desired folder through Terminal, and type "git clone
    https://github.com/FuadYoussef/TwistOnTime.git".
2. Download the ATAK.apk file from this link (http://bit.ly/atakapk) and place it
    somewhere you can remember.
3. Within Android Studio, navigate to your preferences and ensure that the Android SDK is
    set to API Level 21.
4. Within Android Studio, open the following project: atak-civ/plugin-
    examples/plugintemplate
5. Within Android Studio, navigate to the Android Virtual Device Manager (AVD) and
    create a new virtual device. Use the (Phone) Pixel 2 API 28 as your virtual device.
6. In the AVD, select the play button to launch the Pixel 2 AVD in the emulator. Now, drag
    the ATAK.apk file you downloaded in step 2 onto the Pixel 2 screen to install the ATAK
    application.
7. In the emulator, open the newly downloaded ATAK application, and be sure to agree to
    all permissions. You might be asked to type in a passcode. NOTE: keep this passcode in a
    safe place!
8. Keep your emulator running. Now we are ready to begin the installation process of the
    Timer plugin. Go back to Android Studio and select "Gradle" on the right-side of your
    screen. Navigate to and double-click on plugintemplate/Tasks/install/uninstallCivDebug.
    Then, navigate to and double-click on plugintemplate/Tasks/install/installCivDebug.
    After a few moments, you should see an alert on the emulator's screen asking "Would
    you like to load this installed plugin into ATAK?" -- be sure to select "OK".
9. In the ATAK app, select the three vertical dots on the top-right of the screen, and scroll
    down to the "Twist on Time" plugin. Half of the screen will be populated with the new
    timer plugin.
10. Make sure in device settings you enable storage permissions for both ATAK and the
    Twist on Time plugin to ensure that Presets work properly. These permissions can be
    changed by going to the settings app on AVD, navigating to app permissions and
    selecting Twist on Time and ATAK.
11. All done! Great work!


