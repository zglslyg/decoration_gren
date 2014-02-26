package com.gren.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gren.decoration.DecorationDetailFragment.CommentModel;
import com.gren.decoration.DecorationApplication;
import com.gren.decoration.DecorationDetailActivity;
import com.gren.decoration.DecorationDetailFragment;
import com.gren.decoration.R;
import com.gren.decoration.model.DecorationInfo;



public class CommentAdapter extends BaseAdapter {

	private Context context;
	/**
	 * 显示数据的集合
	 */
	private List<Map<String, Object>> mCommonData;

	/**
	 * 每层楼之间的间距
	 */
	private int  padding =(int)( 2 * (DecorationApplication.cpc.getCurDensity()/160f));
	/**
	 * 总的楼层数
	 */
	private int totalCount = 0;
	
	 PopupWindow mPopupWindow;
	 
	 TextView mUsername;
	 
	 private DecorationDetailActivity container;
	 
	 private boolean isPopupMenuShow=false;
	 
	 private String mReplyId;
	 
	 private DecorationDetailFragment dfragment;
	
	 private View mTotalView;
	 private ImageView mImageView;
	 DecorationInfo mDecorationInfo;
	 TextView commentNumTxt;

	public CommentAdapter(Context context, List<Map<String, Object>> strings
			) {
		this.context = context;
		this.mCommonData = strings;
		container = (DecorationDetailActivity)context;
		mReplyId = "0";
	}
	
	public CommentAdapter(Fragment fragment, List<Map<String, Object>> strings
	,DecorationInfo decorationInfo) {
		
		this.mCommonData = strings;
		dfragment = (DecorationDetailFragment)fragment;
		mDecorationInfo = decorationInfo;
		this.context = dfragment.getActivity();
		
		mReplyId = "0";
    }

	public void setCommonData(List<Map<String, Object>> commonData ){
		mCommonData = commonData;
	}
	@Override
	public int getCount() {
		return mCommonData != null ? mCommonData.size()+1 : 0;
		//return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		 return position==0?0:1;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public Object getItem(int position) {
		if(position==0){
			return null;
		}
		return mCommonData.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		if(position==0){
			return -1;
		}
		return position-1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("lyl","position is " + position);
		if(position==0){
			return getTopView();
		}
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.news_comment_container, null);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.main_linearLayout);
			holder.lastCommonText = (TextView) convertView
					.findViewById(R.id.last_common);
			
			holder.lastCommonDate = (TextView) convertView
			.findViewById(R.id.comment_date);
			
			holder.lastUserAvatar = (ImageView) convertView
			.findViewById(R.id.usericon);
			
			holder.lastUserName = (TextView) convertView
			.findViewById(R.id.username);
			holder.commentId = (TextView) convertView
			.findViewById(R.id.comment_id);
			
			holder.memberId = (TextView) convertView
			.findViewById(R.id.member_id);
			
		   
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// 显示最后一个用户的评论内容
//		holder.lastCommonText.setText(mCommonData.get(position).get("lastComment")
//				.toString());
		CommentModel userModel = (CommentModel)mCommonData.get(position-1).get("lastUser");
		
		if(userModel!=null){
			holder.lastCommonText.setText(userModel.content);
			holder.lastUserName.setText(userModel.userName);
			holder.lastCommonDate.setText(userModel.publicDate);
			
			holder.commentId.setText(userModel.commentId);
			holder.memberId.setText(userModel.memberId);
			
			//container.getImageFetcher().loadImage(userModel.iconUrl, holder.lastUserAvatar);
			dfragment.getImageFetcher().loadImage(userModel.iconUrl, holder.lastUserAvatar);
			holder.lastUserAvatar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView memberid = (TextView)(((View)v.getParent().getParent()).findViewById(R.id.member_id));
					String member_id = memberid.getText().toString();
					Log.d("lyl","click icon memberid is " + member_id);
				}
			});
			
		
		}
		
		//mReplyId = "0";
		
		
		//convertView.setTag(userModel.commentId);
	  

		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				if(v.getTag()!=null&&v.getTag().equals("click")){
				if(isPopupMenuShow){
					hidePopupWindow();
					isPopupMenuShow = false;
				
				//	v.setTag("unclick");
				}else{
					showPopupWindow(v);
					isPopupMenuShow = true;
				//	v.setTag("click");
				}
				
				
			}
		});
		
		// 获得用户名的字符串数组和用户评论内容的字符串数组
	//	String[] userNames = (String[]) mCommonData.get(position).get("name");
	//	String[] userComment = (String[]) mCommonData.get(position).get("comment");
		
		CommentModel[] replyUserModel =(CommentModel[])mCommonData.get(position-1).get("replyUser");
		// 开始盖楼
		
		
