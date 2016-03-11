# react-native-android-wifi

A react-native module for viewing and connecting to Wifi networks on Android devices.

### Installation

```bash
npm install react-native-wifi-module --save
```

### Add it to your android project

* In `android/setting.gradle`

```gradle
...
include ':AndroidWifiModule', ':app'
project(':AndroidWifiModule').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-wifi')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':AndroidWifiModule')
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

```
var wifi = require('react-native-android-wifi')
```

Wifi connectivity status:
```
wifi.isEnabled((isEnabled)=>{
  if (isEnabled){
    this.setState({status:"wifi service enabled"});
  }else{
    this.setState({status:"wifi service is disabled"});
  }
});
```

Enable/Disable wifi connectivity:
```
//Set TRUE to enable and FALSE to disable; 
wifi.setEnabled(true);
```

Toast all networks:
```
wifi.toastAllNetworks();
```

Sign device into a specific network:
```
wifi.findAndConnect(ssid, password);
```

You can put all wifi networks into a ListView like this:
```
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
```
wifi.connectionStatus((isConnected) => {
  if (isConnected) {
    //Do something
  }
},
```

