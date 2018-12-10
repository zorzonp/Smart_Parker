# Android app of Smart Parker 

The android app allows users to sign up, log in and update information of their account. The android app post request to php, which is able to add information to the database and get information from the database.

## Getting started
### Prerequisites
If you want to run the whole project:
You have to download Android Studio on your computer. Here’s the link to download it.
```
https://developer.android.com/studio/?gclid=EAIaIQobChMIr9-vooCM3wIVj5OzCh0jyAiIEAAYASABEgJ8rvD_BwE
```

if you want to run the app on your android phone:
Click the "app" drop down box next to the build icon, and select 'edit configuration'. If you already have the android phone connected with computer with USB line, just select USB device in Target drop down box.

### How to run
You should clone or download the whole project on your local computer and open the project with Android Studio. 
Sync error may occur when you try to build the project. You can follow Build —> Build Bundle(s)/APK(s) —> Build APK(s). Find the file build.gradle(Module:app) and go to the bottom of code. You can find the path of implementation, change the path to where you store the project.  Then check the file local.properties(SDK Location). Change the path of sdk.dir to where you store the project. Build —> Clean Project, Build—> Rebuild Project. You are able to click on the “run app” button this time. Select any android device. (Mine is Pixel 2 API 28(Android 9, API 28).)

### How to use the app
When getting into the app, you should be on the log in page. If you already have registered an account, you can login with username and password. If you do not have the account, click on SIGN UP and it will help you sign up.

In order to sign up, information including username, first name, last name, password, license	 plate number, state, make, model, year, color, email address is needed. 

After submit the personal information, you can review all the information. If there’s anything you would like to change, click UPDATE INFO, and that page helps you to update everything. You can also log out and log in with username and password you just entered to double check if you’ve already have an account.