//		if(userNames!=null){
//			holder.layout.setVisibility(View.VISIBLE);
//			totalCount = userNames.length;
//			   Log.d("lyl","child count is " + holder.layout.getChildCount());	
//			    Log.d("lyl","child position is " + position);	
//			    padding = 2 * (int)(Main.mDensity/160f);
//			 //   if(holder.layout.getChildCount()==0){
//			    	holder.layout.removeAllViews();
//			    	holder.layout.addView(add(context, (userNames.length - 1), padding, userNames, userComment,
//							color));
//		}
		if(replyUserModel!=null){
			holder.layout.setVisibility(View.VISIBLE);
			totalCount = replyUserModel.length;
			   Log.d("lyl","child count is " + holder.layout.getChildCount());	
			    Log.d("lyl","child position is " + position);	
			    padding =(int)( 2 * (DecorationApplication.cpc.getCurDensity()/160f));
			    Log.d("lyl","padding is "+ padding);
			 //   if(holder.layout.getChildCount()==0){
			    	holder.layout.removeAllViews();
			    	holder.layout.addView(add(context, (replyUserModel.length - 1), padding, replyUserModel,userModel.commentId));
		}
		
		else{
		 	holder.layout.removeAllViews();
			holder.layout.setVisibility(View.GONE);
		}
		
	 
	  //  }
		return convertView;
	}

	/**
	 * 递归加载楼层的方法
	 * 
	 * @param context上下文的对像
	 * @param 递归的控制参数
	 *            ，同时也是取用户评论信息和背景色的下标，引参数的大小必须是从集合中获得的用户名数组或从集合中获得的评论内容数据的大小减一
	 * @param pad
	 *            楼层的间距
	 * @param strs
	 *            用户名的字符串数组
	 * @param strs1
	 *            用户相应评论内容的字符串数组
	 * @param color
	 *            背景色的数组
	 * @return 返回一个楼层的LinearLayout布局对象
	 */
