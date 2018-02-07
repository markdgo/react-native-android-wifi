# react-native-android-wifi

A react-native module for viewing and connecting to Wifi networks on Android devices.

![example app](/docs/example-app.gif)

### Installation

### Add it to your android project
```bash
npm install react-native-android-wifi --save
```

### Install the native dependencies
Use react-native link to install native dependencies automatically:
```bash
react-native link
```
or do it manually as described [here](docs/link-manually.md).

### Example usage

```javascript
import wifi from 'react-native-android-wifi';
```

Wifi connectivity status:
```javascript
wifi.isEnabled((isEnabled) => {
  if (isEnabled) {
    console.log("wifi service enabled");
  } else {
    console.log("wifi service is disabled");
  }
});
```

Enable/Disable wifi service:
```javascript
//Set TRUE to enable and FALSE to disable; 
wifi.setEnabled(true);
```

Sign device into a specific network:
> This method doesn't have a callback when connection succeeded, check [this](https://github.com/devstepbcn/react-native-android-wifi/issues/4) issue.

```javascript
//found returns true if ssid is in the range
wifi.findAndConnect(ssid, password, (found) => {
  if (found) {
    console.log("wifi is in range");
  } else {
    console.log("wifi is not in range");
  }
});
```

Disconnect current wifi network
```javascript
wifi.disconnect();
```

Get current SSID
```javascript
wifi.getSSID((ssid) => {
  console.log(ssid);
});
```

Get current BSSID
```javascript
wifi.getBSSID((bssid) => {
  console.log(bssid);
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
    console.log(wifiArray);
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
      console.log("is connected");
    } else {
      console.log("is not connected");
  }
});
```

Get connected wifi signal strength
```javascript
//level is the detected signal level in dBm, also known as the RSSI. (Remember its a negative value)
wifi.getCurrentSignalStrength((level) => {
  console.log(level);
});
```

Get connected wifi frequency
```javascript
wifi.getFrequency((frequency) => {
  console.log(frequency);
})
```

Get current IP
```javascript
//get the current network connection IP
wifi.getIP((ip) => {
  console.log(ip);
});
```

Remove/Forget the Wifi network from mobile by SSID, returns boolean
``` javascript
wifi.isRemoveWifiNetwork(ssid, (isRemoved) => {
  console.log("Forgetting the wifi device - " + ssid);
});
```

Starts native Android wifi network scanning and returns list
``` javascript
wifi.reScanAndLoadWifiList((wifiStringList) => {
  var wifiArray = JSON.parse(wifiStringList);
  console.log('Detected wifi networks - ',wifiArray);
});
```

Method to force wifi usage. Android by default sends all requests via mobile data if the connected wifi has no internet connection.
``` javascript
//Set true/false to enable/disable forceWifiUsage.
//Is important to enable only when communicating with the device via wifi
//and remember to disable it when disconnecting from device.
wifi.forceWifiUsage(true);
