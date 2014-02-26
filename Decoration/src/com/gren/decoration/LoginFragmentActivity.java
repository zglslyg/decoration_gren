package com.gren.decoration;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gren.decoration.model.UserInfo;
import com.gren.util.CustomerHttpClient;

public class LoginFragmentActivity extends FragmentActivity{

	private FrameLayout mTitleFrame;
	private FrameLayout mLoginFrame;
	private FrameLayout mRegFrame;
	
	private EditText mLoginUserName;
    private EditText mLoginUserPwd;
    
    private EditText mRegUserName;
    private EditText mRegUserPwd;
    
    private TextView mTitle;
    
    private ProgressDialog mDialog;
	private String responseMsg = "";
	
    //注册
    private TextView mJumpToReg;
    private TextView mRegPost;
    private ImageView mRegPwdIcon;
    //登录
    private TextView mLoginPost;
    
    private ViewPager mViewPager;
    private TextView mOrderComment;
    private TextView mOrderPraise;
    
    private loginPageAdapter mMyPagerAdapter;
    
    private boolean isDirectIn;
    private boolean isRegShow;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		isRegShow = false;
		setContentView(R.layout.login_fragment);
		isDirectIn = getIntent().getBooleanExtra("is_directin", false);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		initView();
	}
	private void initView(){
		mTitle = (TextView)findViewById(R.id.title);
		mLoginFrame = (FrameLayout)findViewById(R.id.login_frame);
		
	 
		LinearLayout.LayoutParams LinearLp;
		LinearLp = new LinearLayout.LayoutParams(
					DecorationApplication.cpc.changeImageX(800),
					DecorationApplication.cpc.changeImageX(581));
			
		mLoginFrame.setLayoutParams(LinearLp);
		
		LinearLayout loginTopBg = (LinearLayout)findViewById(R.id.login_top_bg);
		LinearLp = new LinearLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(800),
				DecorationApplication.cpc.changeImageX(694));
		
		loginTopBg.setLayoutParams(LinearLp);
		
		//上部红色背景
		ImageView topbgImg = (ImageView)findViewById(R.id.top_bg);
		FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(802),
				DecorationApplication.cpc.changeImageX(113));
		topbgImg.setLayoutParams(frameLp);
		
		
		//返回按钮
