# Test-Assignment
This Interview assignment will allow you to Sign Up and Sign In using Parse SDK. On main screen you can see Logged In user Image along with the option to edit it. You can check the video demonstration on the following link:
https://www.youtube.com/watch?v=C6m_Xwf2JrQ&feature=youtu.be

## Graddle Configuration
In graddle file I am putting Parse SDK, Picasso, my native crop library along with Dexter library for run time permission.

```
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
```

## Login Code

Following is the login code for parse user. It also put a validation of minimum 6 digits password for a User.

```
 if(email==null||password==null||email.equalsIgnoreCase("")||password.equalsIgnoreCase("")){
                        uiViewInstance.dialogBox(dialogError,"Kindly fill full information!");
                }else{

                    if(password.length()<6){
                        uiViewInstance.dialogBox(dialogError,"Minimum password length is 6 digits!");
                    }else{
                        pDialog = uiViewInstance.showProgressBar(getActivity());
                        ParseUser.logInInBackground(email, password, new LogInCallback() {

                            @Override
                            public void done(ParseUser user, ParseException e) {

                                pDialog.dismiss();
                                if (user != null) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    if(e.getMessage().equalsIgnoreCase("Invalid username/password.")){
                                        uiViewInstance.dialogBox(dialogError,"Incorrect email or password");
                                    }else{
                                        uiViewInstance.dialogBox(dialogError,"Please try again later!");
                                    }
                                }
                            }
                        });
                    }
```           

## Sign Up Code
Following is the signup code for parse user. It also put a validation of minimum 6 digits password for a User.

```
 ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("password", password);

        pDialog =  uiViewInstance.showProgressBar(getActivity());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {

                if (e == null) {
                    pDialog.dismiss();
                    try {
                        Toast.makeText(getActivity(),"Sign up Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }catch (Exception exp){
                        exp.getMessage();
                    }


                } else {
                    pDialog.dismiss();
                    if(e.getMessage().equalsIgnoreCase("Account already exists for this username.")){
                        Toast.makeText(getActivity(),"Account already exists for this username.", Toast.LENGTH_LONG).show();
                    }else{

                    }

                }
            }
        });
```         
 ## User Login Details
 
 Following code I am using to extract the latest user details from Parse object. Parse.fetchIfNeeded will make it sure that my local parse variable is updated with any changes and will return a fresh copy of ParseUser object.
 
 ```
 try {
            parseUser.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(parseUser.has("firstName")){
            firstName = parseUser.getString("firstName");
        }else{
            firstName = "";
        }
        if(parseUser.has("lastName")){
            lastName = parseUser.getString("lastName");
        }else{
            lastName = "";
        }

        if (parseUser.has("imageUrl")) {
            imageUrl = parseUser.getString("imageUrl");
            Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(1).cornerRadiusDp(70).oval(false).build();
            Picasso.with(this).load(imageUrl).transform(transformation).fit().into(imgProfile);
        } else {
            Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidthDp(1).cornerRadiusDp(70).oval(false).build();
            Picasso.with(this).load(R.drawable.defaultprofile).transform(transformation).fit().into(imgProfile);
        }
  ``` 
  
