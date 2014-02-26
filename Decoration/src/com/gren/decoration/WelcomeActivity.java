package com.gren.decoration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bitmapfun.util.Utils;
import com.gren.util.Constants;
import com.gren.util.CustomerHttpClient;


/*
 * intro 进入系统后的第一个界面，即欢迎动画页面
 * author lyl
 * modify 20130625 
 */


public class WelcomeActivity extends FragmentActivity {
	JSONObject JSONOdata ;
	private int newVerCode;          
	protected int currVerCode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userData = this.getSharedPreferences("userdata",0);
        boolean isFirstOpen = userData.getBoolean("is_first_open", true);
        if(isFirstOpen){
       	 Intent intent = new Intent(WelcomeActivity.this,FunctionIntroActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
			WelcomeActivity.this.finish();
        }
        
        new UploadTelInfoThread().start();
        
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				checkUpdate();
			}
			}, 1000);   //保证欢迎界面最少延迟800毫秒后，检查更新
    //      checkUpdate(); //检查应用更新
        
       
      //  new UploadTelInfoThread().start();  //每次登陆都给服务器发送信息，用于统计用户信息
        
     //   final DisplayMetrics displayMetrics = new DisplayMetrics();
     //   getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        
     //   final int height = displayMetrics.heightPixels;
     //   final int width = displayMetrics.widthPixels; 
        
    //	FrameLayout.LayoutParams lp =new FrameLayout.LayoutParams(width, (int)height);
		//lp.gravity = Gravity.CENTER;
		
		//img1为背景彩带
        
        ImageView img1 = new ImageView(this);
        //img1.setLayoutParams(lp);
        img1.setScaleType(ScaleType.CENTER_CROP);
        
        img1.setImageBitmap(DecorationApplication.bc.getBitmap(R.drawable.welcome, this));
        
        setContentView(img1);
        
//        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,1.0f); 
//        alphaAnimation.setDuration(1000);
//        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        alphaAnimation.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				// TODO Auto-generated method stub
//			
//			}
//		});
        
     //   alphaAnimation.setRepeatMode(Animation.REVERSE); 
//        alphaAnimation.setStartOffset(1000);
//        alphaAnimation.setFillAfter(true);
      //  alphaAnimation.setRepeatCount(1);
        
        
