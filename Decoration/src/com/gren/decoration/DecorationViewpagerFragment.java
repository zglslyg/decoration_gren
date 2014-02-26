package com.gren.decoration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gren.util.Constants;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/*
 * 显示项
 */

public class DecorationViewpagerFragment extends Fragment {

	//private static DecorationViewpagerFragment dlf;
	
    private TextView mOrderHot;
    private TextView mOrderPrice;
    private TextView mOrderPraise;
   
    
    
  //  private List<Fragment> mIndexListFragment;
    SlidingFragmentActivity sfa;
    private ViewPager mMainViewPager;// 页卡内容
    
    private ImageView topLogin;
    private ImageView topMenu;
    
    private MainActivity mContainer;
    testPageAdapter mMyPagerAdapter;
	

    
	public DecorationViewpagerFragment() {}
	
//	public DecorationViewpagerFragment(int pos) {
//		mPos = pos;
//	}
	
	public static DecorationViewpagerFragment getInstance(){
		//if(dlf == null){
			DecorationViewpagerFragment dvf = new DecorationViewpagerFragment();
			// final Bundle args = new Bundle();
		   //  args.putString(Constants.CLASS_NAME, className);
		    // dvf.setArguments(args);
		     
		     //清理三个fragment信息
		     
		//}
		return dvf;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 // setContentView(R.layout.activity_main);
	   //     mImageFetcher = new ImageFetcher(this.getActivity(), 240);
	      //  mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mContainer = (MainActivity)this.getActivity();
	//	 Log.d("lyl","DecorationViewpager "+DecorationViewpagerFragment.this.getId()+"onCreate");
	      
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 Log.d("lyl","DecorationViewpager "+DecorationViewpagerFragment.this.getId()+"onCreateView");
		sfa = (SlidingFragmentActivity)DecorationViewpagerFragment.this.getActivity();
		View v = inflater.from(this.getActivity()).inflate(R.layout.main_viewpager, null);
	
		mOrderHot = (TextView)v.findViewById(R.id.order_hot);
		mOrderPrice = (TextView)v.findViewById(R.id.order_price);
		mOrderPraise = (TextView)v.findViewById(R.id.order_praise);

		topLogin = (ImageView)v.findViewById(R.id.top_login);
		topMenu = (ImageView)v.findViewById(R.id.top_menu);
		 
		FrameLayout topBg = (FrameLayout)v.findViewById(R.id.top_bg);
		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(802),
				DecorationApplication.cpc.changeImageX(113));
		topBg.setLayoutParams(linearLp);
		
		LayoutParams topMenuLp = new LayoutParams(DecorationApplication.cpc.changeImageX(64),
				DecorationApplication.cpc.changeImageX(64));
		topMenuLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		topMenuLp.leftMargin = DecorationApplication.cpc.changeImageX(32);
		topMenu.setLayoutParams(topMenuLp);
		
		topMenuLp = new LayoutParams(DecorationApplication.cpc.changeImageX(74),
				DecorationApplication.cpc.changeImageX(141));  //48,52    5 40  61
		
		topMenuLp.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
		topMenuLp.rightMargin = DecorationApplication.cpc.changeImageX(30);
		topLogin.setLayoutParams(topMenuLp);
		topLogin.setPadding(DecorationApplication.cpc.changeImageX(5), DecorationApplication.cpc.changeImageX(40), 
				DecorationApplication.cpc.changeImageX(5), DecorationApplication.cpc.changeImageX(40));
		ImageView mainWave = (ImageView)v.findViewById(R.id.wave);
		topMenuLp = new LayoutParams(DecorationApplication.cpc.changeImageX(802),
				DecorationApplication.cpc.changeImageX(32));  //48,52    5 40
		mainWave.setLayoutParams(topMenuLp);
		
		topLogin.setOnClickListener(onViewpagerClick);
		topMenu.setOnClickListener(onViewpagerClick);
		mOrderHot.setOnClickListener(onViewpagerClick);
		mOrderPrice.setOnClickListener(onViewpagerClick);
		mOrderPraise.setOnClickListener(onViewpagerClick);
		 
			mMainViewPager = (ViewPager) v.findViewById(R.id.vPager);
			mMainViewPager.setOffscreenPageLimit(2);
			
			

		//	mIndexListFragment = new ArrayList<Fragment>();

			

		//	mIndexListFragment.add(DecorationListFragment.newInstance(
		//			this.getArguments().getString(Constants.CLASS_NAME),0));

		//	mIndexListFragment.add(DecorationListFragment.newInstance(
		//			this.getArguments().getString(Constants.CLASS_NAME),1));
		//	mIndexListFragment.add(DecorationListFragment.newInstance(
		//			this.getArguments().getString(Constants.CLASS_NAME),2));
		
		//	mMyPagerAdapter = new MyPagerAdapter(this.getActivity()
		//			.getSupportFragmentManager());
			
			mMyPagerAdapter = new testPageAdapter(this.getActivity()
					.getSupportFragmentManager());
			
			mMainViewPager.setAdapter(mMyPagerAdapter);
		//	mMyPagerAdapter.notifyDataSetChanged();

			
			
			mMainViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageScrollStateChanged(int arg0) { }

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) { }

				@Override
				public void onPageSelected(int position) {
					Log.d("lyl","position is " + mMainViewPager.getId()+position);
					switch (position) {
					case 0:
						sfa.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
						//sfa.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
						mOrderHot.setBackgroundResource(R.drawable.main_category_txtbg);
						mOrderPraise.setBackgroundColor(Color.TRANSPARENT);
						mOrderPrice.setBackgroundColor(Color.TRANSPARENT);
						mOrderHot.setTextColor(Color.parseColor("#ffffff"));
						mOrderPraise.setTextColor(Color.parseColor("#604646"));
						mOrderPrice.setTextColor(Color.parseColor("#604646"));
						break;
					case 1:
						sfa.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
						mOrderPrice.setBackgroundResource(R.drawable.main_category_txtbg);
						mOrderHot.setBackgroundColor(Color.TRANSPARENT);
						mOrderPraise.setBackgroundColor(Color.TRANSPARENT);
						mOrderHot.setTextColor(Color.parseColor("#604646"));
						mOrderPraise.setTextColor(Color.parseColor("#604646"));
						mOrderPrice.setTextColor(Color.parseColor("#ffffff"));
					
						break;
					case 2:
						sfa.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
						mOrderPraise.setBackgroundResource(R.drawable.main_category_txtbg);
						mOrderHot.setBackgroundColor(Color.TRANSPARENT);
						mOrderPrice.setBackgroundColor(Color.TRANSPARENT);
					
						mOrderHot.setTextColor(Color.parseColor("#604646"));
						mOrderPraise.setTextColor(Color.parseColor("#ffffff"));
						mOrderPrice.setTextColor(Color.parseColor("#604646"));
						break;
					default:
					 
						sfa.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
						 
						break;
					}
				}

			});
			
			
		//	mMainViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	//	outState.putInt("mPos", mPos);
	}
	 
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	    Log.d("lyl","DecorationViewpager "+DecorationViewpagerFragment.this.getId()+"onAttach");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		   Log.d("lyl","DecorationViewpager "+DecorationViewpagerFragment.this.getId()+"onDestroy");
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d("lyl","DecorationViewpager "+DecorationViewpagerFragment.this.getId()+"onDetach");
	}
	 /**
		 * ViewPager适配器
		 */
		public class MyPagerAdapter extends FragmentPagerAdapter {
			// public List<View> mListViews;
			//public List<Fragment> mListFragments;

			public MyPagerAdapter(FragmentManager fm) {
				super(fm);
			}

//			public MyPagerAdapter(FragmentManager fm, List<Fragment> listFragments) {
//				super(fm);
//				this.mListFragments = listFragments;
//			}

			@Override
			public int getCount() {
			//	return mIndexListFragment.size();
				return 3;
			}

//			@Override
//			public int getItemPosition(Object object) {
//			//	return super.getItemPosition(object);
//				 return POSITION_NONE;  
//			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
                Log.d("lyl","getItem is " + arg0);
				//return mIndexListFragment.get(arg0);
				
				return DecorationListFragment.newInstance(arg0);
			}
			 
		}
		
		public class testPageAdapter extends FragmentStatePagerAdapter{

			public testPageAdapter(FragmentManager fm) {
				super(fm);
				// TODO Auto-generated constructor stub
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				//return mIndexListFragment.get(arg0);
				DecorationListFragment dlf = DecorationListFragment.newInstance(arg0);
				Bundle bundle = new Bundle();
				
				bundle.putString(Constants.CLASS_NAME, DecorationViewpagerFragment.this.getArguments().getString(Constants.CLASS_NAME));
				bundle.putInt(Constants.CATEGORY, arg0);
				bundle.putBoolean(Constants.IS_SEARCH, DecorationViewpagerFragment.this.getArguments().getBoolean(Constants.IS_SEARCH));
				dlf.setArguments(bundle);
				return dlf;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
			//	return mIndexListFragment.size();
				return 3;
			}
			
		}
		
		
		OnClickListener onViewpagerClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
				
				case R.id.top_menu:
					if(mContainer.getSlidingMenu().isMenuShowing()){
						mContainer.getSlidingMenu().showContent();
					}else{
						mContainer.getSlidingMenu().showMenu();
					}
					break;
				case R.id.top_login:
					DecorationApplication d = (DecorationApplication)DecorationViewpagerFragment.this.getActivity().getApplication();
					if(d.checkIsLogin()){
						Intent intent = new Intent(DecorationViewpagerFragment.this.getActivity(), PersonalActivity.class);
						startActivity(intent);
					}else{
						Intent intent = new Intent(DecorationViewpagerFragment.this.getActivity(), LoginFragmentActivity.class);
						startActivity(intent);
						DecorationViewpagerFragment.this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					//	LoginDialogFragment loginDialogFragment=LoginDialogFragment.getInstance();
					//	loginDialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
					
					//	FragmentTransaction ft = getFragmentManager()						
						//                            .beginTransaction();
						
						// ft.replace(R.id.details, details);//将得到的fragment 替换当前的viewGroup内容，add则不替换会依次累加
					
					//	 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);//设置动画效果
						
					//	 ft.commit();//提交

					//	loginDialogFragment.show(DecorationViewpagerFragment.this.getActivity().getSupportFragmentManager(),"login");
					
					//	loginDialogFragment.show(ft,"login");
					}
					
					break;
				case R.id.order_hot:
					mMainViewPager.setCurrentItem(0,true);
					break;
				case R.id.order_praise:
					mMainViewPager.setCurrentItem(2,true);
					break;
				case R.id.order_price:
					mMainViewPager.setCurrentItem(1,true);
					break;
				}
			}
		};
	
	
}
