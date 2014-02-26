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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView.ScaleType;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker;
import com.example.android.bitmapfun.util.Utils;
import com.gren.adapter.CommentAdapter;
import com.gren.decoration.CommentActivity.CommentModel;
import com.gren.decoration.model.DecorationInfo;
import com.gren.decoration.model.UserInfo;
import com.gren.util.Constants;
import com.gren.util.CustomerHttpClient;

/**
 * This fragment will populate the children of the ViewPager from {@link DecorationDetailActivity}.
 */
public class DecorationDetailFragment extends Fragment {
   // private static final String IMAGE_DATA_EXTRA = "extra_image_data";
   
    private String mImageUrl;
    private ImageView mImageView;
    private ImageFetcher mImageFetcher;

    private int mCategory;
    private int mPosition;
    
    private ImageView mPraiseImg;
    private RelativeLayout mPraiseFrame;
   // private TextView commentNumTxt;
    int mIsPraise;
	String mCommentNum ;
//	TextView mPraiseNum;
	boolean isPraiseRunning=false;
    
    DecorationInfo decorationInfo ;
    
    ImageView mLoadingImg;
    LinearLayout mLoadingLinear;
    ImageView mNetworkError;
    
    //底部评论区域
    FrameLayout commentContainer;
    Boolean isRefresh=false;
   
    /**
	 * 每层楼显示数据的集合
	 */
	private List<Map<String, Object>> mLastUserList = null;
	private List<Map<String, Object>> mLastUser = null;
	
	public final static String DECORATION_ID = "decoration_id"; 
	public final static String TABLE_NAME = "table_name"; 
	
	 private View v;
	 private  int mCurPage;
	 private  int mTotalPage;
	 private boolean mIsLoading = false;
	// private View footerView;
     private CommentAdapter mCommentAdapter;
     ListView mCommentListView;
     
     private String mNewsId;
     private String mCommentReplyId;
     
     private TextView mTxtMessage;
     private String mTableName="decoration"; //调用的评论对应的表名，新闻表，图片表等
	 
	 private final static int COMMENTNUM_PERPAGE = 5;//每页显示的评论数
	 EditText mWriteComment;
	 FrameLayout bottomFrame;
	 
	 private boolean isCommentContainerShow = false;
	 
	
    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param imageUrl The image url to load
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
//    public static DecorationDetailFragment newInstance(String imageUrl) {
//        final DecorationDetailFragment f = new DecorationDetailFragment();
//
//        final Bundle args = new Bundle();
//        args.putString(IMAGE_DATA_EXTRA, imageUrl);
//        f.setArguments(args);
//
//        return f;
//    }

    public static DecorationDetailFragment newInstance(int position,int category) {
        final DecorationDetailFragment f = new DecorationDetailFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.POSITION_DATA_EXTRA, position);
        args.putInt(Constants.CATEGORY, category);
        f.setArguments(args);

