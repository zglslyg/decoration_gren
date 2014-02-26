package com.gren.decoration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailMoreActivity extends FragmentActivity{

	public final static String MERCHANT_NAME = "merchant_name";
	public final static String MERCHANT_ADDRESS = "merchant_address";
	public final static String MERCHANT_TEL="merchant_tel";
	
	String merchantTel;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.detail_more);
		Intent intent = getIntent();
		String merchantName = intent.getStringExtra(MERCHANT_NAME);
		Log.d("lyl","merchantName is " + merchantName);
		String merchantAddress = intent.getStringExtra(MERCHANT_ADDRESS);
		merchantTel = intent.getStringExtra(MERCHANT_TEL);
		initView();
		TextView nameTxt = (TextView)findViewById(R.id.detail_name);
		TextView telTxt = (TextView)findViewById(R.id.detail_tel);
		TextView addressTxt = (TextView)findViewById(R.id.detail_loc);
		
		if(merchantName==null||merchantName.equals("")){
			merchantName = "暂无信息";
		}
		if(merchantAddress.equals("")){
			merchantAddress = "暂无信息";
		}
		if(merchantTel.equals("")){
			merchantTel = "暂无信息";
		}
		nameTxt.setText(merchantName);
		telTxt.setText(merchantTel);
		addressTxt.setText(merchantAddress);
		
		telTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				connectMerchant();
			}
		});
	}
	
	private void initView(){
		initTopView();
	}

    private void initTopView(){
    	//顶部背景
		FrameLayout topBg = (FrameLayout)findViewById(R.id.top_bg);
		LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(DecorationApplication.cpc.changeImageX(800),
				DecorationApplication.cpc.changeImageX(113));
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
		
		 
		 topReturn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DetailMoreActivity.this.finish();
				//	DetailMoreActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				}
			});
    }
    private void connectMerchant(){
    	if(!merchantTel.equals("暂无信息")){

    				Intent DialIntent = new 
    				Intent(Intent.ACTION_DIAL,
        			Uri.parse("tel:" + merchantTel));
        			startActivity(DialIntent);
    	}
    }
//	
//	//RegisterThread线程类
//	Runnable getInfoThread = new Runnable()
//    {
//
//		@Override
//		public void run() {
//			String urlStr = DecorationApplication.SERVER_NAME+"index.php?m=tablesli&t=comment&table_name=" + mTableName +"&table_id="+mNewsId+"&totalCount="
//    		  +COMMENTNUM_PERPAGE+"&page="+ mCurPage;
//    		String response = CustomerHttpClient.post(urlStr);
//    		if(response.equals("error")){
//    			  Message msg = getInfoHandler.obtainMessage();
//    				msg.what = 3;
//    				getInfoHandler.sendMessage(msg);  
//    		return;
//    		}
//		}
//    };
//    private Handler  getInfoHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//            
//            switch(msg.what){
//            
//            case 0:
//            	break;
//            }
//        }
//    };
}