//	private LinearLayout add(Context context, int i, int pad, String[] strs,
//			String[] strs1, int[] color) {
//		
//		//如果层数大于5，就不再设置层之间的距离
//		  Log.d("lyl"," totalCount-i is " + (totalCount-i));	
//		 if((totalCount-i)>5){
//			 pad = 0;
//		 }		
//		 
//		// 加载一个布局
//		LinearLayout layout1 = (LinearLayout) LayoutInflater.from(context)
//				.inflate(R.layout.news_comment_add, null);
//		
//		layout1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
////				if(v.getTag()!=null&&v.getTag().equals("click")){
//				if(isPopupMenuShow){
//					hidePopupWindow();
//					isPopupMenuShow = false;
//				//	v.setTag("unclick");
//				}else{
//					showPopupWindow(v);
//					isPopupMenuShow = true;
//				//	v.setTag("click");
//				}
//				
//				
//			}
//		});
//		
//		
//		// 获得显示用户名、楼层数、用户评论内容的TextView
//		 
//		ImageView icon = (ImageView)layout1.findViewById(R.id.usericon);
//		TextView name = (TextView) layout1.findViewById(R.id.username);
//		TextView page = (TextView) layout1.findViewById(R.id.floor_num);
//		TextView comment = (TextView) layout1.findViewById(R.id.user_comment);
//		
//		//container.getImageFetcher().loadImage(icon, holder.lastUserAvatar, true);
//		
//		// 设置显示用户名、楼层数、用户评论内容TextView的内容
//		name.setText(strs[i]);
//		page.setText((i + 1) + "");
//		comment.setText(strs1[i]);
//		// 动态生成一个LinearLayout来装载获得的布局
//		LinearLayout layout = new LinearLayout(context);
//		layout.setOrientation(LinearLayout.VERTICAL);
//		//layout.setBackgroundColor(color[i%3]);
//		layout.setBackgroundResource(R.drawable.news_common_bg);
//		
//		layout.setPadding(pad, pad, pad, pad);
//		
//		
//		// 当i的值为零时，递归结束
//		if (i != 0) {			 
//			layout.addView(add(context, --i, pad, strs, strs1, color));
//		}
//		layout.addView(layout1);
//	
//		return layout;
//	}
	
	/**
	 * 递归加载楼层的方法
	 * 
	 * @param context上下文的对像
	 * @param 递归的控制参数
	 *            ，同时也是取用户评论信息和背景色的下标，引参数的大小必须是从集合中获得的用户名数组或从集合中获得的评论内容数据的大小减一
	 * @param pad
	 *            楼层的间距
	 * @param strs
	 *            用户名的字符串数组
	 * @param strs1
	 *            用户相应评论内容的字符串数组
	 * @param color
	 *            背景色的数组
	 * @return 返回一个楼层的LinearLayout布局对象
	 */
	private LinearLayout add(Context context, int i, int pad, CommentModel[] commentModel,
			String commentid) {
		LinearLayout layout;   //需要返回的layout
		// 动态生成一个LinearLayout来装载获得的布局
		 layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		//layout.setBackgroundColor(color[i%3]);
		//layout.setBackgroundResource(R.drawable.news_common_bg);
		
		
		
		//如果层数大于5，就不再设置层之间的距离
		  Log.d("lyl"," totalCount-i is " + (totalCount-i));	
		 if((totalCount-i)>5){
			 pad = 0;
		 }		
		 layout.setPadding(pad, pad, pad, pad);
		 
		 if((totalCount-i)>5){
				for(int curIndex = 0;curIndex<=i;curIndex++){
					// 加载一个布局
					RelativeLayout layout1 = (RelativeLayout) LayoutInflater.from(context)
							.inflate(R.layout.news_comment_add, null);
			//		layout1.setBackgroundResource(R.drawable.news_common_bg);
					
					layout1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							if(isPopupMenuShow){
								hidePopupWindow();
								isPopupMenuShow = false;
							}else{
								showPopupWindow(v);
								isPopupMenuShow = true;
							}
						}
					});
					
					
					// 获得显示用户名、楼层数、用户评论内容的TextView
					 
					ImageView icon = (ImageView)layout1.findViewById(R.id.usericon);
					TextView name = (TextView) layout1.findViewById(R.id.username);
					TextView page = (TextView) layout1.findViewById(R.id.floor_num);
					TextView comment = (TextView) layout1.findViewById(R.id.user_comment);
					TextView date = (TextView) layout1.findViewById(R.id.comment_date);
					TextView comment_id = (TextView) layout1.findViewById(R.id.comment_id);
					TextView member_id = (TextView) layout1.findViewById(R.id.add_member_id);
					
					comment_id.setText(commentid);
					//container.getImageFetcher().loadImage(icon, holder.lastUserAvatar, true);
					
					// 设置显示用户名、楼层数、用户评论内容TextView的内容
					
					//container.getImageFetcher().loadImage(commentModel[curIndex].iconUrl, icon);
					dfragment.getImageFetcher().loadImage(commentModel[curIndex].iconUrl, icon);
					
					name.setText(commentModel[curIndex].userName);
					date.setText(commentModel[curIndex].publicDate);
					page.setText((curIndex + 1) + "");
					comment.setText(commentModel[curIndex].content);
					member_id.setText(commentModel[curIndex].memberId);
					icon.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							TextView memberid = (TextView)(((View)v.getParent()).findViewById(R.id.add_member_id));
							String member_id = memberid.getText().toString();
							Log.d("lyl","click icon memberid is " + member_id);
						}
					});
				
					
					layout.addView(layout1);
				}
				return layout;
				
		}
		 
		// 加载一个布局
		RelativeLayout layout1 = (RelativeLayout) LayoutInflater.from(context)
				.inflate(R.layout.news_comment_add, null);
		
		layout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isPopupMenuShow){
					hidePopupWindow();
					isPopupMenuShow = false;
				}else{
					showPopupWindow(v);
					isPopupMenuShow = true;
				}
			}
		});
		
		
		
		// 获得显示用户名、楼层数、用户评论内容的TextView
		 
		ImageView icon = (ImageView)layout1.findViewById(R.id.usericon);
		TextView name = (TextView) layout1.findViewById(R.id.username);
		TextView page = (TextView) layout1.findViewById(R.id.floor_num);
		TextView comment = (TextView) layout1.findViewById(R.id.user_comment);
		TextView date = (TextView) layout1.findViewById(R.id.comment_date);
		TextView comment_id = (TextView) layout1.findViewById(R.id.comment_id);
		TextView member_id = (TextView) layout1.findViewById(R.id.add_member_id);
		
		comment_id.setText(commentid);
		//container.getImageFetcher().loadImage(icon, holder.lastUserAvatar, true);
		
		// 设置显示用户名、楼层数、用户评论内容TextView的内容
		
		//container.getImageFetcher().loadImage(commentModel[i].iconUrl, icon);
		dfragment.getImageFetcher().loadImage(commentModel[i].iconUrl, icon);
		
		name.setText(commentModel[i].userName);
		date.setText(commentModel[i].publicDate);
		page.setText((i + 1) + "");
		comment.setText(commentModel[i].content);
		member_id.setText(commentModel[i].memberId);
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView memberid = (TextView)(((View)v.getParent()).findViewById(R.id.add_member_id));
				String member_id = memberid.getText().toString();
				Log.d("lyl","click icon memberid is " + member_id);
			}
		});
		
		
			// 当i的值为零时，递归结束
			if (i != 0) {			 
				layout.addView(add(context, --i, pad,commentModel,commentid ));
			}
		layout.addView(layout1);
		
		
	
		return layout;
	}

	
	
	private class ViewHolder {
		/**
		 * 加载楼层的LinearLayout布局
		 */
		public LinearLayout layout;
		/**
		 * 显示最后一个用户评论的TextView
		 */
		public TextView lastCommonText;
		/**
		 * 显示最后一个用户姓名的TextView
		 */
		public TextView lastUserName;
		/**
		 * 显示最后一个用户头像的ImageView
		 */
		public ImageView lastUserAvatar;
		/**
		 * 显示最后一个用户评论时间的TextView
		 */
		public TextView lastCommonDate;
		
		public TextView commentId;
		
		public TextView memberId;
		
		
	}

	
	 public void showPopupWindow(View parent) {
	      
	        LayoutInflater mLayoutInflater = (LayoutInflater) context
	                .getSystemService("layout_inflater");
	        
	        TextView replayid = (TextView)parent.findViewById(R.id.comment_id);
	        
	        mReplyId = replayid.getText().toString();
	        
	        mUsername = (TextView)parent.findViewById(R.id.username);
	        
	        
	        if(mPopupWindow==null){
	        	  View commentPopupmenu = mLayoutInflater.inflate(
	  	                R.layout.comment_popupmenu, null);
	        	  mPopupWindow = new PopupWindow(commentPopupmenu,
	  	                LayoutParams.MATCH_PARENT
	  	                , LayoutParams.WRAP_CONTENT);
//	        	  TextView praise = (TextView)commentPopupmenu.findViewById(R.id.praise);
//	  	        praise.setOnClickListener(new OnClickListener() {
//	  				
//	  				@Override
//	  				public void onClick(View v) {
//	  					// TODO Auto-generated method stub
//	  					v.post(new Runnable() {
//	  						
//	  						@Override
//	  						public void run() {
//	  							// TODO Auto-generated method stub
//	  							Toast.makeText(context, "您单击了顶顶顶"+mUsername.getText().toString(), Toast.LENGTH_SHORT).show();
//	  						}
//	  					});
//	  					mPopupWindow.dismiss();
//	  					isPopupMenuShow = false;
//	  				}
//	  			});

		  	      TextView reply = (TextView)commentPopupmenu.findViewById(R.id.reply);
		  	      
		  	    reply.setOnClickListener(new OnClickListener() {
	  				
	  				@Override
	  				public void onClick(View v) {
	  					// TODO Auto-generated method stub
	  					
	  					InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
	  					//container.getWriteComment().requestFocus();
	  					 
	  					dfragment.getWriteComment().requestFocus();
	  					
	  					dfragment.setCommentReplyId(mReplyId);
	  					dfragment.setCommentVisible();
	  					
	  					imm.showSoftInput(dfragment.getWriteComment(), InputMethodManager.RESULT_SHOWN);
	  					mPopupWindow.dismiss();
	  					isPopupMenuShow = false;
	  				}
	  			});
		  	    
//	  	      TextView down = (TextView)commentPopupmenu.findViewById(R.id.shit);
//	  	      
//	  	    down.setOnClickListener(new OnClickListener() {
//  				
//  				@Override
//  				public void onClick(View v) {
//  					// TODO Auto-generated method stub
//  					v.post(new Runnable() {
//  						
//  						@Override
//  						public void run() {
//  							// TODO Auto-generated method stub
//  							Toast.makeText(context, "您单击了踩踩踩"+mUsername.getText().toString(), Toast.LENGTH_SHORT).show();
//  						}
//  						
//  					});
//  					mPopupWindow.dismiss();
//  					isPopupMenuShow = false;
//  				}
//  			});
	  	      
	        }
	        
	       
	      //点击popupwindow以外区域，让popupwindow隐藏
	       // mPopupWindow.setBackgroundDrawable(null);
	        Drawable dr = context.getResources().getDrawable(R.drawable.account_edit_bg);
	        mPopupWindow.setBackgroundDrawable(dr);
	        mPopupWindow.setOutsideTouchable(true);
	        mPopupWindow.showAsDropDown(parent);
	   //     mPopupWindow.showAtLocation(parent, Gravity.LEFT, parent.getLeft(), parent.getTop());
	    }
	 public void hidePopupWindow() {
		if(mPopupWindow!=null){
			 mPopupWindow.dismiss();
		}
	    }
	 
	// 获得圆角图片的方法
		public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
			if(bitmap == null){
				return null;
			}
			
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			 
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}
		
		 private View getTopView(){
		    	if(mTotalView == null){
		    		 mTotalView = LayoutInflater.from(context).inflate(R.layout.detail_topview, null);
		           TextView title = (TextView)mTotalView.findViewById(R.id.title);
		           TextView intro = (TextView)mTotalView.findViewById(R.id.introduce);
		           TextView publicPrice = (TextView)mTotalView.findViewById(R.id.public_price);
		           TextView myPrice = (TextView)mTotalView.findViewById(R.id.my_price);
		           commentNumTxt = (TextView)mTotalView.findViewById(R.id.comment_num);
		           FrameLayout.LayoutParams lp =null;
		      	 
		         if(mDecorationInfo.getImgWidth()>DecorationApplication.cpc.getCurWidth()){
		         	int height =(int)(mDecorationInfo.getImgHeight()*DecorationApplication.cpc.getCurWidth()/mDecorationInfo.getImgWidth());
		         	 lp = new FrameLayout.LayoutParams((int)DecorationApplication.cpc.getCurWidth(),
		         			 height);
		         }else{
		         	 lp = new FrameLayout.LayoutParams((int)DecorationApplication.cpc.getCurWidth(),
		         			 (int)(DecorationApplication.cpc.getCurWidth()*0.75));
		         }
		         
		    ;
		         
		         mImageView = (ImageView) mTotalView.findViewById(R.id.imageView);
		         lp.gravity = Gravity.TOP;
		         mImageView.setLayoutParams(lp);
		         title.setText(mDecorationInfo.getTitle());
		        intro.setText(mDecorationInfo.getIntroduce());
		        publicPrice.setText("￥"+mDecorationInfo.getPublicPrice());
		        myPrice.setText("￥"+mDecorationInfo.getMyPrice());
		     
	            dfragment.getImageFetcher().loadImage(mDecorationInfo.getImgUrl()+"&w=" + DecorationApplication.cpc.getCurWidth(), mImageView);

		    	}
		    
		    	
					return mTotalView;
			}
		 public void setCommentText(String commentNum){
					commentNumTxt.setText(commentNum);
		 }
}
