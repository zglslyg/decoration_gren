package com.gren.decoration;

import java.util.ArrayList;

import com.gren.util.AdaptationClass;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FunctionIntroActivity extends Activity{

	private ViewPager viewPager;
	private stepPageAdapter stepAdapter;
	boolean isOpen=false;
	float height;
	float width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.function_intro);
		
		  final DisplayMetrics displayMetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	         height = displayMetrics.heightPixels;
	         width = displayMetrics.widthPixels;
	         final int density = displayMetrics.densityDpi;
	         DecorationApplication.cpc = new AdaptationClass(width,
						height,density);
	         DecorationApplication d = (DecorationApplication)this.getApplication();
	 		SharedPreferences userData = this.getSharedPreferences("userdata",0);
	 		d.setPreference(userData);
	 		
		viewPager = (ViewPager)findViewById(R.id.vPager);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			int isScorllChanged;
			int curposition;
		
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				curposition = arg0;
//				if(arg0==3){
//					SharedPreferences userData = FunctionIntroActivity.this.getSharedPreferences("userdata",0);
//					Editor editor = userData.edit();
//					editor.putBoolean("is_first_open", false);
//					editor.commit();
//					
//					 Intent intent = new Intent(FunctionIntroActivity.this,MainActivity.class);
//			 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			 			startActivity(intent);
//			 			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
//			 			FunctionIntroActivity.this.finish();
//				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
			//	Log.d("lyl","onPageScrolled position is " + position);
			//	Log.d("lyl","onPageScrolled positionOffset is " + positionOffset);
			  if(isScorllChanged == 1&&curposition==2&&position!=1&&!isOpen){
				    isOpen = true;
					Log.d("lyl","isOpen   is true");
				    SharedPreferences userData = FunctionIntroActivity.this.getSharedPreferences("userdata",0);
					Editor editor = userData.edit();
					editor.putBoolean("is_first_open", false);
					editor.commit();
					 Intent intent = new Intent(FunctionIntroActivity.this,MainActivity.class);
			 			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 			startActivity(intent);
			 			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
			 			
			 			FunctionIntroActivity.this.finish();
				
			  }
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				isScorllChanged =arg0;
			}
		});
		stepAdapter = new stepPageAdapter();
		viewPager.setAdapter(stepAdapter);
		viewPager.setPageMargin((int) getResources().getDimension(R.dimen.image_detail_pager_margin));
	}
	public class stepPageAdapter extends PagerAdapter{

 		ArrayList<View> pageViews ;
 		
 		public stepPageAdapter() {
			// TODO Auto-generated constructor stub
 			pageViews = new ArrayList<View>();
 			ImageView stepOne = new ImageView(FunctionIntroActivity.this);
 			stepOne.setBackgroundResource(R.drawable.step_one);
 			//collect.setBackgroundColor(Color.parseColor("#f8f8f6"));
 			ImageView stepTwo = new ImageView(FunctionIntroActivity.this);
 			stepTwo.setBackgroundResource(R.drawable.step_two);
 			
 			FrameLayout fm =  new FrameLayout(FunctionIntroActivity.this);
 			fm.setBackgroundResource(R.drawable.step_three);
 			
 			ImageView stepThree = new ImageView(FunctionIntroActivity.this);
 			
 			FrameLayout.LayoutParams fmLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(281),
 					DecorationApplication.cpc.changeImageX(87));
 			fmLp.gravity = Gravity.BOTTOM|Gravity.LEFT;
 			fmLp.leftMargin = (int)(60*width/800);
 			fmLp.bottomMargin = (int)(200*width/800);
 			
 			
 			
 			stepThree.setImageResource(R.drawable.login_direct);
 			stepThree.setLayoutParams(fmLp);
 			fm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!isOpen){
						isOpen=true;
						SharedPreferences userData = FunctionIntroActivity.this.getSharedPreferences("userdata",0);
						Editor editor = userData.edit();
						editor.putBoolean("is_first_open", false);
						editor.commit();
						
						 Intent intent = new Intent(FunctionIntroActivity.this,MainActivity.class);
				 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 			startActivity(intent);
				 			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
				 			FunctionIntroActivity.this.finish();
					}
					
				}
			});
 			//stepThree.setBackgroundResource(R.drawable.step_three);
 			
 			//stepThree.setImageBitmap(DecorationApplication.bc.readBitMap(FunctionIntroActivity.this, R.drawable.step_three_btn, null));
 			//stepThree.setScaleType(ScaleType.FIT_END);
 		//	stepThree.setPadding(50, 0, 50,0);
 			stepThree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!isOpen){
						isOpen=true;
						SharedPreferences userData = FunctionIntroActivity.this.getSharedPreferences("userdata",0);
						Editor editor = userData.edit();
						editor.putBoolean("is_first_open", false);
						editor.commit();
						
						Intent intent = new Intent(FunctionIntroActivity.this,LoginFragmentActivity.class);
				 	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 	    intent.putExtra("is_directin", true);
				 	    
				 		startActivity(intent);
				 		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
				 		FunctionIntroActivity.this.finish();
					}
					
				}
 			});
//            ImageView stepFour = new ImageView(FunctionIntroActivity.this);
// 			
// 			fmLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
// 					FrameLayout.LayoutParams.WRAP_CONTENT);
// 			fmLp.gravity = Gravity.BOTTOM|Gravity.RIGHT;
// 			fmLp.rightMargin = 50;
// 			fmLp.bottomMargin = 50;
// 			stepFour.setImageResource(R.drawable.top_submit);
// 			stepFour.setLayoutParams(fmLp);
 			//stepThree.setBackgroundResource(R.drawable.step_three);
 			
 			//stepThree.setImageBitmap(DecorationApplication.bc.readBitMap(FunctionIntroActivity.this, R.drawable.step_three_btn, null));
 			//stepThree.setScaleType(ScaleType.FIT_END);
 		//	stepThree.setPadding(50, 0, 50,0);
// 			stepFour.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if(!isOpen){
//						isOpen=true;
//						SharedPreferences userData = FunctionIntroActivity.this.getSharedPreferences("userdata",0);
//						Editor editor = userData.edit();
//						editor.putBoolean("is_first_open", false);
//						editor.commit();
//						
//						 Intent intent = new Intent(FunctionIntroActivity.this,MainActivity.class);
//				 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				 			startActivity(intent);
//				 			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out); 
//				 			FunctionIntroActivity.this.finish();
//					}
//					
//				}
//			});
 			//ImageView stepFour = new ImageView(FunctionIntroActivity.this);
 			fm.addView(stepThree);
 			//fm.addView(stepFour);
 			
 			pageViews.add(stepOne);
 			pageViews.add(stepTwo);
 			pageViews.add(fm);
 			//pageViews.add(stepFour);
 			
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
