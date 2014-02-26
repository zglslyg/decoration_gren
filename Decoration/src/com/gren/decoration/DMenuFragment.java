package com.gren.decoration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.decoration.model.UserInfo;
import com.gren.util.Constants;

public class DMenuFragment extends Fragment implements OnItemClickListener{

	View mTotalView;
	
	private int mClickPosition = 0;
		
	ListView menuList;
	MenuAdapter dMenuAdapter;
	MenuAdapter hMenuAdapter;
	ImageView tabBelow;
	
	int mCurMenuList;
	TextView searchEdit;
	boolean isClicked;
	
	private static final int MENULIST_DECORATION = 0;
	private static final int MENULIST_HOUSE = 1;
	private static final int PIC_ROUND_RADIUS = 5;
	

	 
	
	ImageView indexIcon;
	
	private static DMenuFragment menuFragment = new DMenuFragment();
	
	int[] mDMenuIconId = {
			R.drawable.menu_fitting_icon,
			R.drawable.menu_tile_icon,
			R.drawable.menu_kitchen_icon,
			R.drawable.menu_door_icon,
			
			R.drawable.menu_cabinet_icon	,
			R.drawable.menu_sofa_icon,
			R.drawable.menu_bed_icon,
			R.drawable.menu_meal_icon,
			
			R.drawable.menu_wallpaper_icon	,
			R.drawable.menu_light_icon,
			R.drawable.menu_floor_icon,
			R.drawable.menu_oil_icon,
			
			R.drawable.menu_deco_icon	,

		};
		
		String[] mDMenuItemName = {
			"管材/管件","瓷砖/卫浴","吊顶/橱柜","木门/楼梯",
			"衣柜/鞋柜","沙发/茶几","床/床垫","餐桌/餐椅",
			"壁纸/窗帘","照明/灯具","地板/电视柜","油漆/涂料",
			"饰品/电器"
		};
		
		int[] mHMenuIconId = {
				R.drawable.menu_house_icon	,
				R.drawable.menu_cloth_icon,
				R.drawable.menu_tel_icon,
				R.drawable.menu_car_icon,
				
				R.drawable.menu_food_icon	,
				R.drawable.menu_travel_icon,
				R.drawable.menu_child_icon,
				R.drawable.menu_recharge_icon,
				
				R.drawable.menu_fish_icon	,
				R.drawable.menu_driver_icon,
				R.drawable.menu_used_icon,
				R.drawable.menu_buddha_icon,
				
				R.drawable.menu_flower_icon	,
				R.drawable.menu_medicine_icon,
				R.drawable.menu_antique,
			};
			
			String[] mHMenuItemName = {
				"房产/租赁","服饰/鞋帽","手机/数码","汽车",
				"食品/饮料","旅行/酒店","母婴","充值/事务",
				"钓鱼/驴友","自驾/棋牌","二手","佛教",
				"鱼花/草","化妆品/药","茶叶/古玩"
			};
	List<SampleItem> dMenuItems;
	List<SampleItem> hMenuItems;
			
	public static DMenuFragment getInstance(){
		return menuFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCurMenuList = MENULIST_DECORATION;
		
		isClicked = false;
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mTotalView = inflater.inflate(R.layout.slide_menu, null);
		initView();
	
		
		return mTotalView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		
		dMenuItems = new ArrayList<SampleItem>();
		hMenuItems = new ArrayList<SampleItem>();
		for (int i = 0; i < mDMenuItemName.length; i++) {
			//	adapter.add(new SampleItem(mMenuItemName[i], mMenuIconId[i]));
			   dMenuItems.add(new SampleItem(mDMenuItemName[i], mDMenuIconId[i]));
				
			}
		for (int i = 0; i < mHMenuItemName.length; i++) {
			//	adapter.add(new SampleItem(mMenuItemName[i], mMenuIconId[i]));
			hMenuItems.add(new SampleItem(mHMenuItemName[i], mHMenuIconId[i]));
				
		}
		
		dMenuAdapter = new MenuAdapter(dMenuItems);
		
		hMenuAdapter = new MenuAdapter(hMenuItems);
		
		menuList.setAdapter(dMenuAdapter);
		
		menuList.setOnItemClickListener(this);
	}
	
