package com.gren.decoration;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bitmapfun.util.ImageFetcher;

public class SettingActivity extends FragmentActivity{

	RelativeLayout aboutus;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.setting_activity);
		initTopView();
		
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
				SettingActivity.this.finish();
			//	PersonalActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
		 
		 TextView  topReturnTxt = (TextView)findViewById(R.id.top_return_txt);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		 topReturnTxt.setLayoutParams(frameLp);
		 
		 LinearLayout settingTwoFrame = (LinearLayout)findViewById(R.id.setting_two_frame);
		 
		 linearLp = new LinearLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(732),DecorationApplication.cpc.changeImageX(235));
		 int margin =DecorationApplication.cpc.changeImageX(34);
		 linearLp.setMargins(margin, margin, margin, margin);
		 
		 
		 settingTwoFrame.setLayoutParams(linearLp);
		 
		 LinearLayout settingFrame = (LinearLayout)findViewById(R.id.setting_frame);
		 
		 linearLp = new LinearLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(732),DecorationApplication.cpc.changeImageX(119));
		 linearLp.setMargins(margin, margin, margin, margin);
		 settingFrame.setLayoutParams(linearLp);
		 
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
//					SettingActivity.this.finish();
//				}
//			});
		 
		  aboutus = (RelativeLayout)findViewById(R.id.about_us);
//		 aboutus.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(SettingActivity.this, AboutUsFragmentActivity.class);
//				startActivity(intent);
//				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//			}
//		});
		 aboutus.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//aboutus.setBackgroundColor(Color.parseColor("#f3f2f2"));
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						//aboutus.setBackgroundColor(Color.parseColor("#00000000"));
						Intent intent = new Intent(SettingActivity.this, AboutUsFragmentActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
						//aboutus.setBackgroundColor(Color.parseColor("#00000000"));
						
					}
					return true;
			}
		});
		 
		 
		 final RelativeLayout feedback = (RelativeLayout)findViewById(R.id.feedback);
		 feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this, FeedBackFragmentActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		 
		 feedback.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//	feedback.setBackgroundColor(Color.parseColor("#f3f2f2"));
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
						//	feedback.setBackgroundColor(Color.parseColor("#00000000"));
							Intent intent = new Intent(SettingActivity.this, FeedBackFragmentActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						
						} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
						//	feedback.setBackgroundColor(Color.parseColor("#00000000"));
							
						}
						return true;
				}
			});
		 
		 final RelativeLayout clearCache = (RelativeLayout)findViewById(R.id.clear_cache);
		 clearCache.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//	clearCache.setBackgroundColor(Color.parseColor("#f3f2f2"));
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
						//	clearCache.setBackgroundColor(Color.parseColor("#00000000"));
							alert();
//							new AlertDialog.Builder(SettingActivity.this).setTitle("") 
//						    .setIcon(android.R.drawable.ic_dialog_info) 
//						    .setMessage("清除缓存后将重新加载已有的图片资源，确定清除么？")
//						    .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
//						 
//						        @Override 
//						        public void onClick(DialogInterface dialog, int which) { 
//						        // 点击“确认”后的操作 
//						        	DecorationApplication d = (DecorationApplication)SettingActivity.this.getApplication();
//									ArrayList<ImageFetcher> fetcherList = d.getImageFetcher();
//									for(int i = 0;i<fetcherList.size();i++){
//										fetcherList.get(i).clearCache();
//									}
//									Toast.makeText(SettingActivity.this, "清理完毕", Toast.LENGTH_SHORT).show();
//						        } 
//						    }) 
//						    .setNegativeButton("返回", new DialogInterface.OnClickListener() { 
//						 
//						        @Override 
//						        public void onClick(DialogInterface dialog, int which) { 
//						        // 点击“返回”后的操作,这里不设置没有任何操作 
//						        } 
//						    }).show(); 
						
						} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
							//clearCache.setBackgroundColor(Color.parseColor("#00000000"));
							
						}
						return true;
				}
			});
		 
    }
	
	public void alert(){ 
        final AlertDialog alert = new AlertDialog.Builder(this).create(); 
        alert.show(); 

        alert.getWindow().setLayout(DecorationApplication.cpc.changeImageX(657), DecorationApplication.cpc.changeImageX(311)); 
       // alert.setTitle("测试"); 
        
        alert.getWindow().setContentView(R.layout.mydialog); 
        
        TextView title = (TextView)alert.findViewById(R.id.title);
        title.setText("清除缓存后将重新加载已有的图片资源，确定清除么？");
        Button okButton = (Button)alert.findViewById(R.id.ok);
        okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DecorationApplication d = (DecorationApplication)SettingActivity.this.getApplication();
				ArrayList<ImageFetcher> fetcherList = d.getImageFetcher();
				for(int i = 0;i<fetcherList.size();i++){
					fetcherList.get(i).clearCache();
				}
				Toast.makeText(SettingActivity.this, "清理完毕", Toast.LENGTH_SHORT).show();
		        alert.dismiss();
			}
		});
        
        Button cancelButton = (Button)alert.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  alert.dismiss();
			}
		});
        
}
}
