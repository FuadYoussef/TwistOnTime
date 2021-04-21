## Welcome to Twist on Time

This document will serve as a guide to set up the development environment for the new timer plugin
within the ATAK application. Below, you will find the exact steps about how to do so.

1) Clone the TwistOnTime repository from Github to your computer. You can navigate to your desired folder through Terminal, and type "git clone https://github.com/FuadYoussef/TwistOnTime.git".

2) Download the ATAK.apk file from this link (http://bit.ly/atakapk) and place it somewhere you can remember.

3) Within Android Studio, navigate to your preferences and ensure that the Android SDK is set to API Level 21.

3) Within Android Studio, open the following project: atak-civ/plugin-examples/plugintemplate

4) Within Android Studio, navigate to the Android Virtual Device Manager (AVD) and create a new virtual device. Use the (Phone) Pixel 2 API 28 as your virtual device.

5) In the AVD, select the play button to launch the Pixel 2 AVD in the emulator. Now, drag the ATAK.apk file you downloaded in step 2 onto the Pixel 2 screen to install the ATAK application.

6) In the emulator, open the newly downloaded ATAK application, and be sure to agree to all permissions. You might be asked to type in a passcode. NOTE: keep this passcode in a safe place!

7) Keep your emulator running. Now we are ready to begin the installation process of the Timer plugin. Go back to Android Studio and select "Gradle" on the right-side of your screen. Navigate to and double-click on plugintemplate/Tasks/install/uninstallCivDebug. Then, navigate to and double-click on plugintemplate/Tasks/install/installCivDebug. After a few moments, you should see an alert on the emulator's screen asking "Would you like to load this installed plugin into ATAK?" -- be sure to select "OK".

8) In the ATAK app, select the three vertical dots on the top-right of the screen, and scroll down to the "Twist on Time" plugin. Half of the screen will be populated with the new timer plugin. 


9) Make sure in device settings you enable storage permissions for both ATAK and the Twist on Time plugin to ensure that Presets work properly

10) All done! Great work!
