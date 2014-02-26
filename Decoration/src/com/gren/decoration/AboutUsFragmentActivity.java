package com.gren.decoration;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class AboutUsFragmentActivity extends FragmentActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus_frame);
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
				AboutUsFragmentActivity.this.finish();
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
//					AboutUsFragmentActivity.this.finish();
//				}
//			});
	}
}
