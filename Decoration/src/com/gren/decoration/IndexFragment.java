package com.gren.decoration;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gren.adapter.IndexStaggeredAdapter;
import com.gren.decoration.model.DecorationInfo;
import com.gren.util.CustomerHttpClient;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnScrollListener;

public class IndexFragment extends Fragment{

	private View mTotalView;
	private ImageView topLogin;
	private ImageView topMenu;
	private MainActivity mContainer;
	StaggeredGridView gridView;
	private int totalPageNum;
    private int curPage=1;
    private int totalDecorationNum;
    
    private ImageView mNetworkError;
    private ImageView mLoadingImg;  
    private LinearLayout mLoadingLinear;
    private TextView mBottomLoading;
    
    private IndexStaggeredAdapter mAdapter;
    boolean isLoading = false;    
    
    
	private static IndexFragment indexFragment = new IndexFragment();
   // private static IndexFragment indexFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	        
		DecorationApplication.decorationHotList.clear();
		curPage=1;
		mContainer = (MainActivity)this.getActivity();
		new Thread(readInfoRun).start();
		
		
	}
	public static IndexFragment getInstance(){
		
			return indexFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 
		mTotalView = inflater.inflate(R.layout.index_frame, null);
		
		initView();
		setListener();
		return mTotalView;
	}
	
	private void initView(){
		FrameLayout topBg = (FrameLayout)mTotalView.findViewById(R.id.top_bg);
		
//		AutoScrollTextView autoScrollTextView = (AutoScrollTextView)mTotalView.findViewById(R.id.textview_notice);
//	    autoScrollTextView.init(IndexFragment.this.getActivity().getWindowManager());
//	    autoScrollTextView.startScroll();
			
		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(802),
				DecorationApplication.cpc.changeImageX(109));
		topBg.setLayoutParams(linearLp);
		
		topLogin = (ImageView)mTotalView.findViewById(R.id.top_login);
		topMenu = (ImageView)mTotalView.findViewById(R.id.top_menu);
		
		LayoutParams topMenuLp = new LayoutParams(DecorationApplication.cpc.changeImageX(64),
				DecorationApplication.cpc.changeImageX(64));
		topMenuLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		topMenuLp.leftMargin = DecorationApplication.cpc.changeImageX(32);
		topMenu.setLayoutParams(topMenuLp);
		
		topMenuLp = new LayoutParams(DecorationApplication.cpc.changeImageX(74),
				DecorationApplication.cpc.changeImageX(141));  //48,52    5 40
		
		topMenuLp.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
		topMenuLp.rightMargin = DecorationApplication.cpc.changeImageX(30);
		topLogin.setLayoutParams(topMenuLp);
		topLogin.setPadding(DecorationApplication.cpc.changeImageX(5), DecorationApplication.cpc.changeImageX(40), 
				DecorationApplication.cpc.changeImageX(5), DecorationApplication.cpc.changeImageX(40));
		
		mLoadingImg = (ImageView)mTotalView.findViewById(R.id.loading);
		mLoadingImg.setScaleType(ScaleType.CENTER_INSIDE);
		
		RotateAnimation loadingAnim = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		  LinearInterpolator lir = new LinearInterpolator();
		loadingAnim.setInterpolator(lir);
		loadingAnim.setDuration(1000);
		loadingAnim.setRepeatCount(-1); 
		mLoadingImg.startAnimation(loadingAnim);
		
		mLoadingLinear = (LinearLayout)mTotalView.findViewById(R.id.loading_linear);
		
		mNetworkError = (ImageView)mTotalView.findViewById(R.id.network_error);
		mNetworkError.setScaleType(ScaleType.CENTER_INSIDE);
		mNetworkError.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 new Thread(readInfoRun).start();
				 mNetworkError.setVisibility(View.GONE);
				 RotateAnimation loadingAnim = new RotateAnimation(0, 360,
							Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
							0.5f);
					  LinearInterpolator lir = new LinearInterpolator();
					loadingAnim.setInterpolator(lir);
					loadingAnim.setDuration(1000);
					loadingAnim.setRepeatCount(-1); 
				 mLoadingImg.startAnimation(loadingAnim);
			}
		});
		
		 mBottomLoading = (TextView)mTotalView.findViewById(R.id.bottom_loading);
		 
		 gridView = (StaggeredGridView)mTotalView.findViewById(R.id.staggeredGridView1);
 
		  gridView.setItemMargin(2,0,2,2); // set the GridView margin
		  
	      gridView.setFastScrollEnabled(false);
	      gridView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(StaggeredGridView view, int scrollState) {
					// TODO Auto-generated method stub
					//mListPos=view.getFirstPosition();
					//Log.d("lyl","ListPos is "  + mListPos);
				} 
				
				@Override
				public void onScroll(StaggeredGridView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
					if(!isLoading&&totalItemCount!=0&&firstVisibleItem+visibleItemCount == totalItemCount&&curPage<totalPageNum){
						Log.d("lyl","到底了!");
						
						isLoading = true;
						 mBottomLoading.setVisibility(View.VISIBLE);
						curPage++;
						new Thread(readInfoRun).start();
					   //  mAdapter.notifyDataSetChanged();
					  
					}
					
				}
			});
	      
	        mAdapter = new IndexStaggeredAdapter(this.getActivity(),mContainer.getImageFetcher());
	        
	       
	       // gridView.setFastScrollEnabled(true);
	        gridView.setAdapter(mAdapter);
           gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
				
				@Override
				public void onItemClick(StaggeredGridView parent, View view, int position,
						long id) {
					// TODO Auto-generated method stub
					if(position ==0 ){
						return;
					}
					if (getActivity() == null)
						return;
					
					MainActivity activity = (MainActivity) getActivity();
					
			//		activity.onItemPressed(mPos);
					activity.onItemPressed(position-1,4);
				}
			});
		 
	}
	private void setListener(){
		topLogin.setOnClickListener(onViewpagerClick);
		topMenu.setOnClickListener(onViewpagerClick);
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
				DecorationApplication d = (DecorationApplication)IndexFragment.this.getActivity().getApplication();
				if(d.checkIsLogin()){
					Intent intent = new Intent(IndexFragment.this.getActivity(), PersonalActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(IndexFragment.this.getActivity(), LoginFragmentActivity.class);
					startActivity(intent);
					IndexFragment.this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
				
				break;
			}
		}
	};
		
	Runnable readInfoRun = new Runnable(){

		@Override

		public void run() {

		// TODO Auto-generated method stub

			    String urlStr = "" ;

//			    if(mIsHeaderRefreshing){
//					
//			    	curPage=1;
//		        }
			  

		    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=tablesli&page="
		    	+curPage+"&t=decoration&o=praise&totalCount="+DecorationApplication.pageNum;
					  
			    String response = CustomerHttpClient.post(urlStr);
			    Log.d("lyl","response is " + response);
			    if(response.contains("error")){
			    	 Message m = new Message();
		        	  //  Bundle responseBundle = new Bundle();
		        	   // responseBundle.putString("response", response);
		        	  //  m.setData(responseBundle);
		        	    m.what = 1;
		        	    readInfoHandler.sendMessage(m);
			    }else{
			    	  try {
							JSONObject jo = new JSONObject(response);
							
							if (jo.has("viewpage")&&jo.length()>2) {
								totalPageNum = jo.getJSONObject("viewpage")
										.getJSONObject("infoPage").getInt("pagefrom");
							//	curPage = jo.getJSONObject("viewpage")
							//			.getJSONObject("infoPage").getInt("pagesize");
								totalDecorationNum = jo.getJSONObject("viewpage")
										.getJSONObject("infoPage").getInt("total");
							} else {
								totalDecorationNum = jo.length() - 1;    //如果没有
							}
							String tempImgurl;
							if(curPage == 1){							
								DecorationApplication.decorationHotList.clear();
					        }
							for (int i = 0; i < jo.length() - 2; i++) {
								JSONObject decorationJson = jo.getJSONObject(String.valueOf(i));
								DecorationInfo tempInfo = new DecorationInfo();
								tempInfo.setTitle(decorationJson.getString(DecorationInfo.TITLE));
								tempInfo.setDecorationId(decorationJson.getString(DecorationInfo.DECORATION_ID));
								tempInfo.setClassName(decorationJson.getString(DecorationInfo.CLASS_NAME));
								tempInfo.setPublicPrice(decorationJson.getInt(DecorationInfo.PUBLIC_PRICE));
								tempInfo.setMyPrice(decorationJson.getInt(DecorationInfo.MY_PRICE));
								tempImgurl =	DecorationApplication.SERVER_NAME
								+ "img.php?src=uploadfile/decoration/"
								+ decorationJson.getString(DecorationInfo.DECORATION_ID) + "/" + decorationJson.getString(DecorationInfo.IMG_URL);
								//+ "&w=" + DecorationApplication.cpc.getCurWidth();
								tempInfo.setImgUrl(tempImgurl);
								 
								 
//								tempInfo.setImgHeight(
//										(int)(decorationJson.getJSONObject("getimagesize").getInt("1")
//										* DecorationApplication.cpc.getCurWidth()/2) /
//										decorationJson.getJSONObject("getimagesize").getInt("0")
//										);
								tempInfo.setImgHeight(decorationJson.getJSONObject("getimagesize").getInt("1"));
								tempInfo.setImgWidth(decorationJson.getJSONObject("getimagesize").getInt("0"));
								
								
								tempInfo.setIntroduce(decorationJson.getString(DecorationInfo.INTRODUCE));
								tempInfo.setPraise(decorationJson.getInt(DecorationInfo.PRAISE));
								tempInfo.setMerchantName(decorationJson.getString(DecorationInfo.MERCHANT_NAME));
								tempInfo.setMerchantAddress(decorationJson.getString(DecorationInfo.MERCHANT_ADDRESS));
								tempInfo.setMerchantTel(decorationJson.getString(DecorationInfo.MERCHANT_TEL));
								tempInfo.setMerchantIntro(decorationJson.getString(DecorationInfo.MERCHANT_INTRO));
	
								DecorationApplication.decorationHotList.add(tempInfo);
							}
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
		
		/**这里重写handleMessage方法，接受到子线程数据后更新UI**/
	    private Handler readInfoHandler=new Handler(){
	        @Override
	        public void handleMessage(Message msg){
	            
	            switch(msg.what){
	            
	            case 0:
	                //关闭
	            //	faceImage.setImageBitmap(bitmap);
	            	  mLoadingImg.clearAnimation();
	            	 // mLoadingImg.setVisibility(View.INVISIBLE);
	            	  mLoadingLinear.setVisibility(View.GONE);
	            	//Toast.makeText(mContainerActivity, "读取数据成功", Toast.LENGTH_SHORT).show();
	            	  mAdapter.notifyDataSetChanged();
	            	//  gridView.setSelection(mListPos);
	            	  mBottomLoading.setVisibility(View.GONE);
	            	isLoading = false;
	            	break;
	            case 1:
	                //关闭
	            //	faceImage.setImageBitmap(bitmap);
	            //	Toast.makeText(mContainerActivity, "读取数据失败", Toast.LENGTH_SHORT).show();
	            	mLoadingImg.clearAnimation();
	            	mLoadingImg.setVisibility(View.GONE);
	            	mLoadingLinear.setVisibility(View.VISIBLE);
	            	
	            	 mBottomLoading.setVisibility(View.GONE);
	            	
	                mNetworkError.setVisibility(View.VISIBLE);
	            //	mAdapter.notifyDataSetChanged();
	            	isLoading = false;
	            	
	            	break;
	            }
	        }
	    };
}