//		ImageView retImg = (ImageView)findViewById(R.id.top_return);
//		frameLp = new FrameLayout.LayoutParams(
//				DecorationApplication.cpc.changeImageX(34),
//				DecorationApplication.cpc.changeImageX(51));
//		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
//		
//		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(45);
//		
//		retImg.setLayoutParams(frameLp);
//		
//		retImg.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				LoginFragmentActivity.this.finish();
//				LoginFragmentActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//			}
//		});
		
		
		 //返回图标
		 ImageView topReturn = (ImageView)findViewById(R.id.top_return);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(32);
		 topReturn.setLayoutParams(frameLp);
		 
		 topReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	LoginFragmentActivity.this.finish();
			//	LoginFragmentActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				if(isRegShow){
					mRegFrame.setVisibility(View.GONE);
					mLoginFrame.setVisibility(View.VISIBLE);
					mTitle.setText("登陆");
					isRegShow = false;
				}else{
					if(isDirectIn){
						 Intent intent = new Intent(LoginFragmentActivity.this,MainActivity.class);
				 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 			startActivity(intent);
				 			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out); 
				 			LoginFragmentActivity.this.finish();
					}else{
						LoginFragmentActivity.this.finish();
						LoginFragmentActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
					}
				}
				
				//	PersonalActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
		 
		 TextView  topReturnTxt = (TextView)findViewById(R.id.top_return_txt);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		 topReturnTxt.setLayoutParams(frameLp);
		
		//登陆背景
	//	ImageView loginbgImg = (ImageView)findViewById(R.id.login_bg);
	//	frameLp = new FrameLayout.LayoutParams(
	//			DecorationApplication.cpc.changeImageX(800),
	//			DecorationApplication.cpc.changeImageX(581));
	//	loginbgImg.setLayoutParams(frameLp);
		
		LinearLayout userbg = (LinearLayout)findViewById(R.id.user_bg);
		frameLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(657),
				DecorationApplication.cpc.changeImageX(97));
		frameLp.gravity = Gravity.LEFT|Gravity.TOP;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		frameLp.topMargin = DecorationApplication.cpc.changeImageX(62);
		userbg.setLayoutParams(frameLp);
		
		mLoginUserName = (EditText)findViewById(R.id.username_edit);
		mLoginUserPwd  = (EditText)findViewById(R.id.pwd_edit);
		
		
		LinearLayout pwdbg = (LinearLayout)findViewById(R.id.pwd_bg);
		frameLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(657),
				DecorationApplication.cpc.changeImageX(97));
		frameLp.gravity = Gravity.LEFT|Gravity.TOP;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		frameLp.topMargin = DecorationApplication.cpc.changeImageX(236);
		
		pwdbg.setLayoutParams(frameLp);
		
		
		
		mLoginPost = (TextView)findViewById(R.id.login_post);
		frameLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(270),
				DecorationApplication.cpc.changeImageX(94));
		frameLp.gravity = Gravity.LEFT|Gravity.TOP;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		frameLp.topMargin = DecorationApplication.cpc.changeImageX(393);
		
		mLoginPost.setLayoutParams(frameLp);
		
		mLoginPost.setOnClickListener(loginOnClick);
		
		mJumpToReg = (TextView)findViewById(R.id.jumptoreg);
		frameLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(270),
				DecorationApplication.cpc.changeImageX(94));
		frameLp.gravity = Gravity.LEFT|Gravity.TOP;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(460);
		frameLp.topMargin = DecorationApplication.cpc.changeImageX(393);
		
		mJumpToReg.setLayoutParams(frameLp);
		
		mJumpToReg.setOnClickListener(loginOnClick);
		
		
		initRegView();
		
		mViewPager = (ViewPager)findViewById(R.id.vPager);
		
	//	LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(800),
	//			DecorationApplication.cpc.changeImageX(657));
	//	mViewPager.setLayoutParams(linearLp);
		
		mViewPager.setOffscreenPageLimit(1);
		
		mOrderComment = (TextView)findViewById(R.id.order_comment);
	
		mOrderPraise = (TextView)findViewById(R.id.order_praise);
		
		mOrderComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1, true);
			}
		});
		
		mOrderPraise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0,true);
			}
		});
		
		
		mMyPagerAdapter = new loginPageAdapter();
		
		mViewPager.setAdapter(mMyPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					mOrderPraise.setBackgroundResource(R.drawable.main_category_txtbg);
					mOrderComment.setBackgroundColor(Color.TRANSPARENT);

					mOrderComment.setTextColor(Color.parseColor("#604646"));
					mOrderPraise.setTextColor(Color.parseColor("#ffffff"));
					break;
				case 1:
					mOrderComment.setBackgroundResource(R.drawable.main_category_txtbg);
					mOrderPraise.setBackgroundColor(Color.TRANSPARENT);
				
					mOrderComment.setTextColor(Color.parseColor("#ffffff"));
					mOrderPraise.setTextColor(Color.parseColor("#604646"));
					break;
				
				default: 
					break;
				}
			}
		});
		
		
	}
	
	public void initRegView(){
		mRegFrame = (FrameLayout)findViewById(R.id.reg_frame);
	//	ImageView regbgImg = (ImageView)findViewById(R.id.reg_bg);
		FrameLayout.LayoutParams framelayoutLp;
	//	framelayoutLp = new FrameLayout.LayoutParams(
	//			DecorationApplication.cpc.changeImageX(800),
	//			DecorationApplication.cpc.changeImageX(581));
	//	regbgImg.setLayoutParams(framelayoutLp);
		LinearLayout.LayoutParams LinearLp;
		LinearLp = new LinearLayout.LayoutParams(
					DecorationApplication.cpc.changeImageX(800),
					DecorationApplication.cpc.changeImageX(581));
			
		mRegFrame.setLayoutParams(LinearLp);
		
		LinearLayout regUserbg = (LinearLayout)findViewById(R.id.reg_user_bg);
		framelayoutLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(657),
				DecorationApplication.cpc.changeImageX(97));
		framelayoutLp.gravity = Gravity.LEFT|Gravity.TOP;
		framelayoutLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		framelayoutLp.topMargin = DecorationApplication.cpc.changeImageX(62);
		regUserbg.setLayoutParams(framelayoutLp);
		
		mRegUserName = (EditText)findViewById(R.id.reg_username_edit);
		mRegUserPwd  = (EditText)findViewById(R.id.reg_pwd_edit);
		
		
		LinearLayout regpwdbg = (LinearLayout)findViewById(R.id.reg_pwd_bg);
		framelayoutLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(657),
				DecorationApplication.cpc.changeImageX(97));
		framelayoutLp.gravity = Gravity.LEFT|Gravity.TOP;
		framelayoutLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		framelayoutLp.topMargin = DecorationApplication.cpc.changeImageX(236);
		
		regpwdbg.setLayoutParams(framelayoutLp);
		
		
		
		mRegPost = (TextView)findViewById(R.id.reg_post);
		framelayoutLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(270),
				DecorationApplication.cpc.changeImageX(94));
		framelayoutLp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.TOP;
		framelayoutLp.topMargin = DecorationApplication.cpc.changeImageX(393);
		
		mRegPost.setLayoutParams(framelayoutLp);
		
		mRegPost.setOnClickListener(loginOnClick);
		
		mRegPwdIcon = (ImageView)findViewById(R.id.reg_pwd_visible);
		
		framelayoutLp = new FrameLayout.LayoutParams(
				DecorationApplication.cpc.changeImageX(60),
				DecorationApplication.cpc.changeImageX(39));
		framelayoutLp.gravity = Gravity.LEFT|Gravity.TOP;
		framelayoutLp.leftMargin = DecorationApplication.cpc.changeImageX(630);
		framelayoutLp.topMargin = DecorationApplication.cpc.changeImageX(375);
		
		mRegPwdIcon.setLayoutParams(framelayoutLp);
		mRegPwdIcon.setOnClickListener(loginOnClick);
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(isRegShow){
			mRegFrame.setVisibility(View.GONE);
			mLoginFrame.setVisibility(View.VISIBLE);
			mTitle.setText("登陆");
			isRegShow = false;
		}else{
			super.onBackPressed();
			if(isDirectIn){
				 Intent intent = new Intent(LoginFragmentActivity.this,MainActivity.class);
		 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 			startActivity(intent);
		 			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out); 
		 			LoginFragmentActivity.this.finish();
			}else{
				LoginFragmentActivity.this.finish();
				LoginFragmentActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		}
		
		
	    
	}
	
	  OnClickListener loginOnClick = new OnClickListener() {
    	  boolean isChecked=false;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.jumptoreg:
				mRegFrame.setVisibility(View.VISIBLE);
				mLoginFrame.setVisibility(View.GONE);
				mTitle.setText("注册");
				isRegShow = true;
				break;
			case R.id.reg_pwd_visible:
		    	if (isChecked) {    
	    	    // 显示密码     
		    		mRegUserPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);   
		    		mRegPwdIcon.setImageBitmap(DecorationApplication.bc.readBitMap(LoginFragmentActivity.this, 
		    				R.drawable.login_showpwd, null));
		    		isChecked =false;
		    	}   
	    	else {    
	    	    // 隐藏密码     
	    		mRegUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);  
	    		mRegPwdIcon.setImageBitmap(DecorationApplication.bc.readBitMap(LoginFragmentActivity.this, 
	    				R.drawable.login_hidepwd, null));
	    		isChecked =true;
	    	}
				break;
				
			case R.id.login_post:
				if(mLoginUserName.getText().toString().equals("")||mLoginUserPwd.getText().toString().equals("")){
					Toast.makeText(LoginFragmentActivity.this, "用户名或密码为空，请填写完整后再提交！", Toast.LENGTH_SHORT).show();
				    return;
				}
				mDialog = new ProgressDialog(LoginFragmentActivity.this);
				mDialog.setTitle("登陆");
				mDialog.setMessage("正在登陆服务器，请稍后...");
				mDialog.show();
				Thread loginThread = new Thread(new LoginThread());
				loginThread.start();
				
				break;
			case R.id.reg_post:
				String newusername = mRegUserName.getText().toString();				
				String newpassword = mRegUserPwd.getText().toString();
					
				mDialog = new ProgressDialog(LoginFragmentActivity.this);
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
	    		 DecorationApplication d = (DecorationApplication)LoginFragmentActivity.this.getApplication();
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
    	TelephonyManager tm = (TelephonyManager) LoginFragmentActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
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
    			
    			Toast.makeText(LoginFragmentActivity.this, "注册成功,请设置个人资料", Toast.LENGTH_SHORT).show();
    		//	showDialog("注册成功！");
    			if(isDirectIn){
    				
    			}else{
    				DMenuFragment.getInstance().setAvatar();
    			}
    			LoginFragmentActivity.this.finish();
    			 
     			Intent intent = new Intent(LoginFragmentActivity.this, PersonalActivity.class);
     			
     			intent.putExtra("is_directin", true);
     			
				startActivity(intent);
				LoginFragmentActivity.this.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    			break;
    		case 1:
    			mDialog.cancel();
    			Toast.makeText(LoginFragmentActivity.this, "用户名不符合规则", Toast.LENGTH_SHORT).show();
    			break;
    		case 3:
    			mDialog.cancel();
    			Toast.makeText(LoginFragmentActivity.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
    			break;
    		case 2:
    			mDialog.cancel();
    			Toast.makeText(LoginFragmentActivity.this, "密码长度不符合要求", Toast.LENGTH_SHORT).show();
    			break;
    		case 4:
    			mDialog.cancel();
    			Toast.makeText(LoginFragmentActivity.this, "URL验证失败", Toast.LENGTH_SHORT).show();
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
     			DecorationApplication d = (DecorationApplication)LoginFragmentActivity.this.getApplication();
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
     			Toast.makeText(LoginFragmentActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
     			//LoginDialogFragment.this.dismiss();
     			if(isDirectIn){
     				
     			}else{
     				DMenuFragment.getInstance().setAvatar();
     			}
     			
     			LoginFragmentActivity.this.finish();
     			Intent intent = new Intent(LoginFragmentActivity.this, PersonalActivity.class);
     			intent.putExtra("is_directin", true);
				startActivity(intent);
			
				LoginFragmentActivity.this.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
     			break;
     		case 1:
     			mDialog.cancel();
     			Toast.makeText(LoginFragmentActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
     			break;
     		case 2:
     			mDialog.cancel();
     			Toast.makeText(LoginFragmentActivity.this, "网络异常，请重试", Toast.LENGTH_SHORT).show();
     			break;
     		
     		}
     		
     	}
     };
    
 	public class loginPageAdapter extends PagerAdapter{

 		ArrayList<ImageView> pageViews ;
 		
 		public loginPageAdapter() {
			// TODO Auto-generated constructor stub
 			pageViews = new ArrayList<ImageView>();
 			ImageView collect = new ImageView(LoginFragmentActivity.this);
 			collect.setScaleType(ScaleType.CENTER_INSIDE);
 			collect.setImageResource(R.drawable.login_viewpager_collect);
 			//collect.setBackgroundColor(Color.parseColor("#f8f8f6"));
 			ImageView comment = new ImageView(LoginFragmentActivity.this);
 			comment.setImageResource(R.drawable.login_viewpager_comment);
 			comment.setScaleType(ScaleType.CENTER_INSIDE);
 			pageViews.add(collect);
 			pageViews.add(comment);
 		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager) container).addView(pageViews.get(position));  
            return pageViews.get(position);  
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView(pageViews.get(position));  
		}


	}
    
    
}
