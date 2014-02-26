package com.gren.decoration;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gren.adapter.StaggeredAdapter;
import com.gren.decoration.model.DecorationInfo;
import com.gren.util.Constants;
import com.gren.util.CustomerHttpClient;
import com.gren.util.PullToRefreshView;
import com.gren.util.PullToRefreshView.OnHeaderRefreshListener;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnScrollListener;

/*
 * 显示项
 */

public class DecorationListFragment extends Fragment implements OnHeaderRefreshListener {

	private int mPos = -1;
	//private int mImgRes;
	//private static DecorationListFragment dlf;
	
	//private ImageFetcher mImageFetcher;
    private StaggeredAdapter mAdapter;
   // private ContentTask task = new ContentTask(this.getActivity(), 2);
    PullToRefreshView mPullToRefreshView;
    StaggeredGridView gridView;
    
    private int totalPageNum;
    private int curPage=1;
    private int totalDecorationNum;

    MainActivity mContainerActivity;
    
    
    private ImageView mNetworkError;
    private ImageView mLoadingImg;  
    private LinearLayout mLoadingLinear;
    private TextView mBottomLoading;
    
    private String mClassName;  //分类名称
    
    private String mClassNameOr; //两个分类时
    
    
    boolean isLoading = false;
    
  //  int mListPos;
    
    int mCategory;   //小分类名称   .
    
    boolean mIsHeaderRefreshing = false;
    
    private boolean mIsSearch;
	
    public DecorationListFragment() {
    	
    	
	}
//	public DecorationListFragment(MainActivity main,String className,int category) {
//	
//		mContainerActivity = main;
//		mCategory = category;
//		mClassName = className;
//	}
	
	public DecorationListFragment(int pos) {
		mPos = pos;
	}
	
	public static DecorationListFragment newInstance(int category){
		DecorationListFragment dlf = new DecorationListFragment();
		// final Bundle args = new Bundle();
	   //  args.putString(Constants.CLASS_NAME, className);
	  //   args.putInt(Constants.CATEGORY, category);
	//	dlf.setArguments(args);
		return  dlf;
		//new DecorationListFragment(main,className,category);
	}
	
	
//	public static DecorationListFragment getInstance(String imageUrl){
//		if(dlf == null){
//			dlf = new DecorationListFragment();
//		//	 final Bundle args = new Bundle();
//		 //    args.putString(IMAGE_DATA_EXTRA, imageUrl);
//		 //    f.setArguments(args);
//		}
//		return dlf;
//	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 // setContentView(R.layout.activity_main);
	  //      mImageFetcher = new ImageFetcher(this.getActivity(), 240);
		//Log.d("lyl","DecorationListFragment"+DecorationListFragment.this.getId()+ "onCreate");
	      //  mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mContainerActivity = (MainActivity)this.getActivity();
		
		//Log.d("lyl","className is " + this.getArguments().getString(Constants.CLASS_NAME));
		
		//mClassName = DecorationApplication.className;
		mClassName = this.getArguments().getString(Constants.CLASS_NAME);
		
		mIsSearch = this.getArguments().getBoolean(Constants.IS_SEARCH);
		
		String[] className ;
		if(mClassName.contains("/")){
			className = mClassName.split("/");
			if(className.length==2){
				mClassName = className[0];
				mClassNameOr = className[1];
			}
			
		}
		
