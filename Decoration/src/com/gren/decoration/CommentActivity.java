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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.adapter.CommentAdapter;
import com.gren.decoration.model.UserInfo;
import com.gren.util.Constants;
import com.gren.util.CustomerHttpClient;
import com.gren.util.PullToRefreshView;
import com.gren.util.PullToRefreshView.OnHeaderRefreshListener;



public class CommentActivity extends FragmentActivity implements OnHeaderRefreshListener{
	/**
	 * 每层楼显示数据的集合
	 */
	private List<Map<String, Object>> mLastUserList = null;
	
	/**
	 * 每条评论的引用评论集合
	 */
	List<CommentModel> mQuoteUserList;
	
	public final static String DECORATION_ID = "decoration_id"; 
	public final static String TABLE_NAME = "table_name"; 
	
	/**
	 * 每层楼的背景色，引数组的大小必须与str集合的大小相同
	 */
	private int[] color = new int[] { Color.CYAN, Color.RED, Color.YELLOW };
	
	private ImageView mImgReturn;
	PullToRefreshView mPullToRefreshView;
	
	 EditText mWriteComment;
	 ImageView mSendComent;
	 
	 private  int mCurPage;
	 private  int mTotalPage;
	 private boolean mIsLoading = false;
	 private View footerView;
     private CommentAdapter mCommentAdapter;
     ListView mCommentListView;
     
     private String mNewsId;
     private String mCommentReplyId;
     
     private TextView mTxtMessage;
     
     private boolean mIsHeaderRefreshing = false;
     
     private String mTableName; //调用的评论对应的表名，新闻表，图片表等
	 
	 private ImageFetcher mImageFetcher;
	 private final static int COMMENTNUM_PERPAGE = 5;//每页显示的评论数
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去应用的标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_comment_main);
		//键盘不自动获取edittext焦点
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		
		Intent intent = getIntent();
		mNewsId = intent.getStringExtra(DECORATION_ID);
		
		mTableName = intent.getStringExtra(TABLE_NAME);
		
		Log.d("lyl","CommentActivity news id is " + mNewsId);
		
		mCommentReplyId = "0";    //默认为0
		
		mTxtMessage = (TextView)findViewById(R.id.message);
		
		mCurPage = 1;
		mImageFetcher = new ImageFetcher(this, (int)(DecorationApplication.cpc.getCurWidth()/8f));

		footerView = LayoutInflater.from(this).inflate(R.layout.loading, null);

		
		  ImageCache.ImageCacheParams cacheParams =
              new ImageCache.ImageCacheParams(this,MainActivity.IMAGE_CACHE_DIR);
         cacheParams.setMemCacheSizePercent(0.25f); 
		mImageFetcher.addImageCache(this.getSupportFragmentManager(),
				cacheParams);
		
		 DecorationApplication d = (DecorationApplication)this.getApplication();
		 d.addImageFetcher(mImageFetcher);
		 mCommentListView = (ListView) this.findViewById(R.id.main_listView);
		
			//顶部背景
			FrameLayout topBg = (FrameLayout)findViewById(R.id.top_bg);
			LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(802),
					DecorationApplication.cpc.changeImageX(113));
			topBg.setLayoutParams(linearLp);
		 
		 mImgReturn = (ImageView)findViewById(R.id.img_return);
		
		LayoutParams frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(64),
				DecorationApplication.cpc.changeImageX(113));//54,45
		
		frameLp.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(20);
		mImgReturn.setPadding(DecorationApplication.cpc.changeImageX(5),
				DecorationApplication.cpc.changeImageX(34),
				DecorationApplication.cpc.changeImageX(5),
				DecorationApplication.cpc.changeImageX(34));
		mImgReturn.setLayoutParams(frameLp);
		
		
		ImageView commentBg = (ImageView)findViewById(R.id.comment_bg);
		frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(803),
				DecorationApplication.cpc.changeImageX(112));//54,45
		 
		commentBg.setLayoutParams(frameLp);
		
		ImageView commentLeftIcon = (ImageView)findViewById(R.id.comment_left_icon);
		frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(75),
				DecorationApplication.cpc.changeImageX(76));//54,45
		frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(45);
		commentLeftIcon.setLayoutParams(frameLp);
		
		 
		
		
		mWriteComment = (EditText)findViewById(R.id.write_newcomment);
		frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(509),
				DecorationApplication.cpc.changeImageX(78));//54,45
		frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(145);
		mWriteComment.setLayoutParams(frameLp);
		
		
		
		
		
		mSendComent = (ImageView)findViewById(R.id.send_comment);
		frameLp = new LayoutParams(DecorationApplication.cpc.changeImageX(73),
				DecorationApplication.cpc.changeImageX(73));//54,45
		frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
		frameLp.rightMargin = DecorationApplication.cpc.changeImageX(45);
		mSendComent.setLayoutParams(frameLp);
		
		
		mSendComent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=commentAdd";
				String memberId = UserInfo.getInstance().getUserId();
				if(memberId == null||memberId==""){
				//	memberId = "100091";
					Toast.makeText(CommentActivity.this, "只有登陆账号的用户才有评论权限，请先到设置界面登陆您的账号", Toast.LENGTH_SHORT).show();
					return;
				}
				if(mWriteComment.getText().toString().length()==0){
					Toast.makeText(CommentActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(CommentActivity.this, "发送出错，请稍后再试", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(CommentActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
				}
				mWriteComment.setText(null);
				
			
				InputMethodManager imm = (InputMethodManager)CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mWriteComment.getWindowToken(), 0); 
				
				mIsHeaderRefreshing = true;
				loadRefreshItems();
				 
			} 
		});
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.comment_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mImgReturn.setOnClickListener(CommenListener);
		
		mLastUserList = new ArrayList<Map<String, Object>>();
		/*
		 * 模拟显示的数据,最后一个用户评论的内容要与其他用户的评论内容分离开
		 */
		
	//	Thread getCommentThread = new Thread(new getCommentThread());
	//	getCommentThread.start();
		  new Thread(getCommentThread).start();
		
		//mCommentListView.addFooterView(footerView);
		
		footerView.setVisibility(View.GONE);
		
		
		mCommentAdapter = new CommentAdapter(this, mLastUserList);
		mCommentListView.setAdapter(mCommentAdapter);
		
		mCommentListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//if (view.getLastVisiblePosition() > 0 && view.getLastVisiblePosition() >= totalItemCount-1) {
		//	Log.d("lyl","view.getLastVisiblePosition() is " + view.getLastVisiblePosition());
		//	Log.d("lyl","totalItemCount-1 is " + (totalItemCount-1));
		//	Log.d("lyl","mCurPage < mTotalPage " + mCurPage +","+ mTotalPage);
