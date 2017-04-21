# Method Tracking

# ![building](https://travis-ci.org/CoXier/AppMethodTracking.svg?branch=master)

A plugin to display how methods are called in application.

[Live Demo](http://coxier.cn/AppMethodTracking.html)

# Usage

## Stpe1
Add these to your root `build.gradle`.

```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.uniquestudio:method-tracking:1.0.6"
  }
}
```
Don't forget :

```groovy
apply plugin: "com.uniquestudio.method-tracking"
```

## Step2
First find where you want to debug.In my demo I care about how to wear cloth,so I add two lines around `wearCloth` like this:

```groovy
Debug.startMethodTracing(getPackageName());
wearCloth();
Debug.stopMethodTracing();

public void wearCloth() {
    putOnCoat();
    putOnPants();
    putOnShoes();
    putOnHat();
}


private void putOnPants() {

}

private void putOnCoat() {

}

private void putOnHat() {
    // Put on hat
}


public void putOnShoes() {
    putOnSocks();
    // Put on shoes
}

public void putOnSocks() {
    // Put on socks
}
```
## Step3
Now let's start configure this plugin.In your app module, add `method-tracking` closure.

```groovy
methodTracking{
    traceName = 'com.hackerli.sample.trace'
    filterList = ['com.hackerli.sample']
}
```
* `traceName` is from the method `Debug.startMethodTracing(traceName)`
* `filterList` is used to filter some extra methods.We usually use package name.

## Step4
```groovy
./gradlew track
```
After above steps,[traceName.html](http://coxier.cn/AppMethodTracking.html) is created.

Its output like this:

<img src="/screenshot/s_0.png"/>

The digit is `useces`.


In html,I add click event that means you can toggle these elements that have children.For example if I click `com.hackerli.sample.MainActivity.putOnShoes` above line,I will get:

<img src="/screenshot/s_1.png"/>


# THANKS
I am inspired by [AppMethodOrder](https://github.com/zjw-swun/AppMethodOrder),espically how to compatible Windows os in groovy script or gradle script.

# LICENSE
```
Copyright 2017 coxier.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
```