			//this.getArguments().getString(Constants.CLASS_NAME);
		//mClassName = "卫浴";
		//mCategory = 0;
    	mCategory  = this.getArguments().getInt(Constants.CATEGORY,0);
     
    	
    	//readInfoHandler.post(readInfoRun);
    	readInfoThread = new Thread(readInfoRun);
    	readInfoThread.start();
	}
	Thread readInfoThread;
	Runnable readInfoRun = new Runnable(){

		@Override

		public void run() {

		// TODO Auto-generated method stub

			    String urlStr = "" ;
//			    if(User.type == LoginFragment.ACCOUNT_TYPE_SYS){
			//    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=tablesli&t=decoration&page=1&wv=卫浴&wk=class_name&o=my_price";
//			    }
//			    else{
//			    	urlStr= "http://shouji400.com/fcliaoning/index.php?m=tables&t=open&id=" + User.id;
//			    }
			    if(mIsHeaderRefreshing){
					
			    	curPage=1;
		        }
			    if(mIsSearch){
			    	  if(mCategory == 0){
					    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=tablesli&t=decoration&page="
					    	+curPage+"&search="+mClassName+"&sd=title&o=praise&totalCount="+DecorationApplication.pageNum;
					    }else if(mCategory == 1){
					    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=tablesli&t=decoration&page="
					    	+curPage+"&search="+mClassName+"&sd=title&o=my_price&totalCount="+DecorationApplication.pageNum;;
					    }else {
					    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=tablesli&t=decoration&page="
					    	+curPage+"&search="+mClassName+"&sd=title&o=update&totalCount="+DecorationApplication.pageNum;
					    }
			    }else{
			    	  if(mCategory == 0){
					    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=decorationlist&page="
					    	+curPage+"&wv="+mClassName+"&wk=class_name&o=praise&totalCount="+DecorationApplication.pageNum;
					    }else if(mCategory == 1){
					    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=decorationlist&page="
					    	+curPage+"&wv="+mClassName+"&wk=class_name&o=my_price&totalCount="+DecorationApplication.pageNum;;
					    }else {
					    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=decorationlist&page="
					    	+curPage+"&wv="+mClassName+"&wk=class_name&o=update&totalCount="+DecorationApplication.pageNum;
					    }
					    
						if(mClassNameOr!=null){
							urlStr+="&wv_or="+mClassNameOr;
						}
			    }
			  
        	   
				
				
			    String response = CustomerHttpClient.post(urlStr);
			   // Log.d("lyl","response is " + response);
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
							if(mIsHeaderRefreshing||curPage == 1){
								
								if(mCategory == 0){
									DecorationApplication.decorationPraiseList.clear();
									
								}else if(mCategory == 1){
									DecorationApplication.decorationPriceList.clear();
								}else{
									DecorationApplication.decorationTimeList.clear();
								}
								 
								
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
								
								//tempInfo.setImgWidth();
								
								
								 
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
							//	tempInfo.setTitle(decorationJson.getString(DecorationInfo.TITLE));
							//	tempInfo.setTitle(decorationJson.getString(DecorationInfo.TITLE));
								
									if(mCategory == 0){
										DecorationApplication.decorationPraiseList.add(tempInfo);
										
									}else if(mCategory == 1){
										DecorationApplication.decorationPriceList.add(tempInfo);
									}else{
										DecorationApplication.decorationTimeList.add(tempInfo);
									}
								
							
								
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
	            	if(mIsHeaderRefreshing){
	                  	 Animation listviewAnim =new TranslateAnimation(0, 0,  mPullToRefreshView.getHeaderViewHeight(),0);
	      					listviewAnim.setFillAfter(true);
	      					listviewAnim.setDuration(300);
	      					gridView.startAnimation(listviewAnim);
	      					mPullToRefreshView.getHeaderView().startAnimation(listviewAnim);
	      				    mPullToRefreshView.onHeaderRefreshComplete();
	      				    mIsHeaderRefreshing = false;
	                  	}
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
	            	
	            	 if(mIsHeaderRefreshing){
	            		mIsHeaderRefreshing = false;          			
	            			  Animation listviewAnim =new TranslateAnimation(0, 0,  mPullToRefreshView.getHeaderViewHeight(),0);
	            				listviewAnim.setFillAfter(true);
	            				listviewAnim.setDuration(300);
	            				gridView.startAnimation(listviewAnim);
	            				mPullToRefreshView.getHeaderView().startAnimation(listviewAnim);
	            			mPullToRefreshView.onHeaderRefreshComplete();
	            			
	            		}
	            	break;
	            }
	        }
	    };
	    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.from(this.getActivity()).inflate(R.layout.gridviewpager, null);
		
		mLoadingImg = (ImageView)v.findViewById(R.id.loading);
		mLoadingImg.setScaleType(ScaleType.CENTER_INSIDE);
		
		
		
		RotateAnimation loadingAnim = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		  LinearInterpolator lir = new LinearInterpolator();
		loadingAnim.setInterpolator(lir);
		loadingAnim.setDuration(1000);
		loadingAnim.setRepeatCount(-1); 
		mLoadingImg.startAnimation(loadingAnim);
		
		mLoadingLinear = (LinearLayout)v.findViewById(R.id.loading_linear);
		
		mNetworkError = (ImageView)v.findViewById(R.id.network_error);
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
		
		 mBottomLoading = (TextView)v.findViewById(R.id.bottom_loading);
		 
		gridView = (StaggeredGridView) v.findViewById(R.id.staggeredGridView1);

		 mPullToRefreshView = (PullToRefreshView)v.findViewById(R.id.main_pull_refresh_view);
		 mPullToRefreshView.setOnHeaderRefreshListener(this);
		 
		 int margin = getResources().getDimensionPixelSize(R.dimen.margin);

	        gridView.setItemMargin(2,2,2,2); // set the GridView margin
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
				//	Log.d("lyl","firstVisibleItem is " + firstVisibleItem);
				//	Log.d("lyl","visibleItemCount is " + visibleItemCount);
			//		Log.d("lyl","totalItemCount is " + totalItemCount);
				//	Log.d("lyl","curPage is " + curPage);
				//	Log.d("lyl","totalPageNum is " + totalPageNum);
					
				//	Log.d("lyl","isLoading is " + isLoading);
					
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

	        mAdapter = new StaggeredAdapter(this.getActivity(),mContainerActivity.getImageFetcher(),mCategory);
	        gridView.setAdapter(mAdapter);
	       // mAdapter.notifyDataSetChanged();
	        gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
				
				@Override
				public void onItemClick(StaggeredGridView parent, View view, int position,
						long id) {
					// TODO Auto-generated method stub
					if (getActivity() == null)
						return;
					MainActivity activity = (MainActivity) getActivity();
			//		activity.onItemPressed(mPos);
					activity.onItemPressed(position,mCategory);
				}
			});
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mPos", mPos);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//设置更新时间
				//mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				 Animation listviewAnim =new TranslateAnimation(0, 0,  mPullToRefreshView.getHeaderViewHeight(),0);
					listviewAnim.setFillAfter(true);
					listviewAnim.setDuration(300);
					gridView.startAnimation(listviewAnim);
					mPullToRefreshView.getHeaderView().startAnimation(listviewAnim);
					
			}
		},800);
	
	
//	mIsHeaderRefreshing = true; 
//	new Thread(readInfoRun).start();
	}	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//readInfoHandler.removeCallbacks(readInfoRun);
		//DecorationListFragment.this.getActivity().de
		super.onDestroy();
	}
}
