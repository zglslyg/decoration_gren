package com.gren.decoration;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.decoration.model.DecorationInfo;
import com.gren.decoration.model.UserInfo;
import com.gren.util.CustomerHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class AdDetailActivity extends FragmentActivity{

	
	private ImageView topPic;
	private TextView title;
	private TextView lead;   //简介
	private TextView intro;   //详细介绍
	private String adId;
	private String strTitle;
	private String strLead;
	private String strIntro;
	private String imgUrl;
	private ImageFetcher imageFetcher;
	 public static final String IMAGE_CACHE_DIR = "images";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_detail);
		adId = getIntent().getStringExtra("ad_id");
		imgUrl = getIntent().getStringExtra("img_url");
		  ImageCache.ImageCacheParams cacheParams =
              new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
       cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
		 // The ImageFetcher takes care of loading images into our ImageView children asynchronously
		imageFetcher = new ImageFetcher(this, (int)DecorationApplication.cpc.getCurWidth());
		 
		imageFetcher.addImageCache(this.getSupportFragmentManager(), cacheParams);
		imageFetcher.setImageFadeIn(true);

		imageFetcher.setLoadingImage(R.drawable.load_default);
		
		DecorationApplication d = (DecorationApplication)this.getApplication();
		d.addImageFetcher(imageFetcher);
		
		initTopView();
		initOtherView();
		new Thread(readInfoRun).start();
		
		

     
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
				AdDetailActivity.this.finish();
			//	PersonalActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
		 
		 TextView  topReturnTxt = (TextView)findViewById(R.id.top_return_txt);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		 topReturnTxt.setLayoutParams(frameLp);
	}
	private void initOtherView(){
		topPic = (ImageView)findViewById(R.id.pic);
		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(760),
				DecorationApplication.cpc.changeImageX(500));
		linearLp.setMargins(DecorationApplication.cpc.changeImageX(20),
				DecorationApplication.cpc.changeImageX(30), 
				DecorationApplication.cpc.changeImageX(20), 
				DecorationApplication.cpc.changeImageX(30));
		topPic.setLayoutParams(linearLp);
		
		title = (TextView)findViewById(R.id.title);
		lead = (TextView)findViewById(R.id.lead);
		intro = (TextView)findViewById(R.id.intro);
		
		
		//活动简介标题栏布局
		
		ImageView activityLeft = (ImageView)findViewById(R.id.activity_left);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(36),
				DecorationApplication.cpc.changeImageX(57));
		activityLeft.setLayoutParams(linearLp);
		
		TextView actvityTxt = (TextView)findViewById(R.id.activity_txt);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(195),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearLp.gravity = Gravity.CENTER_VERTICAL;
		actvityTxt.setLayoutParams(linearLp);
		
		ImageView adActivity = (ImageView)findViewById(R.id.ad_activity);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(158),
				DecorationApplication.cpc.changeImageX(22));
		linearLp.rightMargin = DecorationApplication.cpc.changeImageX(30);
		linearLp.gravity = Gravity.CENTER_VERTICAL;
		adActivity.setLayoutParams(linearLp);
		
		
		
		ImageView adLine = (ImageView)findViewById(R.id.ad_line);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(381),
				DecorationApplication.cpc.changeImageX(3));
		adLine.setScaleType(ScaleType.CENTER_CROP);
		linearLp.gravity = Gravity.CENTER_VERTICAL;
		adLine.setLayoutParams(linearLp);
		
		
		//活动详情标题布局
		ImageView eventLeft = (ImageView)findViewById(R.id.event_left);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(36),
				DecorationApplication.cpc.changeImageX(57));
		eventLeft.setLayoutParams(linearLp);
		
		TextView eventTxt = (TextView)findViewById(R.id.event_txt);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(195),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearLp.gravity = Gravity.CENTER_VERTICAL;
		eventTxt.setLayoutParams(linearLp);
		
		ImageView adEvent = (ImageView)findViewById(R.id.ad_event);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(158),
				DecorationApplication.cpc.changeImageX(22));
		linearLp.rightMargin = DecorationApplication.cpc.changeImageX(30);
		linearLp.gravity = Gravity.CENTER_VERTICAL;
		adEvent.setLayoutParams(linearLp);
		
		
		
		ImageView adEeventLine = (ImageView)findViewById(R.id.ad_eline);
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(381),
				DecorationApplication.cpc.changeImageX(3));
		linearLp.gravity = Gravity.CENTER_VERTICAL;
		adEeventLine.setLayoutParams(linearLp);
		adEeventLine.setScaleType(ScaleType.CENTER_CROP);
		
		imageFetcher.loadImage(imgUrl, topPic);

		
	}
	
	 Runnable readInfoRun = new Runnable(){

			@Override

			public void run() {

			// TODO Auto-generated method stub

				    String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=news&id="+adId;
					
	        	   
				    String response = CustomerHttpClient.post(urlStr);
				    Log.d("lyl","response is " + response);
				    if(response==null||response.equals("")){
				    	 Message m = new Message();
			        	  //  Bundle responseBundle = new Bundle();
			        	   // responseBundle.putString("response", response);
			        	  //  m.setData(responseBundle);
			        	    m.what = 1;
			        	    readInfoHandler.sendMessage(m);
				    }
				    if(response.contains("error")){
				    	 Message m = new Message();
			        	  //  Bundle responseBundle = new Bundle();
			        	   // responseBundle.putString("response", response);
			        	  //  m.setData(responseBundle);
			        	    m.what = 1;
			        	    readInfoHandler.sendMessage(m);
				    }else{
				    	  try {
								JSONObject decorationJson = new JSONObject(response);
									
//								Log.d("lyl","info is " +decorationJson.getString("title")+","+
//										decorationJson.getString("lead")+","+decorationJson.getString("content"));	
								strTitle = decorationJson.getString("title");
								strLead = decorationJson.getString("lead");
								strIntro = decorationJson.getString("content");
								 Message m = new Message();
					        	//    Bundle responseBundle = new Bundle();
					        	//    responseBundle.putString("response", response);
					        	 //   m.setData(responseBundle);
					        	    m.what = 0;
					        	    readInfoHandler.sendMessage(m);
								
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				    }	
				    	
				    }

	 };
	 
	  private Handler readInfoHandler=new Handler(){
	        @Override
	        public void handleMessage(Message msg){
	            
	            switch(msg.what){
	            
	            case 0:
	                //关闭
	            //	faceImage.setImageBitmap(bitmap);
//	            	  mLoadingImg.clearAnimation();
//	            	 // mLoadingImg.setVisibility(View.INVISIBLE);
//	            	  mLoadingLinear.setVisibility(View.GONE);
//	            	//Toast.makeText(mContainerActivity, "读取数据成功", Toast.LENGTH_SHORT).show();
//	            	  mAdapter.notifyDataSetChanged();
//	            	//  gridView.setSelection(mListPos);
//	            	  mBottomLoading.setVisibility(View.GONE);
//	            	 mLikeNum.setText(String.valueOf(totalDecorationNum)) ;
//	            	isLoading = false;
//	            	if(DecorationApplication.decorationCollectList.size()==0){
//	            		
//	            	}
	            	
	            	title.setText(strTitle);
	            	lead.setText(strLead);
	            	intro.setText(strIntro);
	            	break;
	            case 1:
	            	break;
	            }
	        }
	  };
	
	
}
