package com.devstepbcn.wifi;

import com.facebook.react.uimanager.*;
import com.facebook.react.bridge.*;
import com.facebook.systrace.Systrace;
import com.facebook.systrace.SystraceMessage;
import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AndroidWifiModule extends ReactContextBaseJavaModule {

	//WifiManager Instance
	WifiManager wifi;

 	//Constructor
	public AndroidWifiModule(ReactApplicationContext reactContext) {
		super(reactContext);
		wifi = (WifiManager)reactContext.getSystemService(Context.WIFI_SERVICE);
	}

  	//Name for module register to use:
  	@Override
	public String getName() {
		return "AndroidWifiModule";
	}

	//Method to load wifi list into string via Callback. Returns a stringified JSONArray
  	@ReactMethod
	public void loadWifiList(Callback successCallback, Callback errorCallback) {
		try {
			List < ScanResult > results = wifi.getScanResults();
			JSONArray wifiArray = new JSONArray();

			for (ScanResult result: results) {
				JSONObject wifiObject = new JSONObject();
				if(!result.SSID.equals("")){
					try {
					    wifiObject.put("SSID", result.SSID);
					    wifiObject.put("BSSID", result.BSSID);
					    wifiObject.put("capabilities", result.capabilities);
					    wifiObject.put("frequency", result.frequency);
					    wifiObject.put("level", result.level);
					    wifiObject.put("timestamp", result.timestamp);
					    //Other fields not added
					    //wifiObject.put("operatorFriendlyName", result.operatorFriendlyName);
					    //wifiObject.put("venueName", result.venueName);
					    //wifiObject.put("centerFreq0", result.centerFreq0);
					    //wifiObject.put("centerFreq1", result.centerFreq1);
					    //wifiObject.put("channelWidth", result.channelWidth);
					} catch (JSONException e) {
					    errorCallback.invoke(e.getMessage());
					}
					wifiArray.put(wifiObject);
				}
			}
			successCallback.invoke(wifiArray.toString());
		} catch (IllegalViewOperationException e) {
			errorCallback.invoke(e.getMessage());
		}
	}

	//Method to check if wifi is enabled
	@ReactMethod
	public void isEnabled(Callback isEnabled) {
		isEnabled.invoke(wifi.isWifiEnabled());
	}

	//Method to connect/disconnect wifi service
	@ReactMethod
	public void setEnabled(Boolean enabled) {
		wifi.setWifiEnabled(enabled);
	}

	//Send the ssid and password of a Wifi network into this to connect to the network.
	//Example:  wifi.findAndConnect(ssid, password);
	//After 10 seconds, a post telling you whether you are connected will pop up.
	//Callback returns true if ssid is in the range
 	@ReactMethod
	public void findAndConnect(String ssid, String password, Callback ssidFound) {
		List < ScanResult > results = wifi.getScanResults();
		Boolean found = false;
		for (ScanResult result: results) {
			String resultString = "" + result.SSID;
			if (ssid.equals(resultString)) {
				found=true;
				connectTo(result, password, ssid);
			}
		}
		ssidFound.invoke(found);
	}

	//Use this method to check if the device is currently connected to Wifi.
	@ReactMethod
	public void connectionStatus(Callback connectionStatusResult) {
		ConnectivityManager connManager = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi.isConnected()) {
			connectionStatusResult.invoke(true);
		}
		if (!mWifi.isConnected()) {
			connectionStatusResult.invoke(false);
		}
	}

	//Method to connect to WIFI Network
	public void connectTo(ScanResult result, String password, String ssid) {
		//Make new configuration
		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + ssid + "\"";
		String Capabilities = result.capabilities;
		if (Capabilities.contains("WPA2")) {
			conf.preSharedKey = "\"" + password + "\"";
		} else if (Capabilities.contains("WPA")) {
			conf.preSharedKey = "\"" + password + "\"";
		} else if (Capabilities.contains("WEP")) {
			conf.wepKeys[0] = "\"" + password + "\"";
			conf.wepTxKeyIndex = 0;
			conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		} else {
			conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		}
		//Remove the existing configuration for this netwrok
		List<WifiConfiguration> mWifiConfigList = wifi.getConfiguredNetworks();
		String comparableSSID = ('"' + ssid + '"'); //Add quotes because wifiConfig.SSID has them
		for(WifiConfiguration wifiConfig : mWifiConfigList){
			if(wifiConfig.SSID.equals(comparableSSID)){
				int networkId = wifiConfig.networkId;
			  wifi.removeNetwork(networkId);
				wifi.saveConfiguration();
			}
		}
		//Add configuration to Android wifi manager settings...
     	wifi.addNetwork(conf);

		//Enable it so that android can connect
		List < WifiConfiguration > list = wifi.getConfiguredNetworks();
		for (WifiConfiguration i: list) {
			if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
				wifi.disconnect();
				wifi.enableNetwork(i.networkId, true);
				wifi.reconnect();
				break;
			}
		}
	}

	//This method will return current ssid
	@ReactMethod
	public void getSSID(final Callback callback) {
	    WifiInfo info = wifi.getConnectionInfo();

	    // This value should be wrapped in double quotes, so we need to unwrap it.
	    String ssid = info.getSSID();
	    if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
	      ssid = ssid.substring(1, ssid.length() - 1);
	    }

	    callback.invoke(ssid);
  	}

    //This method will return the basic service set identifier (BSSID) of the current access point
    @ReactMethod
    public void getBSSID(final Callback callback) {
        WifiInfo info = wifi.getConnectionInfo();

        String bssid = info.getBSSID();

        callback.invoke(bssid.toUpperCase());
    }

  	//This method will return current wifi signal strength
  	@ReactMethod
	public void getCurrentSignalStrength(final Callback callback) {
  		int linkSpeed = wifi.getConnectionInfo().getRssi();
		callback.invoke(linkSpeed);
	}
}
