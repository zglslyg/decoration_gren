package com.gren.decoration;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.decoration.model.DecorationInfo;
import com.gren.util.AdaptationClass;
import com.gren.util.BitmapCache;
import com.gren.util.Constants;

public class DecorationApplication extends Application {

	public static final String TAG = "DecorationApplication";

	// public static ImageManager mImageManager;
//	public static String SERVER_NAME = "http://119.118.76.153/zhuangxiu/";
	public static String SERVER_NAME = "http://www.00up.com/zhuangxiu/";
	//public static String SERVER_NAME = "http://www.00up.com/zhuangxiu/";
	public static Context mContext;
	
	
	private SharedPreferences mSpUserData;
	
	public static ArrayList<DecorationInfo> decorationTimeList = new ArrayList<DecorationInfo>();
	
	public static ArrayList<DecorationInfo> decorationPriceList = new ArrayList<DecorationInfo>();
	
	public static ArrayList<DecorationInfo> decorationPraiseList = new ArrayList<DecorationInfo>();
	
	public static ArrayList<DecorationInfo> decorationCollectList = new ArrayList<DecorationInfo>();
	
	public static ArrayList<DecorationInfo> decorationHotList = new ArrayList<DecorationInfo>();
	
	public static AdaptationClass cpc;
	
	public static BitmapCache bc = BitmapCache.getInstance(); 

	public static int networkType = 0;//0表示无连接，1表示数据连接，2表示wifi连接
	
	public static final int NO_NETWORK_MODE = 0;
	public static final int NET_MOBLILE_MODE = 1;
	public static final int NET_WIFI_MODE = 2;
	
	//public static String className;
	
	public static final int pageNum = 10; //每次读取的商品数
	
	private ArrayList<ImageFetcher>  mImageFetcherList;
	
	public DecorationApplication() {
		// TODO Auto-generated constructor stub
		if(Constants.DEBUG){
			Log.d("lyl","decorationApplication init!");
		}
		 
		//判断是wifi还是网络链接
		
		
		
		//
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
	/*
	 * 判断网络连接类型
	 */
	 public static int checkNetworkConnection(Context context)
	    {
	        final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

	        final android.net.NetworkInfo wifi =connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        final android.net.NetworkInfo mobile =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	        
	        if(State.CONNECTED ==wifi.getState()){
	        	if(Constants.DEBUG){
	        		Log.d("lyl","checkNetworkConnection  wifi isAvailable "  + wifi.isAvailable());
	        		Log.d("lyl","checkNetworkConnection  wifi isConnected "  + (State.CONNECTED ==wifi.getState()));
	        	}
	        	return NET_WIFI_MODE;
	        }
	        ;
	        if(State.CONNECTED ==mobile.getState()){
	        	if(Constants.DEBUG){
	        		
	        		Log.d("lyl","checkNetworkConnection  mobile isAvailable "  + mobile.isAvailable());
	        		Log.d("lyl","checkNetworkConnection  mobile isConnected "  + (State.CONNECTED ==mobile.getState()));
	        	}
	        	return NET_MOBLILE_MODE;
	        }
	      //  NetworkInfo activeNetInfo = connMgr.getActiveNetworkInfo();//获取网络的连接情况  
	        if(Constants.DEBUG){
	        //	activeNetInfo.getType()==ConnectivityManager.TYPE_WIFI
        		Log.d("lyl","checkNetworkConnection is no network!");
        	}
	        return NO_NETWORK_MODE;  
	    }

	 public  SharedPreferences getPreference(){
		 return  mSpUserData;
	 }
	 public  void setPreference(SharedPreferences userPre){
		    mSpUserData = userPre;
	 }
	 public boolean checkIsLogin(){
		 boolean isLogin = false;
		 if(mSpUserData!=null){
			 isLogin = mSpUserData.getBoolean("loginstatus", false);
		 }
			return isLogin;
     }
	 
	 public void addImageFetcher(ImageFetcher imageFetcher){
		 if(mImageFetcherList==null){
			 mImageFetcherList = new ArrayList<ImageFetcher>();
		 } 
		 mImageFetcherList.add(imageFetcher);
	 }
	 public ArrayList<ImageFetcher> getImageFetcher(){
		 return mImageFetcherList;
	 }
	 
	
}
