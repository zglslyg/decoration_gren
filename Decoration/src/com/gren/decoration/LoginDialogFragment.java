package com.gren.decoration;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.gren.decoration.model.UserInfo;
import com.gren.util.CustomerHttpClient;


 
public class LoginDialogFragment extends DialogFragment{
	
	   private static LoginDialogFragment myDialogFragment;
	   
	   private View mTotalView;
	   private ImageView mBgImg;
	   private LinearLayout mLoginFrame;
	   private LinearLayout mRegFrame;
	   
	   private EditText mLoginUserName;
	   private EditText mLoginUserPwd;
	   
	   private EditText mRegUserName;
	   private EditText mRegUserPwd;
	   
	   private ProgressDialog mDialog;
	   private String responseMsg = "";
	   
	   //注册
	   private ImageView mJumpToReg;
	   private ImageView mRegPost;
	   private ImageView mRegPwdIcon;
	   
	   //登录
	   private ImageView mLoginPost;
	   
       public static LoginDialogFragment getInstance() {
		if(myDialogFragment == null){
			  myDialogFragment = new LoginDialogFragment();
		}
//		myDialogFragment.setStyle(myDialogFragment.STYLE_NO_FRAME, 0);
//		Bundle bundle = new Bundle();   
//		bundle.putInt("title", title);   
//		LoginDialogFragment.setArguments(bundle);   
		return myDialogFragment;   
	}
       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	 //  this.getActivity().getApplicationContext().set
    	mTotalView = inflater.inflate(R.layout.login_dialog, null);
    	mBgImg = (ImageView)mTotalView.findViewById(R.id.login_bg);
    	