//			if(!mCommentReplyId.equals("0")){
//				mCommentReplyId = "0";
//			}
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
            	if(mTxtMessage != null)
            	mTxtMessage.setVisibility(View.VISIBLE);
            	break;
            case 1:
            	mTxtMessage.setVisibility(View.GONE);
				if(mCommentListView.getFooterViewsCount()==0&&mCurPage!=1){
					
					
					mCommentListView.addFooterView(footerView);
					
				 //	mCommentAdapter.notifyDataSetChanged();
				//	mCommentListView.setAdapter(mCommentAdapter);
					
					//if(mCurPage==1)
				}
				break;
            case 2:
            	if(mIsHeaderRefreshing){
               	 Animation listviewAnim =new TranslateAnimation(0, 0,  mPullToRefreshView.getHeaderViewHeight(),0);
   					listviewAnim.setFillAfter(true);
   					listviewAnim.setDuration(300);
   					mCommentListView.startAnimation(listviewAnim);
   					mPullToRefreshView.getHeaderView().startAnimation(listviewAnim);
   				    mPullToRefreshView.onHeaderRefreshComplete();
   				    mIsHeaderRefreshing = false;
               	}
            	footerView.setVisibility(View.GONE);
            	mCommentAdapter.notifyDataSetChanged();
            	break;
            case 3:
            	 if(mIsHeaderRefreshing){
           			mIsHeaderRefreshing = false;          			
           			  Animation listviewAnim =new TranslateAnimation(0, 0,  mPullToRefreshView.getHeaderViewHeight(),0);
           				listviewAnim.setFillAfter(true);
           				listviewAnim.setDuration(300);
           				mCommentListView.startAnimation(listviewAnim);
           				mPullToRefreshView.getHeaderView().startAnimation(listviewAnim);
           			mPullToRefreshView.onHeaderRefreshComplete();
           			
           		}
            	Toast.makeText(CommentActivity.this, "网络异常，请稍后刷新！", Toast.LENGTH_SHORT).show();
            	mCommentAdapter.notifyDataSetChanged();
            	break;
            	}
            
            
            }
        };
	public ImageFetcher getImageFetcher(){
		return mImageFetcher;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		Intent intent = new Intent(CommentActivity.this,DecorationDetailActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    startActivity(intent);		
	}
	
   OnClickListener CommenListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.img_return:
				CommentActivity.this.finish();
				Intent intent = new Intent(CommentActivity.this,DecorationDetailActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    startActivity(intent);
			    //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out); 
				break;
			}
		}
   };

@Override
public void onHeaderRefresh(PullToRefreshView view) {
	// TODO Auto-generated method stub	      
			mIsHeaderRefreshing = true;
			loadRefreshItems();

}

public EditText getWriteComment(){
	return mWriteComment;
}
	

  private void getCommentById(){
	  mCommentReplyId = "0";

	  if(mIsHeaderRefreshing){
			
				mCurPage=1;
	   }

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
			if(jsonObject.length()<=3){
				Message msg = commentHandler.obtainMessage();
				msg.what = 0;
				commentHandler.sendMessage(msg);
			    return;
			}else {
				Message msg = commentHandler.obtainMessage();
				msg.what = 1;
				commentHandler.sendMessage(msg);
				
			}
			
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
		    if(mIsHeaderRefreshing){
				
		    	mLastUserList.clear();
				
	        }
		
            Map<String, Object> lastUserMap;
            
			for(int i=0;i<commentNum;i++){
				JSONObject commentJSONObject = jsonObject.getJSONObject(String.valueOf(i));
				CommentModel lastUserModel = new CommentModel();
				lastUserModel.content = commentJSONObject.getString("content");
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
					Log.d("lyl","reply length is " + replyJSonArray.length());
					CommentModel[] replyUserModel = new CommentModel[replyJSonArray.length()];
					
					for(int j = 0;j<replyJSonArray.length();j++){
						JSONObject replyJsonObject = replyJSonArray.getJSONObject(j);
						CommentModel tempReplyModel = new CommentModel();
						
						tempReplyModel.content = replyJsonObject.getString("content");
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
			
			mCommentAdapter.setCommonData(mLastUserList);
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
		footerView.setVisibility(View.VISIBLE);
		
		footerView.setClickable(false);
		
		mCurPage ++;
		
		new Thread(getCommentThread).start();
		if(mCurPage==mTotalPage){
			footerView.setVisibility(View.GONE);
		}
  }
  
  private void loadRefreshItems(){
	 new Thread(getCommentThread).start();		
}
  public void setCommentReplyId(String replyId){
	  mCommentReplyId = replyId;
  }
  public  class CommentModel{
	  public  String iconUrl;
	  public  String userName;
	  public  String publicDate;
	  public  String content;
	  public  String commentId;
	  public String memberId;
  }
}
