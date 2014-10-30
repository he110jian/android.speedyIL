package com.example.speedyil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WiFi{
	private String MaxAp;
	private WifiManager mainWifi;
	private WifiReceiver receiverWifi;
	private List<ScanResult> wifiList;
	private List<Ap> apList;
	private HashMap<String, ApInfo> apinfoList;
	private ProgressDialog dialog;
	private Context context;
	private myView view;
	PointF cPos;
	private float width, height;
	
	WiFi(Context context, myView myView){
		this.context = context;
		view = myView;
		apList = new ArrayList<Ap>();
		cPos = new PointF();
		while(mainWifi == null){
			mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		receiverWifi = new WifiReceiver();
	}

	public void setApinfoList(HashMap<String, ApInfo> apinfoList) {
		this.apinfoList = apinfoList;
	}
	
	public void OpenWifi()
	{
		if (!mainWifi.isWifiEnabled())
		{
			mainWifi.setWifiEnabled(true);
		}
	}

	public void logoff()
	{
		context.unregisterReceiver(receiverWifi);// 注销广播
	}
	
	public void logon()
	{
		context.registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));// 注册广播
	}
	
	public void CloseWifi()
	{
		if (mainWifi.isWifiEnabled())
		{
			mainWifi.setWifiEnabled(false);
		}
	}

	public void scanWifi()//context = MainActivity.this
	{
		OpenWifi();
		context.registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));// 注册广播
		mainWifi.startScan();
		dialog = ProgressDialog.show(context, "", "正在定位,请稍候");
		dialog.setCancelable(true);
	}
	
	class WifiReceiver extends BroadcastReceiver
	{
		public void onReceive(Context context, Intent intent)
		{
				wifiList = mainWifi.getScanResults();
				dialog.dismiss();
				apList.clear();
				String mac;
				int level;
				for (int i = 0; i < wifiList.size(); i++) {
					if(wifiList.get(i).SSID.equals("seu-wlan"))
					{
						mac = wifiList.get(i).BSSID;
						level = wifiList.get(i).level;
						Ap ap = new Ap(mac,level);
						apList.add(ap);
					}
				}
				if(!apList.isEmpty())
				{
					MaxAp = Max(apList);
					Toast.makeText(context, MaxAp, Toast.LENGTH_LONG).show();
					Location(MaxAp);
				}
				else{
					view.setCircle(0,0,0);
					Toast.makeText(context, "无法获取您的位置", Toast.LENGTH_LONG).show();
				}
		}
	}
	public void Location(String mac){
		cPos = view.getPos();
		float scale = view.getScale();
		width = view.getBmpWidth()*scale;
		height = view.getBmpHeight()*scale;
		if(apinfoList.containsKey(mac))
		{
			ApInfo temp = new ApInfo();
			temp = apinfoList.get(mac);
			view.setCircle((cPos.x + temp.getDx()*width), (cPos.y+temp.getDy()*height), temp.getRange()*width);
		}
		else{
			view.setCircle(0,0,0);
			Toast.makeText(context, "无法获取您的位置", Toast.LENGTH_LONG).show();
		}
	}
	
	public String Max(List<Ap> apList) {
		// TODO Auto-generated method stub
		int max = apList.get(0).getLevel();
		String maxMac = apList.get(0).getMac();
		int temp = 0;
		for(int i = 1;i < apList.size(); i ++){
			temp = apList.get(i).getLevel();
			if(max<temp)
			{
				max = temp;
				maxMac = apList.get(i).getMac();
			}
		}
		return maxMac;
	}

}
