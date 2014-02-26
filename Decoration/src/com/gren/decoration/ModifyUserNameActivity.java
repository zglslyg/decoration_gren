package com.gren.decoration;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gren.decoration.model.UserInfo;
import com.gren.util.CustomerHttpClient;

public class ModifyUserNameActivity extends FragmentActivity{

	public static final String USER_NAME = "user_name";
	 private TextView mModifyNameSend;
	 private EditText mEditUsername;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.account_modifyname_frame);
		
		
		initTopView();
		mModifyNameSend = (TextView)findViewById(R.id.name_post);
		
		FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(96),
				DecorationApplication.cpc.changeImageX(67));
		frameLp.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
		frameLp.rightMargin = DecorationApplication.cpc.changeImageX(30);
		
		mModifyNameSend.setLayoutParams(frameLp);
		mModifyNameSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mEditUsername.getText().equals("")||mEditUsername.getText()==null){
					mModifyNameSend.setClickable(false);
				}
				new Thread(new sendThread()).start();
				//mModifyNameSend.setClickable(false);
			}
		});
		mEditUsername = (EditText)findViewById(R.id.edit_username);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(732),
				DecorationApplication.cpc.changeImageX(119));
		lp.leftMargin = DecorationApplication.cpc.changeImageX(34);
		lp.rightMargin = DecorationApplication.cpc.changeImageX(34);
		mEditUsername.setLayoutParams(lp);
		
		mEditUsername.setText(getIntent().getStringExtra(USER_NAME));
		mEditUsername.setSelection(getIntent().getStringExtra(USER_NAME).length());
		
	}
	private void initTopView(){
    	//顶部背景
		FrameLayout topBg = (FrameLayout)findViewById(R.id.top_bg);
		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(802),
				DecorationApplication.cpc.changeImageX(113));
		topBg.setLayoutParams(linearLp);
		
		 //返回图标
		 ImageView topReturn = (ImageView)findViewById(R.id.top_return);
		 FrameLayout.LayoutParams frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(32);
		 topReturn.setLayoutParams(frameLp);
		 
		 topReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ModifyUserNameActivity.this.finish();
			//	PersonalActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
		 
		 TextView  topReturnTxt = (TextView)findViewById(R.id.top_return_txt);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		 topReturnTxt.setLayoutParams(frameLp);
		 
//		ImageView topReturn = (ImageView)findViewById(R.id.top_return);
//		
//		
//		LayoutParams frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(64),
//				DecorationApplication.cpc.changeImageX(113));//54,45
//		
//		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
//		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(20);
//		topReturn.setPadding(DecorationApplication.cpc.changeImageX(5),
//				DecorationApplication.cpc.changeImageX(34),
//				DecorationApplication.cpc.changeImageX(5),
//				DecorationApplication.cpc.changeImageX(34));
//		topReturn.setLayoutParams(frameLp);

//		 topReturn.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					ModifyUserNameActivity.this.finish();
//				}
//			});
	}
	
	 class sendThread implements Runnable{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=modifyUserName&api=1";	
				String response = CustomerHttpClient.post(urlStr,
	    				new BasicNameValuePair("p_id",UserInfo.getInstance().getUserId()),
	    				new BasicNameValuePair("p_nickname",mEditUsername.getText().toString()));
	    		Log.d("lyl","modifyUserName response is " + response);
	    		Message msg = mResultHandle.obtainMessage();
	    		if(response.equals("error")){
	    			
	    			msg.what = 0;
	    			mResultHandle.sendMessage(msg);
	    		}
	    		else{
	    			try {
						JSONObject jo = new JSONObject(response);
						int status = jo.getInt("status");
						if(status == 1){
							msg.what = 1;
			    			mResultHandle.sendMessage(msg);
						}else if(status == 2){
							msg.what = 2;
			    			mResultHandle.sendMessage(msg);
						}else{
							msg.what = 0;
			    			mResultHandle.sendMessage(msg);
						}
					
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    		}
			}
			 
		 }
		 
		 Handler mResultHandle =new Handler(){
			 @Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
					switch(msg.what){
					case 0:
		    			Toast.makeText(ModifyUserNameActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
		    			break;
		    		case 1:
		    			Toast.makeText(ModifyUserNameActivity.this, "昵称修改成功！", Toast.LENGTH_SHORT).show();
		    			ModifyUserNameActivity.this.finish();
//		    			Intent intent = new Intent(ModifyUserNameActivity.this, PersonalActivity.class);
//						startActivity(intent);
//						overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		    			break;
		    		case 2:
		    			Toast.makeText(ModifyUserNameActivity.this, "该昵称已被注册，请重新填写", Toast.LENGTH_SHORT).show();
		    			break;
					}
			}
		 };
}
