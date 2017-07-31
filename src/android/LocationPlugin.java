package com.location.yhck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationPlugin extends CordovaPlugin{
  private CallbackContext mCallbackContext;
  public static final String LOCATION_TYPE= "locationType";
  public static final String LONFITUDE= "LONFITUDE";
  public static final String LATITUDE= "LATITUDE";
  public static final String LOCATION_ACTION = "locationAction";

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
    this.mCallbackContext = callbackContext;
    if("getLocation".equals(action)){
        setService();
    }
    if (!"".equals(action)||action!=null) {

      return true;
    }
    mCallbackContext.error("error");
    return false;

  }

  public void setService() {
    // 注册广播
    IntentFilter filter = new IntentFilter();
    filter.addAction(LOCATION_ACTION);
    cordova.getActivity().registerReceiver(new LocationBroadcastReceiver(), filter);

    // 启动服务
    Intent intent = new Intent();
    intent.setClass(cordova.getActivity(), LocationSvc.class);
    cordova.getActivity().startService(intent);
  }

  private class LocationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (!intent.getAction().equals(LOCATION_ACTION)) return;
      JSONObject resultJson = new JSONObject();
      try {
        resultJson.put("LONFITUDE",intent.getStringExtra(LONFITUDE));
        resultJson.put("LATITUDE",intent.getStringExtra(LATITUDE));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      cordova.getActivity().unregisterReceiver(this);// 注销服务
      mCallbackContext.success(resultJson);

    }
  }
}
