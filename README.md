# react-native-android-wifi

A react-native module for viewing and connecting to Wifi networks on Android devices. 
Based on ccmxy/react-native-wifi-module

### Installation

```bash
npm install react-native-android-wifi --save
```

### Add it to your android project

* In `android/setting.gradle`

```gradle
...
include ':AndroidWifi', ':app'
project(':AndroidWifi').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-wifi')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':AndroidWifi')
}
```

* register module (in MainActivity.java)

On newer versions of React Native (0.18+):

```java
import com.devstepbcn.wifi.AndroidWifiPackage;  // <--- import

public class MainActivity extends ReactActivity {
  ......
  
  /**
   * A list of packages used by the app. If the app uses additional views
   * or modules besides the default ones, add more packages here.
   */
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new AndroidWifiPackage(), // <------ add here
        new MainReactPackage());
    }
}
```

On older versions of React Native:

```java
import com.devstepbcn.wifi.AndroidWifiPackage;  // <--- import

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
  ......

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new AndroidWifiPackage())              // <------ add here
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "ExampleRN", null);

    setContentView(mReactRootView);
  }

  ......

}
```

### Example usage

```javascript
var wifi = require('react-native-android-wifi')
```

Wifi connectivity status:
```javascript
wifi.isEnabled((isEnabled)=>{
  if (isEnabled){
    this.setState({status:"wifi service enabled"});
  }else{
    this.setState({status:"wifi service is disabled"});
  }
});
```

Enable/Disable wifi service:
```javascript
//Set TRUE to enable and FALSE to disable; 
wifi.setEnabled(true);
```

Sign device into a specific network:
```javascript
//found returns true if ssid is in the range
wifi.findAndConnect(ssid, password, (found) => {
  if (found) {
    this.setState({wifiInRange:true});
  }else{
    this.setState({wifiInRange:false});
  }
});
```

Get current SSID
```javascript
wifi.getSSID((ssid) => {
    this.setState({ssid:ssid});
});
```

Get all wifi networks in range
```javascript
/*
wifiStringList is a stringified JSONArray with the following fields for each scanned wifi
{
  "SSID": "The network name",
  "BSSID": "The address of the access point",
  "capabilities": "Describes the authentication, key management, and encryption schemes supported by the access point"
  "frequency":"The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating with the access point",
  "level":"The detected signal level in dBm, also known as the RSSI. (Remember its a negative value)",
  "timestamp":"Timestamp in microseconds (since boot) when this result was last seen"
}
*/
wifi.loadWifiList((wifiStringList) => {
    var wifiArray = JSON.parse(wifiStringList);
  },
  (error) => {
    console.log(error);
  }
);
 ```

connectionStatus returns true or false depending on whether device is connected to wifi
```javascript
wifi.connectionStatus((isConnected) => {
  if (isConnected) {
    //Do something
  }
});
```

Get connected wifi signal strength
```javascript
//level is the detected signal level in dBm, also known as the RSSI. (Remember its a negative value)
wifi.getCurrentSignalStrength((level)=>{
  console.log(level);
});

```