        return f;
    }
    /**
     * Empty constructor as per the Fragment documentation
     */
    public DecorationDetailFragment() {}

    /**
     * Populate image using a url from extras, use the convenience factory method
     * {@link DecorationDetailFragment#newInstance(String)} to create this fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCommentContainerShow = false;
      //  mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
       mCategory = getArguments() != null ? getArguments().getInt(Constants.CATEGORY) : 0;
       mPosition = getArguments() != null ? getArguments().getInt(Constants.POSITION_DATA_EXTRA) : 0;
      
       if(mCategory == 0){
       	 decorationInfo = DecorationApplication.decorationPraiseList.get(mPosition);
       }else if(mCategory == 1){
       	 decorationInfo = DecorationApplication.decorationPriceList.get(mPosition);
       }else if(mCategory == 2){
       	 decorationInfo = DecorationApplication.decorationTimeList.get(mPosition);
       }else if(mCategory == 3){
         decorationInfo = DecorationApplication.decorationCollectList.get(mPosition);
       }else if(mCategory == 4){
    	   decorationInfo = DecorationApplication.decorationHotList.get(mPosition);
       }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.decoration_detail_frame, container, false);
        
        
        
        
        
//        TextView title = (TextView)v.findViewById(R.id.title);
//        TextView intro = (TextView)v.findViewById(R.id.introduce);
//        TextView publicPrice = (TextView)v.findViewById(R.id.public_price);
//        TextView myPrice = (TextView)v.findViewById(R.id.my_price);
//        mPraiseNum = (TextView)v.findViewById(R.id.praise_num);
//        TextView telNum = (TextView)v.findViewById(R.id.tel);
//        TextView location = (TextView)v.findViewById(R.id.location_txt);
        
    	mLoadingImg = (ImageView)v.findViewById(R.id.loading);
		mLoadingImg.setScaleType(ScaleType.CENTER_INSIDE);
		mLoadingLinear = (LinearLayout)v.findViewById(R.id.loading_linear);
		
		
		
		
		RotateAnimation loadingAnim = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		  LinearInterpolator lir = new LinearInterpolator();
		loadingAnim.setInterpolator(lir);
		loadingAnim.setDuration(1000);
		loadingAnim.setRepeatCount(-1); 
		mLoadingImg.startAnimation(loadingAnim);
    	
		mNetworkError = (ImageView)v.findViewById(R.id.network_error);
		mNetworkError.setScaleType(ScaleType.CENTER_INSIDE);
		 mNetworkError.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
			//	new Thread(isPraseAndCommentNumRun).start();
				
				 new Thread(getCommentThread).start();
				 
				 mNetworkError.setVisibility(View.GONE);
				// mLoadingImg.setVisibility(View.VISIBLE);
			//	 mLoadingLinear.setVisibility(View.VISIBLE);
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
		
		
		
		
     //   commentNumTxt = (TextView)v.findViewById(R.id.comment_num);
       
        
//        mPraiseFrame = (RelativeLayout)v.findViewById(R.id.praise_frame);
        
        initBottomFrame();
        mTxtMessage   = (TextView)v.findViewById(R.id.message);
        mCommentListView = (ListView)v.findViewById(R.id.detail_list);
        mNewsId = decorationInfo.getDecorationId();
        
    	mCommentReplyId = "0";    //默认为0
    	mCurPage = 1;
    	mLastUserList = new ArrayList<Map<String, Object>>();
    	mLastUser= new ArrayList<Map<String, Object>>();
    	
    	Log.d("lyl","mComentListView is " + mCommentListView);
    	mCommentAdapter = new CommentAdapter(DecorationDetailFragment.this, mLastUser,decorationInfo);
    	
    	mCommentListView.setAdapter(mCommentAdapter);
    	mCommentListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getLastVisiblePosition() > 0 && view.getLastVisiblePosition() >= totalItemCount-1 && mCurPage < mTotalPage) {
					loadMoreItems();
				}
				else if(mCurPage == mTotalPage){
					
				}
			}
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
		});
    	 new Thread(getCommentThread).start();
        
//        title.setText(decorationInfo.getTitle());
//        intro.setText(decorationInfo.getIntroduce());
//        publicPrice.setText("￥"+decorationInfo.getPublicPrice());
//        myPrice.setText("￥"+decorationInfo.getMyPrice());
//        mPraiseNum.setText("喜欢("+decorationInfo.getPraise()+")");
//        
//        if(decorationInfo.getMerchantTel().equals("")||decorationInfo.getMerchantTel()==null){
//        	telNum.setVisibility(View.GONE);
//        }else{
//        	telNum.setText(decorationInfo.getMerchantTel());
//        }
//        //联系厂商
//        telNum.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				connectMerchant();
//			}
//		});
//        location.setText(decorationInfo.getMerchantAddress());
    	 
//        FrameLayout.LayoutParams lp =null;
    	 
//        if(decorationInfo.getImgWidth()>DecorationApplication.cpc.getCurWidth()){
//        	int height =(int)(decorationInfo.getImgHeight()*DecorationApplication.cpc.getCurWidth()/decorationInfo.getImgWidth());
//        	 lp = new FrameLayout.LayoutParams((int)DecorationApplication.cpc.getCurWidth(),
//        			 height);
//        }else{
//        	 lp = new FrameLayout.LayoutParams((int)DecorationApplication.cpc.getCurWidth(),
//        			 (int)(DecorationApplication.cpc.getCurWidth()*0.75));
//        }
        
   ;
        
//        mImageView = (ImageView) v.findViewById(R.id.imageView);
//        lp.gravity = Gravity.TOP;
//        mImageView.setLayoutParams(lp);
//        
        mPraiseImg = (ImageView)v.findViewById(R.id.praise);
     
//        
//        
        LinearLayout.LayoutParams linealp = new LinearLayout.LayoutParams((int)DecorationApplication.cpc.changeImageX(41),
        		(int)DecorationApplication.cpc.changeImageX(35));
        
         mPraiseImg.setLayoutParams(linealp);
//        
        ImageView commentImg = (ImageView)v.findViewById(R.id.comment);
        linealp = new LinearLayout.LayoutParams((int)DecorationApplication.cpc.changeImageX(41),
       		(int)DecorationApplication.cpc.changeImageX(42));
        
        commentImg.setLayoutParams(linealp);
        
        ImageView moreImg = (ImageView)v.findViewById(R.id.more);
        linealp = new LinearLayout.LayoutParams((int)DecorationApplication.cpc.changeImageX(47),
       		(int)DecorationApplication.cpc.changeImageX(47));
        
        moreImg.setLayoutParams(linealp);
        
//        	
//        RelativeLayout commentFrame = (RelativeLayout)v.findViewById(R.id.comment_frame);
//        commentFrame.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(DecorationDetailFragment.this.getActivity(),CommentActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				intent.putExtra(CommentActivity.DECORATION_ID, decorationInfo.getDecorationId());
//				intent.putExtra(CommentActivity.TABLE_NAME, "decoration");
//			    startActivity(intent);
//			 //   DecorationDetailFragment.this.getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out); 
//			}
//		});
        return v;
        
    }

    
    public boolean isCommentShow(){
    	return isCommentContainerShow;
    }
    
    
    
    private void initBottomFrame(){
    	
    
    	commentContainer = (FrameLayout)v.findViewById(R.id.comment_container);
    	//FrameLayout.LayoutParams framelp = Constants.getFrameLp(800,FrameLayout.LayoutParams.WRAP_CONTENT,0,0);
    	
    	//framelp.gravity = Gravity.BOTTOM;
    	
    	//commentContainer.setLayoutParams(framelp);
    	
    	ImageView commentBg = (ImageView)v.findViewById(R.id.detail_bottom_bg);
    	FrameLayout.LayoutParams framelp = Constants.getFrameLp(800,126,0,0);
    	commentBg.setLayoutParams(framelp);
    	
    	LinearLayout commentSend = (LinearLayout)v.findViewById(R.id.comment_send);
    	framelp = Constants.getFrameLp(151,87,630,30);
    	commentSend.setLayoutParams(framelp);
    	
    	commentSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				  new Thread(commentSendRun).start();
				
			
				 
			} 
		}); 
    	
    	ImageView commentAvatar = (ImageView)v.findViewById(R.id.avatar_com);
    	framelp = Constants.getFrameLp(78,77,20,30);
    	commentAvatar.setLayoutParams(framelp);
    	
    	
    	LinearLayout editLinear = (LinearLayout)v.findViewById(R.id.edit_com_linear);
    	framelp = Constants.getFrameLp(517,FrameLayout.LayoutParams.WRAP_CONTENT,102,45);
    	editLinear.setLayoutParams(framelp);
    	mWriteComment = (EditText)v.findViewById(R.id.edit_com);
    	
    //	commentEdit.setLayoutParams(framelp);
    	
    //	ImageView editBottomline =(ImageView)v.findViewById(R.id.edit_bottomline);
    	//framelp = Constants.getFrameLp(517,11,100,85);
    //	editBottomline.setLayoutParams(framelp);
    	 
    	
    	bottomFrame = (FrameLayout)v.findViewById(R.id.detail_bottom);
        framelp = Constants.getFrameLp(800,126,0,0);
    	framelp.gravity = Gravity.BOTTOM;
    	bottomFrame.setLayoutParams(framelp);
    	
    	LinearLayout praiseLinear = (LinearLayout)v.findViewById(R.id.praise_frame);
    	LinearLayout commentLinear = (LinearLayout)v.findViewById(R.id.comment_frame);
    	LinearLayout moreLinear = (LinearLayout)v.findViewById(R.id.more_frame);
    	moreLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DecorationDetailFragment.this.getActivity(),DetailMoreActivity.class);
                intent.putExtra(DetailMoreActivity.MERCHANT_NAME, decorationInfo.getMerchantName());
                intent.putExtra(DetailMoreActivity.MERCHANT_ADDRESS, decorationInfo.getMerchantAddress());
                intent.putExtra(DetailMoreActivity.MERCHANT_TEL, decorationInfo.getMerchantTel());
			    startActivity(intent);
			}
		});
    	
    	
    	framelp = Constants.getFrameLp(151,87,35,20);
    	praiseLinear.setLayoutParams(framelp);
    	praiseLinear.setOnClickListener(new OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub
   				if(!isPraiseRunning){
   					if(mIsPraise == 1){
   						  new Thread(delPraise).start();
   					}else if(mIsPraise == 2){
   						Message msg = readHandler.obtainMessage();
   	    				msg.what = 4;
   	    				readHandler.sendMessage(msg);
   					}
   					else{
   						   new Thread(addPraise).start();
   					}
   				}
   				
   			}
   		});
    	
    	framelp = Constants.getFrameLp(151,87,220,20);
    	commentLinear.setLayoutParams(framelp);
    	
    	commentLinear.setOnClickListener(new OnClickListener() {
  			
  			@Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
  				commentContainer.setVisibility(View.VISIBLE);
  				bottomFrame.setVisibility(View.INVISIBLE);
  				isCommentContainerShow = true;
  				 
  			}
  		});
    	
    	framelp = Constants.getFrameLp(151,87,612,20);
    	moreLinear.setLayoutParams(framelp);
    	
    
    }
     
   
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Use the parent activity to load the image asynchronously into the ImageView (so a single
        // cache can be used over all pages in the ViewPager
        
//        if (DecorationDetailActivity.class.isInstance(getActivity())) {
            mImageFetcher = ((DecorationDetailActivity) getActivity()).getImageFetcher();
//            mImageFetcher.loadImage(decorationInfo.getImgUrl()+"&w=" + DecorationApplication.cpc.getCurWidth(), mImageView);
//        }
         
        // Pass clicks on the ImageView to the parent activity to handle
        
//        if (OnClickListener.class.isInstance(getActivity()) && Utils.hasHoneycomb()) {
//            mImageView.setOnClickListener((OnClickListener) getActivity());
//        }
        
        new Thread(isPraseAndCommentNumRun).start();
    }
    
    //Thread readIsPraseAndCommentNumThread = new Thread( )
    Runnable isPraseAndCommentNumRun = new Runnable(){
    	public void run() {
    		 String urlStr = DecorationApplication.SERVER_NAME + "index.php?m=isPraiseAndCommentNum";
    	     String response = CustomerHttpClient.post(urlStr
    	    		 ,new BasicNameValuePair("decoration_id",decorationInfo.getDecorationId()),
    	    		 new BasicNameValuePair("member_id",UserInfo.getInstance().getUserId()));
    	     if(response==null||response.equals("error")){
    	    	 Message msg = readHandler.obtainMessage();
 				msg.what = 3;
 				readHandler.sendMessage(msg);
    	     }
    	     
    	     try {
				JSONObject jo = new JSONObject(response);
				mIsPraise = jo.getInt("isPraise");
				mCommentNum = jo.getString("commentNum");
				
				Message msg = readHandler.obtainMessage();
				msg.what = 0;
				readHandler.sendMessage(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	     Log.d("lyl","response is " + response);
    		 
    	};
    };
    
    private Handler  readHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            
            switch(msg.what){
            
            case 0:
                //更新ui
            	//commentNumTxt.setText(mCommentNum+"评论");
            	mCommentAdapter.setCommentText(mCommentNum+"评论");
            	if(mIsPraise == 1){
            		mPraiseImg.setImageBitmap(DecorationApplication.bc.readBitMap(
            				DecorationDetailFragment.this.getActivity(),
            				R.drawable.detail_praise_click, null));
            	//	mPraiseImg.setScaleType(ScaleType.CENTER_INSIDE);
            	 
            		 
            	}
            	if(mLoadingImg!=null){
            		mLoadingImg.clearAnimation();
                //	mLoadingImg.setVisibility(View.GONE);
                	mLoadingLinear.setVisibility(View.GONE);
            	}
            	
            	break;
            case 1:
            	mPraiseImg.setImageBitmap(DecorationApplication.bc.readBitMap(
        				DecorationDetailFragment.this.getActivity(),
        				R.drawable.detail_praise_click, null));
            	//摇摆  
            	final ScaleAnimation animation =new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 
            			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
            	//TranslateAnimation alphaAnimation2 = new TranslateAnimation(-3f, 3f, 0, 0);  
            	animation.setDuration(300);  
            	animation.setRepeatCount(1);  
            	animation.setRepeatMode(Animation.REVERSE);  
            	mPraiseImg.setAnimation(animation);  
            	animation.start();  
            //	mPraiseNum.setText("喜欢("+(decorationInfo.getPraise()+1)+")");
            	mIsPraise = 1;
            	isPraiseRunning = false;
            	Toast.makeText(DecorationDetailFragment.this.getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
            	break;
            case 2:
            	mPraiseImg.setImageBitmap(DecorationApplication.bc.readBitMap(
        				DecorationDetailFragment.this.getActivity(),
        				R.drawable.detail_praise, null));
            	mIsPraise = 0;
            	isPraiseRunning = false;
            	//mPraiseNum.setText("喜欢("+(decorationInfo.getPraise()-1)+")");
            	Toast.makeText(DecorationDetailFragment.this.getActivity(), "取消收藏成功", Toast.LENGTH_SHORT).show();
                break;
            case 3:
            	
            	if(mLoadingImg!=null){
            		mLoadingImg.clearAnimation();
                	mLoadingImg.setVisibility(View.GONE);
                	
            	}
            	mLoadingLinear.setVisibility(View.VISIBLE);
            	mNetworkError.setVisibility(View.VISIBLE);
            	//Toast.makeText(DecorationDetailFragment.this.getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                break;
            case 4:
            	Toast.makeText(DecorationDetailFragment.this.getActivity(), "登陆后才可进行收藏操作哦！", Toast.LENGTH_SHORT).show();
            	break;
            }
        }
    };
    Runnable commentSendRun = new Runnable(){
    	public void run() {
    		Bundle bundle = new Bundle();
    	
    		String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=commentAdd";
			String memberId = UserInfo.getInstance().getUserId();
			if(memberId == null||memberId==""){
			//	memberId = "100091";
			
				Message msg = commentSendHandler.obtainMessage();
				msg.what = 1;
			
				bundle.putString("message", "只有登陆账号的用户才有评论权限，请先到设置界面登陆您的账号");
				msg.setData(bundle);
				
				commentSendHandler.sendMessage(msg);
			    return;
			}
			if(mWriteComment.getText().toString().length()==0){
				Message msg = commentSendHandler.obtainMessage();
				msg.what = 1;
				bundle.putString("message","请输入评论内容");
				msg.setData(bundle);
				commentSendHandler.sendMessage(msg);
				return;
			}
    		String response = CustomerHttpClient.post(urlStr,
					new BasicNameValuePair("member_id",memberId),
					new BasicNameValuePair("reply_id",mCommentReplyId),
					new BasicNameValuePair("table_name",mTableName),
					new BasicNameValuePair("table_id",mNewsId),
					new BasicNameValuePair("content",mWriteComment.getText().toString()));
    		 
		Log.d("lyl","sendcomment response is " + response);
		if(response.equals("error")){
			Message msg = commentSendHandler.obtainMessage();
			msg.what = 1;
			bundle.putString("message", "发送出错，请稍后再试");
			msg.setData(bundle);
			commentSendHandler.sendMessage(msg);
		}else{
			Message msg = readHandler.obtainMessage();
			msg.what = 0;
			commentSendHandler.sendMessage(msg);
		}	
    	}
    };
    private Handler  commentSendHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            
            switch(msg.what){
            
            case 0:
                //更新ui
            	//commentNumT
            	Toast.makeText(DecorationDetailFragment.this.getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
            	isRefresh=true;
            	mWriteComment.setText(null);
            	InputMethodManager imm = (InputMethodManager)DecorationDetailFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mWriteComment.getWindowToken(), 0); 
        		loadRefreshItems();
            	break;
            case 1:
            	Toast.makeText(DecorationDetailFragment.this.getActivity(), msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
            	break;
            }
        }
    };
    
    Runnable addPraise = new Runnable(){
    	public void run() {
    		isPraiseRunning = true;
    		 String urlStr = DecorationApplication.SERVER_NAME + "index.php?m=praiseUp";
    	     String response = CustomerHttpClient.post(urlStr
    	    		 ,new BasicNameValuePair("decoration_id",decorationInfo.getDecorationId()),
    	    		 new BasicNameValuePair("member_id",UserInfo.getInstance().getUserId()));
    	     
    	     try {
				JSONObject jo = new JSONObject(response);
				int status = jo.getInt("status");
                if(status == 1){
                	Message msg = readHandler.obtainMessage();
    				msg.what = 1;
    				readHandler.sendMessage(msg);
                }else if(status == 2){
                	Message msg = readHandler.obtainMessage();
    				msg.what = 4;
    				readHandler.sendMessage(msg);
                }
                else{
                	Message msg = readHandler.obtainMessage();
    				msg.what = 3;
    				readHandler.sendMessage(msg);
                }
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	     Log.d("lyl","response is " + response);
    		 
    	};
    };
    
    Runnable delPraise = new Runnable(){
    	public void run() {
    		isPraiseRunning = true;
    		 String urlStr = DecorationApplication.SERVER_NAME + "index.php?m=praiseDe";
    	     String response = CustomerHttpClient.post(urlStr
    	    		 ,new BasicNameValuePair("decoration_id",decorationInfo.getDecorationId()),
    	    		 new BasicNameValuePair("member_id",UserInfo.getInstance().getUserId()));
    	     
    	     try {
				JSONObject jo = new JSONObject(response);
				int status = jo.getInt("status");
                if(status == 1){
                	Message msg = readHandler.obtainMessage();
    				msg.what = 2;
    				readHandler.sendMessage(msg);
                }else if(status == 2){
                	Message msg = readHandler.obtainMessage();
    				msg.what = 4;
    				readHandler.sendMessage(msg);
                }else{
                	Message msg = readHandler.obtainMessage();
    				msg.what = 3;
    				readHandler.sendMessage(msg);
                }
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	     Log.d("lyl","response is " + response);
    		 
    	};
    };
    
  
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageView != null) {
            // Cancel any pending image work
            ImageWorker.cancelWork(mImageView);
            mImageView.setImageDrawable(null);
        }
    }
    
    //RegisterThread线程类
	Runnable getCommentThread = new Runnable()
    {

		@Override
		public void run() {
			getCommentById();
		}
    };
    
    private Handler  commentHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            
            switch(msg.what){
            
            case 0:
                //更新ui
            //	if(mTxtMessage != null)
            //	mTxtMessage.setVisibility(View.VISIBLE);
//            	if(mLoadingImg!=null){
//            		mLoadingImg.clearAnimation();
//                	mLoadingImg.setVisibility(View.GONE);
//                	mLoadingLinear.setVisibility(View.GONE);
//                	
//            	}
            	
            	break;
            case 1:
            //	mTxtMessage.setVisibility(View.GONE);
			//	if(mCommentListView.getFooterViewsCount()==0&&mCurPage!=1){
					
					
			//		mCommentListView.addFooterView(footerView);
			//	}
				break;
            case 2:
            //	footerView.setVisibility(View.GONE);
            	mCommentAdapter.setCommonData(mLastUserList);
            	if(mLoadingImg!=null){
            		mLoadingImg.clearAnimation();
                	mLoadingImg.setVisibility(View.GONE);
                	mLoadingLinear.setVisibility(View.GONE);
                	
            	}            	
            	mCommentAdapter.notifyDataSetChanged();
            	break;
            case 3:
            
            //	Toast.makeText(DecorationDetailFragment.this.getActivity(), "网络异常，请稍后刷新！", Toast.LENGTH_SHORT).show();
            	
            	if(mLoadingImg!=null){
            		mLoadingImg.clearAnimation();
                	mLoadingImg.setVisibility(View.GONE);
                	
            	}
            	mLoadingLinear.setVisibility(View.VISIBLE);
            	mNetworkError.setVisibility(View.VISIBLE);
            	
            	mCommentAdapter.notifyDataSetChanged();
            	break;
            	}
            
            
            }
        };
        
        private void getCommentById(){
        	if(isRefresh){
        		mCurPage = 1;
        	  
        	}
        	
      	  mCommentReplyId = "0";
      	  String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=tablesli&t=comment&table_name=" + mTableName +"&table_id="+mNewsId+"&totalCount="
      		  +COMMENTNUM_PERPAGE+"&page="+ mCurPage;
      		String response = CustomerHttpClient.post(urlStr);
      		if(response.equals("error")){
      			  Message msg = commentHandler.obtainMessage();
      				msg.what = 3;
      				commentHandler.sendMessage(msg);  
      		return;
      		}
      		
      		try {
      			JSONObject jsonObject = new JSONObject(response);
      			
      			String nowDate = jsonObject.getString("date");
//      			if(jsonObject.length()<=3){
//      				Message msg = commentHandler.obtainMessage();
//      				msg.what = 0;
//      				commentHandler.sendMessage(msg);
//      			    return;
//      			}else {
//      				Message msg = commentHandler.obtainMessage();
//      				msg.what = 1;
//      				commentHandler.sendMessage(msg);
//      				
//      			}
      			
      			JSONObject viewPageObject =jsonObject.getJSONObject("viewpage");
      			JSONObject infoPage = viewPageObject.getJSONObject("infoPage");
      			int totalCommonNum = infoPage.getInt("total");   //总评论数
      			
      			mTotalPage = infoPage.getInt("pagefrom");  //总页数
      			int curPage = infoPage.getInt("pagesize");  //当前页
      			String memberId;
      			
      			int commentNum=0;
      			//如果是第一页或最后一页，评论数位jsonObject总数减3
      		    if(curPage==1||curPage == mTotalPage){
      		    	commentNum = jsonObject.length()-3;
      		    }else{
      		    	commentNum = COMMENTNUM_PERPAGE;
      		    }
      		
      		    if(isRefresh){
      		  	    mLastUserList.clear();
      		  	    isRefresh = false;
      		    }
                  Map<String, Object> lastUserMap;
                  
      			for(int i=0;i<commentNum;i++){
      				JSONObject commentJSONObject = jsonObject.getJSONObject(String.valueOf(i));
      				CommentModel lastUserModel = new CommentModel();
      				
      				
      				lastUserModel.content = commentJSONObject.getString("content");
      				Log.d("lyl","reply content is " +lastUserModel.content);
      				if(lastUserModel.content.contains("&")){
	      				lastUserModel.content = lastUserModel.content.replace("&#039;", "'");
	      				lastUserModel.content = lastUserModel.content.replace("&quot;", "\"");
	      				lastUserModel.content = lastUserModel.content.replace("&lt;", "<");
	      				lastUserModel.content = lastUserModel.content.replace("&gt;", ">");
	      				lastUserModel.content = lastUserModel.content.replace("&amp;", "&");
      				}
      				
      				lastUserModel.commentId = commentJSONObject.getString("comment_id");
      				lastUserModel.memberId = commentJSONObject.getString("member_id");
      				lastUserModel.iconUrl = DecorationApplication.SERVER_NAME+"index.php?m=getPic&t=1&id="+ lastUserModel.memberId +"&w=" +
      				DecorationApplication.cpc.getCurWidth()/6;
      				
      				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      				Date updateDate = null;
      				Date now = null;
      				try {
      					updateDate = format.parse(commentJSONObject.getString("insDate"));
      					now = format.parse(nowDate);
      				} catch (ParseException e) {
      					// TODO Auto-generated catch block
      					e.printStackTrace();
      				}				
      				lastUserModel.publicDate = Constants.getTimeFormatText(now,updateDate);
      				
      				lastUserModel.userName = commentJSONObject.getString("nickname");
      				lastUserMap = new HashMap<String, Object>();
      				lastUserMap.put("lastUser", lastUserModel);
      				
      				Object rdata = commentJSONObject.get("rdata");
      				Log.d("lyl","rdata " + (rdata instanceof JSONArray));
      				Log.d("lyl","rdata " + (rdata instanceof String));
      			
      				if(rdata instanceof JSONArray){
      					JSONArray replyJSonArray = (JSONArray)rdata;
      				//	Log.d("lyl","reply length is " + replyJSonArray.length());
      					CommentModel[] replyUserModel = new CommentModel[replyJSonArray.length()];
      					
      					for(int j = 0;j<replyJSonArray.length();j++){
      						JSONObject replyJsonObject = replyJSonArray.getJSONObject(j);
      						CommentModel tempReplyModel = new CommentModel();
      						
      						tempReplyModel.content = replyJsonObject.getString("content");
      						if(tempReplyModel.content.contains("&")){
      							tempReplyModel.content = lastUserModel.content.replace("&#039;", "'");
      							tempReplyModel.content = lastUserModel.content.replace("&quot;", "\"");
      							tempReplyModel.content = lastUserModel.content.replace("&lt;", "<");
      							tempReplyModel.content = lastUserModel.content.replace("&gt;", ">");
      							tempReplyModel.content = lastUserModel.content.replace("&amp;", "&");
      	      				}
      						tempReplyModel.commentId = replyJsonObject.getString("comment_id");
      					//	memberId = replyJsonObject.getString("member_id");
      						tempReplyModel.memberId = replyJsonObject.getString("member_id");
      						
      						tempReplyModel.iconUrl = DecorationApplication.SERVER_NAME+"index.php?m=getPic&t=1&id="+ tempReplyModel.memberId +"&w=" +
      						DecorationApplication.cpc.getCurWidth()/6;
      					
      							
      							
      	
      						Date replayUpdateDate = null;
      						Date replyNow = null;
      						try {
      							replayUpdateDate = format.parse(replyJsonObject.getString("insDate"));
      							replyNow = format.parse(nowDate);
      						} catch (ParseException e) {
      							// TODO Auto-generated catch block
      							e.printStackTrace();
      						}
      						
      						tempReplyModel.publicDate = Constants.getTimeFormatText(replyNow,replayUpdateDate);
      						
      						tempReplyModel.userName = replyJsonObject.getString("nickname");
      						
      						replyUserModel[j] = tempReplyModel;
      					}
      					
      					lastUserMap.put("replyUser", replyUserModel);
      				}else{
      					lastUserMap.put("replyUser", null);
      				}

      				mLastUserList.add(lastUserMap);
      			}
      			mIsLoading = false;
      			JSONObject commentJSONObject = jsonObject.getJSONObject("0");
      			String content = commentJSONObject.getString("content");
      			String nickname = commentJSONObject.getString("nickname");
      			String insDate = commentJSONObject.getString("insDate");
      			int replyNum = commentJSONObject.getInt("replyNum");
      			String replyId = commentJSONObject.getString("reply_id");
      		//	String memberId = commentJSONObject.getString("member_id");
      		    
      		//	mCommentListView.removeFooterView(footerView);
      			
      			
      			Message msg = commentHandler.obtainMessage();
      			msg.what = 2;
      			commentHandler.sendMessage(msg);

      			if(replyId!=null){
      				
      			}	
      			
      		} catch (JSONException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
        }
    private void loadMoreItems(){
		if (mIsLoading || mCurPage >= mTotalPage) return;
		
		mIsLoading = true;
	//	footerView.setVisibility(View.VISIBLE);
		
	//	footerView.setClickable(false);
		
		mCurPage ++;
		
		new Thread(getCommentThread).start();
		//if(mCurPage==mTotalPage){
		//	footerView.setVisibility(View.GONE);
		//}
  }
    public  class CommentModel{
  	  public  String iconUrl;
  	  public  String userName;
  	  public  String publicDate;
  	  public  String content;
  	  public  String commentId;
  	  public String memberId;
    }
    public void setCommentReplyId(String replyId){
  	  mCommentReplyId = replyId;
    }
    public void setCommentVisible(){
    	   commentContainer.setVisibility(View.VISIBLE);
    	   bottomFrame.setVisibility(View.INVISIBLE);
      }
    public EditText getWriteComment(){
    	return mWriteComment;
    }
    public ImageFetcher getImageFetcher(){
		return mImageFetcher;
	}
    private void loadRefreshItems(){
   	 new Thread(getCommentThread).start();		
   }
}
