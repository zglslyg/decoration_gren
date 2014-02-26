package com.gren.decoration;


import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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

public class FeedBackFragmentActivity extends  Activity{
	
 
	
	 EditText redirect_sendmessage;
	 TextView txt_fontnumber;
	 final int MAX_LENGTH = 140;//设置可输入最大字数
	 int Rest_Length = MAX_LENGTH;
	 
	 private ImageView mFeedbackSend;
	 
	 private CharSequence temp;//输入的文字
	 private int editStart ;//输入光标开始位置
	 private int editEnd ;//输入光标结束位置
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_feedback_frame);
		
		initTopView();
		
		mFeedbackSend = (ImageView)findViewById(R.id.feedback_send);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(96),
				DecorationApplication.cpc.changeImageX(67));
		lp.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
		lp.rightMargin = DecorationApplication.cpc.changeImageX(25);
		mFeedbackSend.setLayoutParams(lp);
		mFeedbackSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(redirect_sendmessage.getText().length()==0){
					Message msg = mResultHandle.obtainMessage();
                    msg.what = 2;
		    		mResultHandle.sendMessage(msg);
				}
				new Thread(new sendThread()).start();				
			}
		});
		 txt_fontnumber =(TextView)findViewById(R.id.txt_fontnumber);
		  redirect_sendmessage = (EditText)findViewById(R.id.feedback_text);
		  
			LayoutParams frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(701),
					DecorationApplication.cpc.changeImageX(381));//54,45
			
			frameLp.gravity = Gravity.CENTER_HORIZONTAL;
			
			redirect_sendmessage.setLayoutParams(frameLp);
			
	        redirect_sendmessage.addTextChangedListener(new TextWatcher() {
				
	        	@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
				//	Rest_Length = MAX_LENGTH - redirect_sendmessage.getText().length();
					Rest_Length =MAX_LENGTH - (int)calculateWeiboLength(redirect_sendmessage.getText());

					
					if(Rest_Length>=0)
						txt_fontnumber.setText(Rest_Length+"x");
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
					txt_fontnumber.setText(Rest_Length+"x");
					temp = s;
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
					editStart = redirect_sendmessage.getSelectionStart();
					editEnd = redirect_sendmessage.getSelectionEnd();
					if (temp.length() > 140) {

						//Toast.makeText(FeedBackFragmentActivity.this, "字数不能超过140个", Toast.LENGTH_SHORT).show();
					    s.delete(editStart-1, editEnd);
					    int tempSelection = editEnd;
					    redirect_sendmessage.setText(s);
					    redirect_sendmessage.setSelection(tempSelection);
					}
					txt_fontnumber.setText(Rest_Length+"x");
				}
			});
	     
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
				FeedBackFragmentActivity.this.finish();
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
//
//		 topReturn.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					FeedBackFragmentActivity.this.finish();
//				}
//			});
	}
	
	 class sendThread implements Runnable{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=feedbackAdd";	
				String response = CustomerHttpClient.post(urlStr,
	    				new BasicNameValuePair("member_id",UserInfo.getInstance().getUserId()),
	    				new BasicNameValuePair("content",redirect_sendmessage.getText().toString()));
	    		Log.d("lyl","register response is " + response);
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
		    			Toast.makeText(FeedBackFragmentActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
		    			break;
		    		case 1:
		    			Toast.makeText(FeedBackFragmentActivity.this, "我们已收到您的反馈，您的支持让我们做的更好！", Toast.LENGTH_SHORT).show();
		    			break;
		    		case 2:
		    			Toast.makeText(FeedBackFragmentActivity.this, "请填写反馈信息", Toast.LENGTH_SHORT).show();
		    			break;
					}
			}
		 };
		 /**
		    * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
		    * @param c 所要统计的字符序列
		    * @return 返回字符序列计算的长度
		    */ 
		    public static long calculateWeiboLength(CharSequence c) { 

		    double len = 0; 
		    for (int i = 0; i < c.length(); i++) { 
		    int temp = (int)c.charAt(i); 
		    if (temp > 0 && temp < 127) { 
		    len += 0.5; 
		    }else{ 
		    len ++; 
		    } 
		    } 
		    return Math.round(len); 
		    }
}