	private void initView(){
		//菜单顶部首页布局开始
		FrameLayout indexFrame =(FrameLayout)mTotalView.findViewById(R.id.menu_index_frame);
		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				DecorationApplication.cpc.changeImageX(108));
		indexFrame.setLayoutParams(linearLp);

		indexIcon = (ImageView)mTotalView.findViewById(R.id.index_icon);
		FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(87), 
				DecorationApplication.cpc.changeImageX(87));
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(30);  //40
		indexIcon.setScaleType(ScaleType.CENTER);
		indexIcon.setLayoutParams(frameLp);
		
		
		
		TextView indexTxt = (TextView)mTotalView.findViewById(R.id.index_txt);
		frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(350), 
				FrameLayout.LayoutParams.WRAP_CONTENT);
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(144);   //140
		indexTxt.setLayoutParams(frameLp);
		indexTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isClicked){
					  Fragment newContent = IndexFragment.getInstance();
						if (newContent != null)
							switchFragment(newContent);
				}
				
			}
		});

		final ImageView settingIcon = (ImageView)mTotalView.findViewById(R.id.index_setting);
		settingIcon.setLayoutParams(Constants.getFrameLp(69, 69, 570, 20));
		settingIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DMenuFragment.this.getActivity(), SettingActivity.class);
				startActivity(intent);
			}
		});
		
		//菜单顶部首页布局结束
		//搜索布局开始
		FrameLayout searchFrame =(FrameLayout)mTotalView.findViewById(R.id.search);
		searchFrame.setLayoutParams(linearLp);
		
		ImageView searchBg = (ImageView)mTotalView.findViewById(R.id.search_bg);
		frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(523), 
				DecorationApplication.cpc.changeImageX(72));
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(10);
		
//		searchBg.setLayoutParams(Constants.getFrameLp(523, 72, 10, 12));
		searchBg.setLayoutParams(frameLp);
		
		ImageView searchEditIcon = (ImageView)mTotalView.findViewById(R.id.search_edit_icon);
		frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(28), 
				DecorationApplication.cpc.changeImageX(34));
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(40);
		
		//searchEditIcon.setLayoutParams(Constants.getFrameLp(28, 34, 40, 30));
		searchEditIcon.setLayoutParams(frameLp);
		
		final ImageView searchButton = (ImageView)mTotalView.findViewById(R.id.search_button);
		frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(69), 
				DecorationApplication.cpc.changeImageX(69)); 
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(570);
		
		
//		searchButton.setLayoutParams(Constants.getFrameLp(69, 69, 560, 12));
		searchButton.setLayoutParams(frameLp);
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if(searchEdit.getText()==null){
					 
				 }else{
					 Fragment newContent = DecorationViewpagerFragment.getInstance();
						Bundle bundle = new Bundle();
						bundle.putString(Constants.CLASS_NAME, searchEdit.getText().toString());
						bundle.putBoolean(Constants.IS_SEARCH, true);
						newContent.setArguments(bundle);
						
						if (newContent != null)
							switchFragment(newContent);
						isClicked = true;
				 }
			}
		});
	
		
		searchEdit = (TextView)mTotalView.findViewById(R.id.search_edit);
		frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(430), 
				FrameLayout.LayoutParams.WRAP_CONTENT);
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(85);
		searchEdit.setLayoutParams(frameLp);
		//搜索布局结束
		
		//tab布局开始
		FrameLayout menuTab =(FrameLayout)mTotalView.findViewById(R.id.menu_tab);
		menuTab.setLayoutParams(linearLp);
		
		LinearLayout menuDeco = (LinearLayout)mTotalView.findViewById(R.id.menu_deco);
		
		menuDeco.setOnClickListener(clickListener);
		LinearLayout menuHouse = (LinearLayout)mTotalView.findViewById(R.id.menu_house);
		menuHouse.setOnClickListener(clickListener);
		
		ImageView tabDecoIcon = (ImageView)mTotalView.findViewById(R.id.tab_deco_icon);
		
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(47),
				DecorationApplication.cpc.changeImageX(55));
		linearLp.leftMargin= DecorationApplication.cpc.changeImageX(50);
		tabDecoIcon.setLayoutParams(linearLp);
	//tabDecoIcon.setLayoutParams(Constants.getFrameLp(47, 55, 50, 28));
		tabDecoIcon.setOnClickListener(clickListener);
		
		ImageView tabHouseIcon = (ImageView)mTotalView.findViewById(R.id.tab_house_icon);
