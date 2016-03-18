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

Toast all networks:
```javascript
wifi.toastAllNetworks();
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

You can put all wifi networks into a ListView like this:
```javascript
wifi.loadWifiList((wifiString) => {
var wifiArray = wifiString.split('SSID:');
this.setState({
  dataSource: this.state.dataSource.cloneWithRows(wifiArray),
     loaded: true,
     });
 },
 (msg) => {
     console.log(msg);
   },
 );
 ```

connectionStatus returns true or false depending on whether device is connected to wifi:
```javascript
wifi.connectionStatus((isConnected) => {
  if (isConnected) {
    //Do something
  }
},
```

