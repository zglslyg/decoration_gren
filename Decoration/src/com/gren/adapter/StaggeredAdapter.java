package com.gren.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.decoration.DecorationApplication;
import com.gren.decoration.R;
import com.gren.decoration.model.DecorationInfo;
import com.gren.decoration.model.DuitangInfo;

public class StaggeredAdapter extends BaseAdapter {
    private ArrayList<DecorationInfo> mInfos;

    ImageFetcher mImageFetcher;
    int mCategory;
    Context mContext;

    public StaggeredAdapter(Context context, ImageFetcher f,int category) {
      //  mInfos = new LinkedList<DuitangInfo>();
        mImageFetcher = f;
        mCategory = category;
        mContext = context;
    }
    public StaggeredAdapter(Context context, ImageFetcher f, ArrayList<DecorationInfo> infos) {
        //  mInfos = new LinkedList<DuitangInfo>();
          mImageFetcher = f;
          mInfos = infos;
          mContext = context;
          mCategory = -1;
          
      }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
     //   DuitangInfo duitangInfo = mInfos.get(position);
    //    Log.d("lyl","getview position is " + position);
//        if(position == 0){
//        	TextView tv = new TextView(mContext);
//        	tv.setText("Test");
//        	return tv;
//        }
        DecorationInfo decorationInfo ;
        if(mCategory == 0){
        	 decorationInfo = DecorationApplication.decorationPraiseList.get(position);
        }else if(mCategory == 1){
        	 decorationInfo = DecorationApplication.decorationPriceList.get(position);
        }else if (mCategory == 2){
        	 decorationInfo = DecorationApplication.decorationTimeList.get(position);
        }else if (mCategory == 3){
        	 decorationInfo = DecorationApplication.decorationCollectList.get(position);
        }else{
        	decorationInfo = mInfos.get(position);
        }
       

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
     //   mImageFetcher.loadImage(decorationInfo.getImgUrl(), holder.decorationImg);
        mImageFetcher.loadImage(decorationInfo.getImgUrl()+"&w="+DecorationApplication.cpc.getCurWidth()/2, holder.decorationImg);
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
    	 if(mCategory == 0){
    	    return DecorationApplication.decorationPraiseList.size();
    	 }
    	 else if(mCategory == 1){
    		 return DecorationApplication.decorationPriceList.size();
    	 }else if (mCategory == 2){
    		 return DecorationApplication.decorationTimeList.size();
    	 }else if (mCategory == 3){
    		 return DecorationApplication.decorationCollectList.size();
    	 } else{
    		 return  mInfos.size();
    	 }
    }

    @Override
    public Object getItem(int arg0) {

//    	if(arg0==0){
//    		return null;
//    	}
    	 if(mCategory == 0){
             return DecorationApplication.decorationPraiseList.get(arg0);
    	 }else if(mCategory == 1){
    		 return DecorationApplication.decorationPriceList.get(arg0);
    	 }else if (mCategory == 2){
    		 return DecorationApplication.decorationTimeList.get(arg0);
    	 }else if (mCategory == 3){
    		 return DecorationApplication.decorationCollectList.get(arg0);
    	 }
    	 else{
    		 return  mInfos.get(arg0);
    	 }
    }

    @Override
    public long getItemId(int arg0) {
    	return 0;
    }
    
//    @Override
//    public int getViewTypeCount() {
//		return 2;
//	}
//    @Override
//    public int getItemViewType(int position) {
//    	// TODO Auto-generated method stub
//    	return position > 0 ? 1 : 0;
//    }
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
}
