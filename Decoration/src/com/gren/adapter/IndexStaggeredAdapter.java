package com.gren.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.decoration.AboutUsFragmentActivity;
import com.gren.decoration.AdDetailActivity;
import com.gren.decoration.DecorationApplication;
import com.gren.decoration.R;
import com.gren.decoration.SettingActivity;
import com.gren.decoration.model.DecorationInfo;
import com.gren.decoration.model.DuitangInfo;
import com.gren.util.CustomerHttpClient;
import com.origamilabs.library.views.StaggeredGridView;

public class IndexStaggeredAdapter extends BaseAdapter {
    private ArrayList<DecorationInfo> mInfos;

    ImageFetcher mImageFetcher;
    Context mContext;
    //viewpager相关变量
    ArrayList<ImageView> viewPagerImgList;
    String[] adImgUrl;
    String[] adId;
    private ViewPager adViewPager;
    private LinearLayout mPagePointer;
    private mPagerAdapter pageAdapter;
    private ArrayList<ImageView> mPagePoints;
    View mTotalView ;
    int adCount;

    public IndexStaggeredAdapter(Context context, ImageFetcher f) {
      //  mInfos = new LinkedList<DuitangInfo>();
        mImageFetcher = f;
        mContext = context;
        
	    viewPagerImgList = new ArrayList<ImageView>();
       
    }
    public IndexStaggeredAdapter(Context context, ImageFetcher f, ArrayList<DecorationInfo> infos) {
        //  mInfos = new LinkedList<DuitangInfo>();
          mImageFetcher = f;
          mInfos = infos;
          mContext = context;
          
      }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
     //   DuitangInfo duitangInfo = mInfos.get(position);
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        if(position == 0){
        	//TextView tv = new TextView(mContext);
        	//tv.setText("Test");
        	return getTopView();
        }
        DecorationInfo decorationInfo ;

        decorationInfo = DecorationApplication.decorationHotList.get(position-1);      

        if (convertView == null) {
        	
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.infos_list, null);
            holder = new ViewHolder();
            holder.decorationImg = (ImageView) convertView.findViewById(R.id.decoration_pic);
            holder.titleTxt = (TextView) convertView.findViewById(R.id.decoration_title);
            holder.priceNumTxt = (TextView) convertView.findViewById(R.id.decoration_price);
            holder.praiseNumTxt = (TextView) convertView.findViewById(R.id.decoration_praise);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        
//        float iHeight = ((float) 200 / 183 * duitangInfo.getHeight());
//        FrameLayout.LayoutParams lp =  new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
//        		(int) decorationInfo.getImgHeight());
        FrameLayout.LayoutParams lp =  new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
        		(int)( decorationInfo.getImgHeight()*(DecorationApplication.cpc.getCurWidth()/2)/decorationInfo.getImgWidth()));
 
        
        holder.decorationImg.setLayoutParams(lp);

     //  holder.contentView.setText(duitangInfo.getMsg());
        holder.titleTxt.setText(decorationInfo.getTitle());
        
        holder.priceNumTxt.setText("￥"+decorationInfo.getMyPrice());
        holder.praiseNumTxt.setText(String.valueOf(decorationInfo.getPraise()));