//		tabHouseIcon.setLayoutParams(Constants.getFrameLp(44, 55, 410, 28));
		linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(44),
				DecorationApplication.cpc.changeImageX(55));
		linearLp.leftMargin= DecorationApplication.cpc.changeImageX(70);
		tabHouseIcon.setLayoutParams(linearLp);
		tabHouseIcon.setOnClickListener(clickListener);
		
		tabBelow = (ImageView)mTotalView.findViewById(R.id.tab_below);
		frameLp = new FrameLayout.LayoutParams(DecorationApplication.cpc.changeImageX(340), 
				DecorationApplication.cpc.changeImageX(2));
		frameLp.gravity = Gravity.BOTTOM|Gravity.LEFT;
		frameLp.bottomMargin = DecorationApplication.cpc.changeImageX(6);
		//tabBelow.setLayoutParams(Constants.getFrameLp(327, 2, 0, 95));
		tabBelow.setLayoutParams(frameLp);
		
		TextView tabDecoTxt = (TextView)mTotalView.findViewById(R.id.tab_deco_txt);
		linearLp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		
		linearLp.leftMargin= DecorationApplication.cpc.changeImageX(50);
		tabDecoTxt.setLayoutParams(linearLp);
		//tabDecoTxt.setLayoutParams(Constants.getFrameLp(170, 
		//		FrameLayout.LayoutParams.WRAP_CONTENT, 150, 30));
		tabDecoTxt.setOnClickListener(clickListener);
		
		TextView tabHouseTxt = (TextView)mTotalView.findViewById(R.id.tab_house_txt);
		linearLp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		
		linearLp.leftMargin= DecorationApplication.cpc.changeImageX(40);
		tabHouseTxt.setLayoutParams(linearLp);
	//	tabHouseTxt.setLayoutParams(Constants.getFrameLp(FrameLayout.LayoutParams.MATCH_PARENT, 
	//			FrameLayout.LayoutParams.WRAP_CONTENT, 485, 30));
		tabHouseTxt.setOnClickListener(clickListener);

		//tab布局结束
		menuList = (ListView)mTotalView.findViewById(R.id.menu_list);
	
		
	}
	
	 OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TranslateAnimation transAnim;
			switch(v.getId()){
			    case R.id.tab_house_txt:
			    case R.id.tab_house_icon:
			    case R.id.menu_house:
			    	if(mCurMenuList == MENULIST_DECORATION){
			    		menuList.setAdapter(hMenuAdapter);
				    	transAnim = new TranslateAnimation(0, 
				    			(int)(DecorationApplication.cpc.getCurWidth()*0.41), 0, 0);
				    	transAnim.setFillAfter(true);
				    	transAnim.setDuration(500);
				    	tabBelow.startAnimation(transAnim);
				    	mCurMenuList = MENULIST_HOUSE;
				    	mClickPosition = 0;
			    	}
			    	
			    	break;
			    case R.id.tab_deco_txt:
			    case R.id.tab_deco_icon:
			    case R.id.menu_deco:
			    	if(mCurMenuList == MENULIST_HOUSE){
			    		menuList.setAdapter(dMenuAdapter);
				    	transAnim = new TranslateAnimation((int)(DecorationApplication.cpc.getCurWidth()*0.41), 
				    			0, 0, 0);
				    	transAnim.setFillAfter(true);
				    	transAnim.setDuration(500);
				    	tabBelow.startAnimation(transAnim);
				    	mCurMenuList = MENULIST_DECORATION;
				    	mClickPosition = 0;
			    	}
			    	break;
//			    case R.id.index_txt:
//			    	       
//			    		   Fragment newContent = IndexFragment.getInstance();
//							if (newContent != null)
//								switchFragment(newContent);
//			    	   
//			    	break;
			    default:
			    		
			}
		}
	};
	
	
	
	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}
	
	public class MenuAdapter extends BaseAdapter {

		private List<SampleItem> mMenuItem;
		public MenuAdapter(List<SampleItem> menuItem) {
			// TODO Auto-generated constructor stub
			mMenuItem = menuItem;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMenuItem.size();
		}

		@Override
		public SampleItem getItem(int position) {
			// TODO Auto-generated method stub
			return mMenuItem.get(position);
		}
		

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.d("lyl","position is " + position);
			   if (convertView == null) {
				   convertView = LayoutInflater.from(DMenuFragment.this.getActivity()).inflate(R.layout.row, null);
				}
			   if(position == mClickPosition){
				   convertView.findViewById(R.id.menu_bg).setBackgroundResource(R.drawable.menu_bg_click);
			   }
			   else{
				   convertView.findViewById(R.id.menu_bg).setBackgroundColor(Color.parseColor("#4d4d4d"));
			   }
			   
			   LinearLayout menuBg = (LinearLayout) convertView.findViewById(R.id.menu_bg);
			   
			   AbsListView.LayoutParams  listLp = new AbsListView.LayoutParams(DecorationApplication.cpc.changeImageX(679),
					   DecorationApplication.cpc.changeImageX(95) );
			    menuBg.setLayoutParams(listLp);
			   
				ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
				
				LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(64),
						DecorationApplication.cpc.changeImageX(65));
				linearLp.leftMargin = DecorationApplication.cpc.changeImageX(45);
				icon.setLayoutParams(linearLp);
				icon.setImageResource(getItem(position).iconRes);
				
				TextView title = (TextView) convertView.findViewById(R.id.row_title);
				linearLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				linearLp.leftMargin = DecorationApplication.cpc.changeImageX(40);
				title.setLayoutParams(linearLp);
				
				title.setText(getItem(position).tag);

				return convertView;
		}
		
		
	}
	@Override
	public void onItemClick(AdapterView<?> lv, View v, int position, long id) {
		// TODO Auto-generated method stub
		mClickPosition = position;
		if(mCurMenuList==MENULIST_DECORATION){
			dMenuAdapter.notifyDataSetChanged();
		}else{
			hMenuAdapter.notifyDataSetChanged();
		}
		Fragment newContent = DecorationViewpagerFragment.getInstance();
		Bundle bundle = new Bundle();
		TextView className = (TextView)v.findViewById(R.id.row_title);
	 
		bundle.putString(Constants.CLASS_NAME, className.getText().toString());
		bundle.putBoolean(Constants.IS_SEARCH, false);
		
		newContent.setArguments(bundle);
		
		if (newContent != null)
			switchFragment(newContent);
		
		isClicked =true;
		
		
	}
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragment);
		}
	}
	
	public void setAvatar(){
		new Thread(readImgRun).start();
	}
	public void delAvatar(){
		
		indexIcon.setImageResource(R.drawable.menu_index_icon);
	}
	Bitmap bitmap;
	 Runnable readImgRun = new Runnable(){

			@Override

			public void run() {

			// TODO Auto-generated method stub
			 	 try{
			 		 URL  url=null;
			 		 url = new URL(DecorationApplication.SERVER_NAME+"index.php?m=getPic&t=1&id="+
			 		    		  UserInfo.getInstance().getUserId() +"&w="+DecorationApplication.cpc.changeImageX(87));

             	   HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect(); 
                    InputStream inputStream=conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream); 
                    Message msg=new Message();
                    msg.what=1;
                    imghandler.sendMessage(msg);                
             
              } catch (MalformedURLException e1) { 
                  e1.printStackTrace();
              } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
			}
			};
			 /**这里重写handleMessage方法，接受到子线程数据后更新UI**/
		    private Handler imghandler=new Handler(){
		        @Override
		        public void handleMessage(Message msg){
		          
		            switch(msg.what){
		            case 1:
		                //关闭
		                if(indexIcon!=null){
		                	indexIcon.setImageBitmap(Constants.getRoundedCornerBitmap(bitmap,PIC_ROUND_RADIUS));
		                }
		                break;
		            
		            }
		        }
		    };
}