//        img1.startAnimation(alphaAnimation);
       
       
    }
    
	/**
	*在调用该方法时，也需构建一个Map对象
	*
	*/
	public  boolean sendGetRequest(String path, Map<String, String> params, String enc) throws Exception{
		StringBuilder sb = new StringBuilder(path);
		sb.append('?');
		// ?method=save&title=435435435&timelength=89&
		//把Map中的数据迭代附加到StringBuilder中
		for(Map.Entry<String, String> entry : params.entrySet()){
			//URLEncoder.encode对字符串中文进行编码，防止乱码
			if(entry.getValue() !=null){
				sb.append(entry.getKey()).append('=')
				.append(URLEncoder.encode(entry.getValue(), enc)).append('&');
			}		
		}
		//去掉最后一个字符&
		sb.deleteCharAt(sb.length()-1);
		//把组拼完的路径传到URL对象
		URL url = new URL(sb.toString());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//设置请求方式，GET要大写
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		//"200"代表请求成功3
		//Log.d("lyl","conn.getResponseCode() is "+ conn.getResponseCode());
		if(conn.getResponseCode()==200){
			if(Constants.DEBUG){
				Log.d("lyl","WelcomeActivity->uploadInfo done");
			}
			return true;
		}
		return false;
	}
    //========================================================================================
    
    private void checkUpdate() {
//    	this.getSupportLoaderManager().initLoader(11, null, checkVersionCallbacks);
    	 new Thread(checkUpdateThread).start();
    }
    
	Runnable checkUpdateThread = new Runnable(){

		@Override

		public void run() {
			
			String url = DecorationApplication.SERVER_NAME+"index.php?m=version";
			String response = CustomerHttpClient.post(url);
			if(Constants.DEBUG){
		        Log.d("lyl","check update response is " + response);
			}
			 Message m = new Message();
			 
       	    Bundle responseBundle = new Bundle();
       	    responseBundle.putString("response", response);
       	    m.setData(responseBundle);
       	    m.what = 0;
       	    readInfoHandler.sendMessage(m);
		}
	};
    private Handler readInfoHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            
            switch(msg.what){
            
            case 0:
                //关闭
            	currVerCode = getCurrVersionCode();
            	String response = msg.getData().getString("response");
            	
            	if(response==null||response.equals("error")){
            		StartMainActivity();
 					break;
            	}
            	JSONObject updateInfo;
				try {
					updateInfo = new JSONObject(response);
					if (! (updateInfo instanceof JSONObject) ) return;
					newVerCode = ((JSONObject) updateInfo).getInt("Version");
					
					if (currVerCode < newVerCode) {	
						showUpdateDialog(updateInfo);   //当前版本号小于服务器上的最新版本号，进行更新操作
					} else {
						StartMainActivity();
					}	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
            	
            	break;
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	//addIndexFragment();
    //	System.out.println("in result -----------------" + this.getClass().getName());
    	if(resultCode == 0){
    		this.finish();
    	}
    }
    
   
    DownLoadTask downloadTask;
    ProgressBar progressBar;
    TextView progressTxt;
 protected void onDestroy() {
	 super.onDestroy();
	 if(downloadTask!=null&&!downloadTask.isCancelled()){
		 downloadTask.cancel(true);
	 }
    	
    };
    private void showUpdateDialog(JSONObject data) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("软件更新");
		builder.setMessage("该软件有新的版本");
		
		String tempStr;
		try {
			tempStr = data.getString("Download");
			builder.setMessage(data.getString("UpdateLog"));
		} catch (JSONException e) {
			tempStr = null;
			e.printStackTrace();
		}
		
		final String apkUrl = tempStr;
		
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//isApproveDownload = true;
				showProgress();
				downloadTask = new DownLoadTask();
				downloadTask.execute(apkUrl);
				dialog.cancel();
			}
			
		});
		
		builder.setNegativeButton("暂不更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if (downloadTask != null) downloadTask.cancel(true);
				dialog.cancel();
				StartMainActivity();
			}
		});
		
		builder.create().show();
		
    }
    private void StartMainActivity(){
    	SharedPreferences userData = this.getSharedPreferences("userdata",0);
    	boolean isFirstOpen = userData.getBoolean("is_first_open", true);
    	
    	// Log.d("lyl","isFirstOpen is " + isFirstOpen);
    	 
    	 if(isFirstOpen){
    		 Intent intent = new Intent(WelcomeActivity.this,FunctionIntroActivity.class);
 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 			startActivity(intent);
 			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
 			WelcomeActivity.this.finish();
    	 }else{
    		 Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 			startActivity(intent);
 			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
 			WelcomeActivity.this.finish();
    	 }
    	
    }
    
    private void showProgress() {
    	
		this.setContentView(R.layout.progress_cui);
		progressBar = (ProgressBar) this.findViewById(R.id.progress);
		progressTxt = (TextView) this.findViewById(R.id.progressTxt);
    }
    
    private class DownLoadTask extends AsyncTask<String, Integer, File> {

		@Override
		protected File doInBackground(String... params) {
			String apkUrl = params[0];
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				int length = connection.getContentLength();
				InputStream inputStream = connection.getInputStream();
				
				File folder = getDiskDir(WelcomeActivity.this, "apk");
				
				if (!folder.exists()) {
					folder.mkdir();
				}
				
				String apkFileName = folder.getPath() + "/Decoration.apk";
				System.out.println("apkfile = " + apkFileName);
				
				File apkFile = new File(apkFileName);
				
				FileOutputStream outputStream = new FileOutputStream(apkFile);
				
				int count = 0;
				byte[] buffer = new byte[1024];
				
				do {
					int readedNum = inputStream.read(buffer);
					count += readedNum;
					
					if (readedNum <= 0 || isCancelled()) {
						break;
					}
					
					outputStream.write(buffer, 0, readedNum);
					publishProgress( (int)( ( (float)count/length ) * 100) );
						
				} while (true);
				
				outputStream.close();
				inputStream.close();
				
				return apkFile;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				//this.cancel(true);
				e.printStackTrace();
			} 
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressBar.setProgress(progress[0]);
			progressTxt.setText(String.valueOf(progress[0]) + "%");
	    }
		
		@Override  
        protected void onPostExecute(File result) {
			//progressAlert.dismiss();
			progressBar.setVisibility(View.GONE);
			progressTxt.setVisibility(View.GONE);
			if (result == null) return;
			installApk(result);
		}
		
		private void installApk(File apkFile) {
		//String test =	Environment.getExternalStorageDirectory().toString();
			Intent i = new Intent(Intent.ACTION_VIEW);
		//	 i.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),apkFile.toString())),"application/vnd.android.package-archive");
			i.setDataAndType(Uri.parse("file://" + apkFile.toString()),	"application/vnd.android.package-archive");
		//	WelcomeActivity.this.startActivity(i);
			
			WelcomeActivity.this.startActivityForResult(i, 0);
			
		}
		
		private File getDiskDir(Context context, String uniqueName) {
	        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
	        // otherwise use internal cache dir
	        final String cachePath =
	                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
	                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
	                                context.getCacheDir().getPath();

	        return new File(cachePath + File.separator + uniqueName);
	    }
		
		/**
	     * Check if external storage is built-in or removable.
	     *
	     * @return True if external storage is removable (like an SD card), false
	     *         otherwise.
	     */
	    @TargetApi(9)
	    public boolean isExternalStorageRemovable() {
	        if (Utils.hasGingerbread()) {
	            return Environment.isExternalStorageRemovable();
	        }
	        return true;
	    }

	    /**
	     * Get the external app cache directory.
	     *
	     * @param context The context to use
	     * @return The external cache dir
	     */
	    @TargetApi(8)
	    public File getExternalCacheDir(Context context) {
	        if (Utils.hasFroyo()) {
	            return context.getExternalCacheDir();
	        }

	        // Before Froyo we need to construct the external cache dir ourselves
	        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
	        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	    }
    	
    }
    
    private int getCurrVersionCode() {
    	String packageName = this.getPackageName();
    	PackageManager pm = this.getPackageManager();
    	try {
			PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.PERMISSION_GRANTED);
			return pi.versionCode;
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
    } 

    
    
    class UploadTelInfoThread extends Thread{
    	   public void run(){
    	   //你要实现的代码   
    		   
    		   TelephonyManager tm = (TelephonyManager) WelcomeActivity.this.getSystemService(Context.TELEPHONY_SERVICE);

    	        final DisplayMetrics displayMetrics = new DisplayMetrics();
    	        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    	        final int height = displayMetrics.heightPixels;
    	        final int width = displayMetrics.widthPixels; 
    				//通过Map构造器传参
    				Map<String, String> params = new HashMap<String, String>();
    				params.put("m", "lives");
    				params.put("member_id", "11");
    				params.put("types", "1");
    				params.put("os", "android");
    				params.put("cpu", android.os.Build.CPU_ABI);
    				params.put("model", android.os.Build.MODEL);
    				params.put("sysVersion", android.os.Build.VERSION.RELEASE);
    				params.put("board", android.os.Build.BOARD);
    				params.put("imei", tm.getDeviceId());
    				params.put("brand", android.os.Build.BRAND);
    				params.put("tel", tm.getLine1Number());
    				
    				params.put("screenWide", String.valueOf(width));
    				params.put("screenHigh", String.valueOf(height));
    				try {
    					//此处第三个参数，为指定上传数据编码
    				
    				    sendGetRequest(DecorationApplication.SERVER_NAME + "index.php", params, "UTF-8");
    					//提示成功
    				
    				} catch (Exception e) {
    					//提示失败
    					e.printStackTrace();
    				}
    		   
    	   }
    	   };
}
