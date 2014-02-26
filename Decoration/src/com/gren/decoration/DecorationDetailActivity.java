/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gren.decoration;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.util.Constants;

public class DecorationDetailActivity extends FragmentActivity implements OnClickListener {
    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";

    private ImagePagerAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private ViewPager mPager;
    private int mCategory;

   
    
    @TargetApi(11)
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

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
      //  mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher = new ImageFetcher(this,(int)DecorationApplication.cpc.getCurWidth());
        
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(true);
        mImageFetcher.setLoadingImage(R.drawable.load_default);
        DecorationApplication d = (DecorationApplication)this.getApplication();
		d.addImageFetcher(mImageFetcher);
        
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        mCategory = intent.getIntExtra(Constants.CATEGORY, 0);
     //   Log.d("lyl","position is " + position);
        
        // Set up ViewPager and backing adapter
     //   mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), Images.imageUrls.length);
        if(mCategory==0){
        	mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), 
        			DecorationApplication.decorationPraiseList.size());
        }else if (mCategory==1){
        	mAdapter = new ImagePagerAdapter(getSupportFragmentManager(),
                    DecorationApplication.decorationPriceList.size());
        }else if(mCategory == 2){
        	mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), 
        			DecorationApplication.decorationTimeList.size());
        }else if(mCategory == 3){
        	mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), 
        			DecorationApplication.decorationCollectList.size());
        }else if(mCategory == 4){
        	mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), 
        			DecorationApplication.decorationHotList.size());
        }
       // mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), DecorationApplication.decorationTimeList.size());
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.image_detail_pager_margin));
        mPager.setOffscreenPageLimit(2);

        
        mPager.setCurrentItem(position);
        
        mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        initTopView();
        // Set up activity to go full screen
        //getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

        // Enable some additional newer visibility and ActionBar features to create a more
        // immersive photo viewing experience
//        if (Utils.hasHoneycomb()) {
//            final ActionBar actionBar = getActionBar();
//
//            // Hide title text and set home as up
//            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//
//            // Hide and show the ActionBar as the visibility changes
//            mPager.setOnSystemUiVisibilityChangeListener(
//                    new View.OnSystemUiVisibilityChangeListener() {
//                        @Override
//                        public void onSystemUiVisibilityChange(int vis) {
//                            if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
//                                actionBar.hide();
//                            } else {
//                                actionBar.show();
//                            }
//                        }
//                    });
//
//            // Start low profile mode and hide ActionBar
//            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//            actionBar.hide();
//        }

        // Set the current item based on the extra passed in to this activity
    //    final int extraCurrentItem = getIntent().getIntExtra(EXTRA_IMAGE, -1);
   //     if (extraCurrentItem != -1) {
    //        mPager.setCurrentItem(extraCurrentItem);
    //    }
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	
    }

    private void initTopView(){
    	//顶部背景
		FrameLayout topBg = (FrameLayout)findViewById(R.id.top_bg);
		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(802),
				DecorationApplication.cpc.changeImageX(109));
		topBg.setLayoutParams(linearLp);
		
		
		TextView topReturn = (TextView)findViewById(R.id.top_return);
		
		
		LayoutParams frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(96),
				DecorationApplication.cpc.changeImageX(67));//54,45
		
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(35);
//		topReturn.setPadding(DecorationApplication.cpc.changeImageX(5),
//				DecorationApplication.cpc.changeImageX(23),
//				DecorationApplication.cpc.changeImageX(5),
//				DecorationApplication.cpc.changeImageX(23));
		
		topReturn.setLayoutParams(frameLp);
		
//		ImageView wave = (ImageView)findViewById(R.id.wave);
//		 frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(802),
//					DecorationApplication.cpc.changeImageX(25));//54,45
//		 wave.setLayoutParams(frameLp);
		 
		 topReturn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DecorationDetailActivity.this.finish();
					DecorationDetailActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				}
			});
    }
    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

 

    

    /**
     * Called by the ViewPager child fragments to load images via the one ImageFetcher
     */
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }

    /**
     * The main adapter that backs the ViewPager. A subclass of FragmentStatePagerAdapter as there
     * could be a large number of items in the ViewPager and we don't want to retain them all in
     * memory at once but create/destroy them on the fly.
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;

        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public DecorationDetailFragment getItem(int position) {
//            return DecorationDetailFragment.newInstance(Images.imageUrls[position]);
//        	 if(mCategory==0){
        		  return DecorationDetailFragment.newInstance(position,mCategory);
//             }else if (mCategory==1){
//            	 return DecorationDetailFragment.newInstance(DecorationApplication.
//       				  decorationPriceList.get(position).getImgUrl());
//             }else{
//            	 return DecorationDetailFragment.newInstance(DecorationApplication.
//       				  decorationTimeList.get(position).getImgUrl());
//             }
          
        }
    }

    /**
     * Set on the ImageView in the ViewPager children fragments, to enable/disable low profile mode
     * when the ImageView is touched.
     */
    @TargetApi(11)
    @Override
    public void onClick(View v) {
        final int vis = mPager.getSystemUiVisibility();
        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }
}