//        if(position==0){
//            holder.dateTxt.setText("test");
//            holder.dateTxt.setVisibility(View.VISIBLE);
//        }else{
//             holder.dateTxt.setVisibility(View.GONE);
//        }
    
        
       // mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
        mImageFetcher.setImageSize((int)(DecorationApplication.cpc.getCurWidth()/2));
        mImageFetcher.loadImage(decorationInfo.getImgUrl()+"&w="+DecorationApplication.cpc.getCurWidth()/2, holder.decorationImg);
    //    mImageFetcher.loadImage(decorationInfo.getImgUrl(), holder.decorationImg);
        return convertView;
    }

    class ViewHolder {
        ImageView decorationImg;
        TextView praiseNumTxt;
        TextView priceNumTxt;
        TextView titleTxt;
        
    }

    @Override
    public int getCount() {
      //  return mInfos.size();

    		 return DecorationApplication.decorationHotList.size()+1;

    }

    @Override
    public Object getItem(int arg0) {
    	if(arg0==0){
    		return null;
    	}
    	return DecorationApplication.decorationHotList.get(arg0-1);

    }

    @Override
    public long getItemId(int arg0) {
    	return 0;
    }
    
    
    @Override
    public int getViewTypeCount() {
		return 2;
	}
    @Override
    public int getItemViewType(int position) {
    	// TODO Auto-generated method stub
    	return position > 0 ? 1 : 0;
    }
    
    public void addItemLast(List<DuitangInfo> datas) {
       // mInfos.addAll(datas);
    	
        
    }
    public void addItem(DecorationInfo data) {
          mInfos.add(data);         
     }
    
    public void addItemTop(List<DuitangInfo> datas) {
        for (DuitangInfo info : datas) {
          //    mInfos.addFirst(info);
            
        }
    }
    
    private View getTopView(){
    	if(mTotalView == null){
    		 mTotalView = LayoutInflater.from(mContext).inflate(R.layout.index_toppager, null);
    	    	
    	    	StaggeredGridView.LayoutParams stalp = new StaggeredGridView.LayoutParams(
    	  			  StaggeredGridView.LayoutParams.WRAP_CONTENT
    	  			  );
    	    	stalp.span = 2;
    	        mTotalView.setLayoutParams(stalp);
    	        
    	    	
    			FrameLayout viewpagerFrame = (FrameLayout)mTotalView.findViewById(R.id.viewpager_frame);
    			
    			adViewPager =(ViewPager)mTotalView.findViewById(R.id.ad_viewPager);
    			 FrameLayout.LayoutParams  lp=new FrameLayout.LayoutParams((int)DecorationApplication.cpc.getCurWidth(),
    					 (int)(DecorationApplication.cpc.getCurWidth()*0.5));   //  /2.27  0.5
    			 adViewPager.setLayoutParams(lp);
    			 
    			  mPagePointer = (LinearLayout)mTotalView.findViewById(R.id.page_pointer);
    			
    			 OnPageChangeListener pageChangeListener =new OnPageChangeListener() {

    					// 页面选择

    					@Override
    					public void onPageSelected(int position) {
    						draw_Point(position);
    					}

    					@Override
    					public void onPageScrollStateChanged(int state) {

    					}

    					@Override
    					public void onPageScrolled(int position,

    					float positionOffset, int positionOffsetPixels) {

    					}

    				};
    				adViewPager.setOnPageChangeListener(pageChangeListener);
    				 new Thread(readAdRun).start();
    	    }
    	
			return mTotalView;
	}
    
	Runnable readAdRun = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			  String urlStr =DecorationApplication.SERVER_NAME + 
			  "index.php?m=tablesli&t=news&d=news_id,img&table_name=team&table_id=22&page=1&totalCount=5";
			  String response = CustomerHttpClient.post(urlStr);
			   if(response.contains("error")){
			    	 Message m = new Message();
		        	  //  Bundle responseBundle = new Bundle();
		        	   // responseBundle.putString("response", response);
		        	  //  m.setData(responseBundle);
		        	    m.what = 1;
		        	    adHandler.sendMessage(m);
			    }else{
			    	  try {
							JSONObject jo = new JSONObject(response);
							 adCount = 5;
							if(jo.length()-2<5){
								adCount = jo.length()-2;
							}
							adImgUrl=new String[5];
							adId = new String[5];
							for (int i = 0; i < adCount; i++) {
								JSONObject decorationJson = jo.getJSONObject(String.valueOf(i));
								adId[i] = decorationJson.getString("news_id");
								adImgUrl[i]  =	DecorationApplication.SERVER_NAME
								+ "img.php?src=uploadfile/news/"
								+ decorationJson.getString("news_id") + "/" + decorationJson.getString("img")
								+ "&w=" + DecorationApplication.cpc.getCurWidth();
							}
							 Message m = new Message();
				        	  //  Bundle responseBundle = new Bundle();
				        	   // responseBundle.putString("response", response);
				        	  //  m.setData(responseBundle);
				        	    m.what = 0;
				        	    adHandler.sendMessage(m);
			    	  }catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		              }
		}
	     
	};
	
	/**这里重写handleMessage方法，接受到子线程数据后更新UI**/
    private Handler adHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            
            switch(msg.what){
            
            case 0:
                  for(int i = 0;i<adCount;i++){
                	  ImageView iv = new ImageView(mContext);
                	  iv.setScaleType(ScaleType.CENTER_CROP);
                	  iv.setTag(R.id.tag_adid,adId[i]);
                	  iv.setTag(R.id.tag_imgurl,adImgUrl[i]);
                	  iv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(mContext, AdDetailActivity.class);
							intent.putExtra("ad_id",(String)v.getTag(R.id.tag_adid));
							intent.putExtra("img_url",(String)v.getTag(R.id.tag_imgurl));
							mContext.startActivity(intent);
//							overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
						}
					});
                	  mImageFetcher.setImageSize((int)DecorationApplication.cpc.getCurWidth(),
                			  (int)(DecorationApplication.cpc.getCurWidth()/2.27));
                	  mImageFetcher.loadImage(adImgUrl[i], iv);
                	  viewPagerImgList.add(iv);
                  }
                  
                  Init_Point();
                  pageAdapter = new mPagerAdapter();
                  adViewPager.setAdapter(pageAdapter);
                  
                
            	break;
            case 1:
                //关闭
            	break;
            }
        }
    };
    
    public class mPagerAdapter extends PagerAdapter {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;

		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
	//		return super.getItemPosition(object);
			  return POSITION_NONE;   
		}
		@Override
		public int getCount() {

			return viewPagerImgList.size();

		}


        @Override 
        public Parcelable saveState() { 
            return null; 
        } 
 
        @Override 
        public void restoreState(Parcelable arg0, ClassLoader arg1) { 
        } 
 
        @Override 
        public void startUpdate(View arg0) { 
        } 
 
        @Override 
        public void finishUpdate(View arg0) { 
        } 
        
		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container).removeView(viewPagerImgList.get(position));

		}

		@Override
		public Object instantiateItem(View container, int position) {
		
			((ViewPager) container).addView(viewPagerImgList.get(position));

			return viewPagerImgList.get(position);

		}

	};
	
	  //初始化页面指示点
	void Init_Point() {
		mPagePoints = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < viewPagerImgList.size(); i++) {
			imageView = new ImageView(mContext);
			imageView.setBackgroundResource(R.drawable.page_unselected);
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(DecorationApplication.cpc.changeImageX(19),
							DecorationApplication.cpc.changeImageX(19)));
//			if(i==0){
				layoutParams.leftMargin = DecorationApplication.cpc.changeImageX(5);
				layoutParams.rightMargin = DecorationApplication.cpc.changeImageX(5);
//			}
//			else{
//				layoutParams.leftMargin = 2;
//				layoutParams.rightMargin = 1;
//			}
			mPagePointer.addView(imageView, layoutParams);
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.page_selected);// 默认第二个要列为选中状态
			}
			mPagePoints.add(imageView);
		}
	}
	  //绘制当前选中的指示点
	public void draw_Point(int index) {
		for (int i = 0; i < mPagePoints.size(); i++) {
			if (index == i) {
				mPagePoints.get(i).setBackgroundResource(
						R.drawable.page_selected);
			} else
				mPagePoints.get(i).setBackgroundResource(
						R.drawable.page_unselected);
		}
	}
}
