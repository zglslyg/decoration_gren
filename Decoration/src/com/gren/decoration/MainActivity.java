package com.gren.decoration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.decoration.model.UserInfo;
import com.gren.util.AdaptationClass;
import com.gren.util.Constants;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity{

	private Fragment mContent;
	private ImageFetcher imageFetcher;
	 public static final String IMAGE_CACHE_DIR = "images";
	 
	//private SlidingMenu sm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		setContentView(R.layout.responsive_content_frame);
		

		///判断网络连接类型
		DecorationApplication.networkType = DecorationApplication.checkNetworkConnection(this);
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		// set the Above View Fragment
	//	if (savedInstanceState != null)
	//		mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null){
			//DecorationApplication.className = "卫浴";
			//mContent = DecorationViewpagerFragment.getInstance();
			mContent = IndexFragment.getInstance();
		//	Bundle bundle = new Bundle();
		 //
		 
			//bundle.putString(Constants.CLASS_NAME, "卫浴");
		//	mContent.setArguments(bundle);
		}
			//mContent = new BirdGridFragment(0);	
		//	mContent = DecorationListFragment.getInstance("ds");{
			
		
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();

		// set the Behind View Fragment
		getSupportFragmentManager()
		.beginTransaction()
	//	.replace(R.id.menu_frame, new MenuFragment())
		.replace(R.id.menu_frame, DMenuFragment.getInstance())
		.commit();

	
	 
 
		// Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
		    final DisplayMetrics displayMetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	        final int height = displayMetrics.heightPixels;
	        final int width = displayMetrics.widthPixels;
	        final int density = displayMetrics.densityDpi;

	        if(DecorationApplication.cpc==null){
	        	DecorationApplication.cpc = new AdaptationClass(width,
						height,density);

	        }
			
	        // For this sample we'll use half of the longest width to resize our images. As the
	        // image scaling ensures the image is larger than this, we should be left with a
	        // resolution that is appropriate for both portrait and landscape. For best image quality
	        // we shouldn't divide by 2, but this will use more memory and require a larger memory
	        // cache.
	        final int longest = (height > width ? height : width) / 2;

	        ImageCache.ImageCacheParams cacheParams =
	                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
	        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

	        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
			imageFetcher = new ImageFetcher(this, width/2);
			 
			imageFetcher.addImageCache(MainActivity.this.getSupportFragmentManager(), cacheParams);
			imageFetcher.setImageFadeIn(true);

			imageFetcher.setLoadingImage(R.drawable.load_default);
			
			DecorationApplication d = (DecorationApplication)this.getApplication();
			d.addImageFetcher(imageFetcher);
		// show the explanation dialog
//		if (savedInstanceState == null)
//			new AlertDialog.Builder(this)
//			.setTitle(R.string.what_is_this)
//			.setMessage(R.string.responsive_explanation)
//			.show();
			//初始化用户，应用配置
			
			// customize the SlidingMenu
			SlidingMenu sm = getSlidingMenu();
			//sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			sm.setBehindOffset((int)(width*0.15));//0.18
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setShadowDrawable(R.drawable.menu_shadow);
			sm.setBehindScrollScale(0.25f);
			sm.setFadeDegree(0.75f);
			initConfig();
		
	}
	void initConfig(){
		DecorationApplication d = (DecorationApplication)this.getApplication();
		SharedPreferences userData = this.getSharedPreferences("userdata",0);
		d.setPreference(userData);
		if(d.checkIsLogin()){
			UserInfo.getInstance().setUserId(userData.getString("userid", ""));
			 DMenuFragment.getInstance().setAvatar();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}	

	public void onItemPressed(int pos,int category) {
		//Intent intent = BirdActivity.newInstance(this, pos);
		//startActivity(intent);
		 Log.d("lyl","MainAcitivity position is " + pos);
		Intent intent = new Intent(this, DecorationDetailActivity.class);
		intent.putExtra("position", pos);
		intent.putExtra(Constants.CATEGORY, category);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
	
	public ImageFetcher getImageFetcher(){
		return imageFetcher;
	}
			
	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();

			} else {
				MainActivity.this.finish();
				System.exit(0);
			//	HyFootballApplication.getInstance().exit();

			}
		}
		
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	DecorationApplication.decorationTimeList.clear();
    	DecorationApplication.decorationPriceList.clear();
    	DecorationApplication.decorationPraiseList.clear();
    	DecorationApplication.decorationHotList.clear();
    	Log.d("lyl","onDestroy");
    }


}