    	//登陆背景
    	FrameLayout.LayoutParams framelp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(680), DecorationApplication.cpc.changeImageX(898));
    	framelp.gravity = Gravity.CENTER;
    	mBgImg.setLayoutParams(framelp);
    	
    	
        // 登陆框
    	mLoginFrame = (LinearLayout)mTotalView.findViewById(R.id.login_frame);
    	framelp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(580), DecorationApplication.cpc.changeImageX(600));
    	framelp.gravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;
    	framelp.topMargin = DecorationApplication.cpc.changeImageX(180);
    	mLoginFrame.setLayoutParams(framelp);
    	
    	mLoginUserName = (EditText)mTotalView.findViewById(R.id.username);
    	mLoginUserPwd = (EditText)mTotalView.findViewById(R.id.pwd);
    	mLoginPost = (ImageView)mTotalView.findViewById(R.id.user_login);
    	mLoginPost.setOnClickListener(loginOnClick);
    	
 	    mJumpToReg = (ImageView)mTotalView.findViewById(R.id.user_reg);
    	mJumpToReg.setOnClickListener(loginOnClick);
    	
    	//注册框
    	framelp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(580), DecorationApplication.cpc.changeImageX(620));
    	framelp.gravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;
    	framelp.topMargin = DecorationApplication.cpc.changeImageX(180);
    	mRegFrame  = (LinearLayout)mTotalView.findViewById(R.id.reg_frame);
    	mRegFrame.setLayoutParams(framelp);
    	
   
    	mRegUserName = (EditText)mTotalView.findViewById(R.id.regusername);
    	mRegUserPwd= (EditText)mTotalView.findViewById(R.id.regpwd);
    	
    	mRegPost = (ImageView)mTotalView.findViewById(R.id.user_reg_post);
    	mRegPost.setOnClickListener(loginOnClick);
    	
    	mRegPwdIcon =(ImageView)mTotalView.findViewById(R.id.regpwd_icon);
    	mRegPwdIcon.setOnClickListener(loginOnClick);

    	return mTotalView;
    //	return super.onCreateView(inflater, container, savedInstanceState);
    }
       OnClickListener loginOnClick = new OnClickListener() {
    	  boolean isChecked=false;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.user_reg:
				mRegFrame.setVisibility(View.VISIBLE);
				mLoginFrame.setVisibility(View.INVISIBLE);
				break;
			case R.id.regpwd_icon:
		    	if (isChecked) {    
	    	    // 显示密码     
		    		mRegUserPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);     
		    		isChecked =false;
		    	}   
	    	else {    
	    	    // 隐藏密码     
	    		mRegUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);    
	    		isChecked =true;
	    	}
				break;
				
			case R.id.user_login:
				if(mLoginUserName.getText().toString().equals("")||mLoginUserPwd.getText().toString().equals("")){
					Toast.makeText(LoginDialogFragment.this.getActivity(), "用户名或密码为空，请填写完整后再提交！", Toast.LENGTH_SHORT).show();
				    return;
				}
				mDialog = new ProgressDialog(LoginDialogFragment.this.getActivity());
				mDialog.setTitle("登陆");
				mDialog.setMessage("正在登陆服务器，请稍后...");
				mDialog.show();
				Thread loginThread = new Thread(new LoginThread());
				loginThread.start();
				
				break;
			case R.id.user_reg_post:
				String newusername = mRegUserName.getText().toString();				
				String newpassword = mRegUserPwd.getText().toString();
					
				mDialog = new ProgressDialog(LoginDialogFragment.this.getActivity());
				mDialog.setTitle("登陆");
				mDialog.setMessage("正在登陆服务器，请稍后...");
				mDialog.show();
				Thread registerThread = new Thread(new RegisterThread());
				registerThread.start();
				break;
			default:
				break;
			}
		}
	};
	
	 //LoginThread线程类
    class LoginThread implements Runnable
    {
		@Override
		public void run() {
			String username = mLoginUserName.getText().toString();
			String password = mLoginUserPwd.getText().toString();	
		//boolean checkstatus = sp.getBoolean("checkstatus", false);
	    	boolean loginValidate = loginServer(username, password);
	    	System.out.println("----------------------------bool is :"+loginValidate+"----------response:"+responseMsg);
	    	Message msg = mLoginHandler.obtainMessage();
	    	if(loginValidate)
	    	{	
	    		if(responseMsg.equals("-1")||responseMsg.equals("-2")){
	    			msg.what = 1;
		    		mLoginHandler.sendMessage(msg);
	    		}else {
	    			msg.what = 0;
		    		mLoginHandler.sendMessage(msg);
	    		}
	
	    	}else
	    	{
	    		msg.what = 2;
	    		mLoginHandler.sendMessage(msg);
	    	}
		}
    }
    
    private boolean loginServer(String username, String password)
    {
    	boolean loginValidate = false;
    	//使用apache HTTP客户端实现    	
    	String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=userLogin&api=1";    
    	try
    	{
    		//设置请求参数项
    	//	request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
    		
    		String response = CustomerHttpClient.post(urlStr,new BasicNameValuePair("username",username),new BasicNameValuePair("password",password));
    		if(response.equals("error")){
    			loginValidate=false;
    		}
    		else {
    			loginValidate = true;
    			responseMsg = response;
    		}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return loginValidate;
    }
    /*
     * 系统用户登陆的回调handler
     */
   //Handler
     Handler mLoginHandler = new Handler()
     {
     	public void handleMessage(Message msg)
     	{
     		switch(msg.what)
     		{
     		case 0:
     			mDialog.cancel();
     			DecorationApplication d = (DecorationApplication)LoginDialogFragment.this.getActivity().getApplication();
     			Editor editor = d.getPreference().edit();
     			
     			UserInfo.getInstance().setUserId(responseMsg);
 				editor.putString("userid", responseMsg);
 				editor.putBoolean("loginstatus", true);
 			//	editor.putInt("type", ACCOUNT_TYPE_SYS);
 				
 				editor.commit();
 				
// 				AccountFragment af = AccountFragment.getInstance();
// 				LoginDialogFragment.this.getActivity().getSupportFragmentManager()
// 				.beginTransaction().replace(R.id.center_frame, af).commit();
 				//switchFragment(af);
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "登录成功！", Toast.LENGTH_SHORT).show();
     			LoginDialogFragment.this.dismiss();
     			break;
     		case 1:
     			mDialog.cancel();
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
     			break;
     		case 2:
     			mDialog.cancel();
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "网络异常，请重试", Toast.LENGTH_SHORT).show();
     			break;
     		
     		}
     		
     	}
     };
     
     
     //RegisterThread线程类
     class RegisterThread implements Runnable
     {

 		@Override
 		public void run() {
 			String username = mRegUserName.getText().toString();
 		//	String password = md5(newPassword.getText().toString());
 			String password = mRegUserPwd.getText().toString();
 			//URL合法，但是这一步并不验证密码是否正确
 	    	boolean registerValidate = registerServer(username, password);
 	    	//System.out.println("----------------------------bool is :"+registerValidate+"----------response:"+responseMsg);
 	    	Message msg = reghandler.obtainMessage();
 	    	if(registerValidate)
 	    	{
 	    		if(responseMsg.equals("-1")||responseMsg.equals("-2")){
 	    			msg.what = 1;
 	    			reghandler.sendMessage(msg);
 	    		}else if(responseMsg.equals("-4")) {
 	    			msg.what = 2;
 	    			reghandler.sendMessage(msg);
 	    		}else if(responseMsg.equals("-3")) {
 	    			msg.what = 3;
 	    			reghandler.sendMessage(msg);
 	    		}else if(responseMsg.equals("-5")){
 	    			msg.what = 4;
 	    			reghandler.sendMessage(msg);
 	    		}else{
 	    			msg.what = 0;
 	    		 DecorationApplication d = (DecorationApplication)LoginDialogFragment.this.getActivity().getApplication();
 	    				Editor editor = d.getPreference().edit();
 						editor.putString("userid", responseMsg);
 					//	editor.putInt("type", ACCOUNT_TYPE_SYS);
 						editor.putBoolean("loginstatus", true);
 						editor.commit();
 						UserInfo.getInstance().setUserId(responseMsg);
 	    			reghandler.sendMessage(msg);
 	    		}
 	    	}else
 	    	{
 	    		msg.what = 4;
 	    		reghandler.sendMessage(msg);
 	    	}
 		}
     	
     }
     
     private boolean registerServer(String username, String password)
     {
     	boolean loginValidate = false;
     	//使用apache HTTP客户端实现
     	String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=userReg&api=1";	
     	HttpPost request = new HttpPost(urlStr);
     	TelephonyManager tm = (TelephonyManager) LoginDialogFragment.this.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
     	try
     	{   		
     		String response = CustomerHttpClient.post(urlStr,
     				new BasicNameValuePair("username",username),
     				new BasicNameValuePair("password",password),
     				new BasicNameValuePair("imei",tm.getDeviceId()));
     	//	Log.d("lyl","register response is " + response);
     		if(response.equals("error")){
     			loginValidate=false;
     		}
     		else {
     			loginValidate = true;
     		
     		//	User.userName  = username;
     		//	User.pwd = password;
     		//	User.id = response;
     			responseMsg = response;
     		}
     	}catch(Exception e)
     	{
     		e.printStackTrace();
     	}
     	return loginValidate;
     }
     Handler reghandler = new Handler()
     {
     	public void handleMessage(Message msg)
     	{
     		switch(msg.what)
     		{
     		case 0:
     			mDialog.cancel();
     		//	AccountFragment af = AccountFragment.getInstance();
 			//	LoginFragment.this.getActivity().getSupportFragmentManager()
 			//	.beginTransaction().replace(R.id.center_frame, af).commit();
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "注册成功,请设置个人资料", Toast.LENGTH_SHORT).show();
     		//	showDialog("注册成功！");
     			break;
     		case 1:
     			mDialog.cancel();
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "用户名不符合规则", Toast.LENGTH_SHORT).show();
     			break;
     		case 3:
     			mDialog.cancel();
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "用户名已经存在", Toast.LENGTH_SHORT).show();
     			break;
     		case 2:
     			mDialog.cancel();
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "密码长度不符合要求", Toast.LENGTH_SHORT).show();
     			break;
     		case 4:
     			mDialog.cancel();
     			Toast.makeText(LoginDialogFragment.this.getActivity(), "URL验证失败", Toast.LENGTH_SHORT).show();
     			break;
     		
     		}
     		
     	}
     };
}
