package com.gren.decoration;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.gren.adapter.StaggeredAdapter;
import com.gren.decoration.model.DecorationInfo;
import com.gren.decoration.model.UserInfo;
import com.gren.util.Constants;
import com.gren.util.CustomerHttpClient;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnScrollListener;

public class PersonalActivity extends FragmentActivity{

    StaggeredGridView gridView;
    private int totalPageNum;
    private int curPage=1;
    private ImageView mNetworkError;
    private ImageView mLoadingImg;  
    private LinearLayout mLoadingLinear;
    private TextView mBottomLoading;
    private int totalDecorationNum;
    
    boolean isLoading = false;
    
    Bitmap bitmap; //头像的bitmap
    ImageView faceImage;
    TextView username;
    
    private static final String IMAGE_FILE_NAME = "face_image.jpg";
    
    /* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	
	private static final int PIC_ROUND_RADIUS = 8;
	
	private String[] mAvatarItems = new String[] { "选择本地图片", "拍照" };
    
   // private ArrayList<DecorationInfo> decorationCollectList = new ArrayList<DecorationInfo>();
	    
    StaggeredAdapter mAdapter;
    TextView mLikeNum;
    
	private ImageFetcher imageFetcher;
	 public static final String IMAGE_CACHE_DIR = "images";
	 
	 private boolean isDirectIn;
	    
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	    setContentView(R.layout.personal_activity);
	    settingUI();
	    mLoadingImg = (ImageView)findViewById(R.id.loading);
		mLoadingImg.setScaleType(ScaleType.CENTER_INSIDE);
		
		isDirectIn = getIntent().getBooleanExtra("is_directin", false);
		
		   ImageCache.ImageCacheParams cacheParams =
               new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
       cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

       // The ImageFetcher takes care of loading images into our ImageView children asynchronously
		imageFetcher = new ImageFetcher(this, (int)(DecorationApplication.cpc.getCurWidth()/2));
		 
		imageFetcher.addImageCache(PersonalActivity.this.getSupportFragmentManager(), cacheParams);
		imageFetcher.setImageFadeIn(true);
         Log.d("lyl","test");
		imageFetcher.setLoadingImage(R.drawable.de_avatar);
		
		RotateAnimation loadingAnim = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		  LinearInterpolator lir = new LinearInterpolator();
		loadingAnim.setInterpolator(lir);
		loadingAnim.setDuration(1000);
		loadingAnim.setRepeatCount(-1); 
		mLoadingImg.startAnimation(loadingAnim);
		
		mLoadingLinear = (LinearLayout)findViewById(R.id.loading_linear);
		
		mNetworkError = (ImageView)findViewById(R.id.network_error);
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
		
		 mBottomLoading = (TextView)findViewById(R.id.bottom_loading);
		 
			gridView = (StaggeredGridView) findViewById(R.id.staggeredGridView1);
			
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
					
					if(!isLoading&&totalItemCount!=0&&firstVisibleItem+visibleItemCount == totalItemCount&&curPage<totalPageNum){
						Log.d("lyl","到底了!");
						 isLoading = true;
						 mBottomLoading.setVisibility(View.VISIBLE);
						curPage++;
						new Thread(readInfoRun).start();					  
					}
					
				}
			});
	    DecorationApplication d = (DecorationApplication)this.getApplication();
	  //  mAdapter = new StaggeredAdapter(this,d.getImageFetcher().get(0),3);
	    
	    mAdapter = new StaggeredAdapter(this,imageFetcher,3);
	    gridView.setAdapter(mAdapter);
	    gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
			
			@Override
			public void onItemClick(StaggeredGridView parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub	
				Log.d("lyl","onItemClick -->position is " + position);
				Intent intent = new Intent(PersonalActivity.this, DecorationDetailActivity.class);
				intent.putExtra("position", position);
				intent.putExtra(Constants.CATEGORY, 3);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
		});
	    new Thread(readInfoRun).start();
	    
	    new Thread(readUserInfoRun).start();
	    
	    new Thread(readImgRun).start();
	    
	       
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//DecorationApplication.decorationCollectList.clear();
		curPage=1;
		new Thread(readInfoRun).start();
		new Thread(readUserInfoRun).start();
	}
	 private void  settingUI(){
		 
		 //标题背景
		 FrameLayout topBg = (FrameLayout)findViewById(R.id.top_bg);
		 LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(802),DecorationApplication.cpc.changeImageX(113));
		 topBg.setLayoutParams(linearLp);
		 
		 //返回图标
		 ImageView topReturn = (ImageView)findViewById(R.id.top_return);
		 FrameLayout.LayoutParams frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(32);
		 topReturn.setLayoutParams(frameLp);
		 
		 topReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isDirectIn){
					 Intent intent = new Intent(PersonalActivity.this,MainActivity.class);
			 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 			startActivity(intent);
			 			PersonalActivity.this.finish();
				}else{
					PersonalActivity.this.finish();
				}
			//	PersonalActivity.this.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
		 
		 TextView  topReturnTxt = (TextView)findViewById(R.id.top_return_txt);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(118),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(70);
		 topReturnTxt.setLayoutParams(frameLp);
		 
		 
		 
		 
		// ImageView personBg = (ImageView)findViewById(R.id.person_bg);
		// frameLp= new FrameLayout.LayoutParams(
		//		 DecorationApplication.cpc.changeImageX(800),DecorationApplication.cpc.changeImageX(250));
	//	 personBg.setLayoutParams(frameLp);
		 FrameLayout personTop = (FrameLayout)findViewById(R.id.person_top);
		 linearLp= new LinearLayout.LayoutParams(
				 LinearLayout.LayoutParams.MATCH_PARENT,DecorationApplication.cpc.changeImageX(220));
		 personTop.setLayoutParams(linearLp);
		 
		 FrameLayout dividerTitle = (FrameLayout)findViewById(R.id.divider_title);
		 linearLp= new LinearLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(806),DecorationApplication.cpc.changeImageX(93));
		 dividerTitle.setLayoutParams(linearLp);
		 
		 
		 TextView collectTitle = (TextView)findViewById(R.id.collect_title);
		 frameLp= new FrameLayout.LayoutParams(
				 FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);

		 frameLp.gravity = Gravity.TOP|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(285);
		 frameLp.topMargin = DecorationApplication.cpc.changeImageX(28);
		 collectTitle.setLayoutParams(frameLp);
		 
		 TextView setTitle = (TextView)findViewById(R.id.set_title);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(178),DecorationApplication.cpc.changeImageX(87));

		 frameLp.gravity = Gravity.TOP|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(620);
		 frameLp.topMargin = DecorationApplication.cpc.changeImageX(3);
		 setTitle.setLayoutParams(frameLp);
		 
		 setTitle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(PersonalActivity.this, ModifyUserNameActivity.class);
					intent.putExtra(ModifyUserNameActivity.USER_NAME, username.getText());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});
		 
		 
		 faceImage = (ImageView)findViewById(R.id.avatar);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(155),DecorationApplication.cpc.changeImageX(155));
		 frameLp.gravity = Gravity.TOP|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(40);
		 frameLp.topMargin = DecorationApplication.cpc.changeImageX(30);
		
		 faceImage.setLayoutParams(frameLp);
		 Bitmap avatarBmp = DecorationApplication.bc.readBitMap(this, R.drawable.de_avatar, null);
		 faceImage.setImageBitmap(Constants.getRoundedCornerBitmap(avatarBmp,PIC_ROUND_RADIUS));
		 
		 faceImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMyDialog();
			}
		});
		 
		 
		 username = (TextView)findViewById(R.id.username);
		 frameLp= new FrameLayout.LayoutParams(
				 FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);

		 frameLp.gravity = Gravity.TOP|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(240);
		 frameLp.topMargin = DecorationApplication.cpc.changeImageX(40);
		 username.setLayoutParams(frameLp);
		// username.setPadding(DecorationApplication.cpc.changeImageX(55), DecorationApplication.cpc.changeImageX(5),
		//		 DecorationApplication.cpc.changeImageX(30), DecorationApplication.cpc.changeImageX(5));
		 username.setClickable(false);
		 username.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(username.getText() == "loading..."){
//					return;
//				}
				Intent intent = new Intent(PersonalActivity.this, ModifyUserNameActivity.class);
				intent.putExtra(ModifyUserNameActivity.USER_NAME, username.getText());
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		 
		 
		 TextView topExit = (TextView)findViewById(R.id.top_exit);
		 frameLp= new FrameLayout.LayoutParams(
				 DecorationApplication.cpc.changeImageX(96),DecorationApplication.cpc.changeImageX(67));
		 frameLp.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
		 frameLp.rightMargin = DecorationApplication.cpc.changeImageX(40);
		 topExit.setLayoutParams(frameLp);
		 topExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		

			    alert();
//				new AlertDialog.Builder(PersonalActivity.this).setTitle("您确定要退出吗？").setCustomTitle(arg0)
//				
//			    .setIcon(android.R.drawable.ic_dialog_info) 
//			    .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
//			 
//			        @Override 
//			        public void onClick(DialogInterface dialog, int which) { 
//			        // 点击“确认”后的操作 
//						DecorationApplication d = (DecorationApplication)PersonalActivity.this.getApplication();
//					    SharedPreferences mSpUserData = d.getPreference();
//						Editor editor = mSpUserData.edit();
//						editor.putBoolean("loginstatus", false);
//						editor.putString("userid", "");
//						editor.commit();
//						PersonalActivity.this.finish();
//			 
//			        } 
//			    }) 
//			    .setNegativeButton("返回", new DialogInterface.OnClickListener() { 
//			 
//			        @Override 
//			        public void onClick(DialogInterface dialog, int which) { 
//			        // 点击“返回”后的操作,这里不设置没有任何操作 
//			        	
//			        } 
//			    }).show(); 
			}
	});
		 
		 
		 
		 LinearLayout personLike = (LinearLayout)findViewById(R.id.person_like);
		 frameLp= new FrameLayout.LayoutParams(
				 FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);

		 frameLp.gravity = Gravity.TOP|Gravity.LEFT;
		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(265);
		 frameLp.topMargin = DecorationApplication.cpc.changeImageX(140);
		 personLike.setLayoutParams(frameLp);
		 
//		 EditText userIntro = (EditText)findViewById(R.id.user_intro);
//		 
//		 frameLp= new FrameLayout.LayoutParams(
//				 DecorationApplication.cpc.changeImageX(400),DecorationApplication.cpc.changeImageX(80));
//		 frameLp.gravity = Gravity.TOP|Gravity.LEFT;
//		 frameLp.leftMargin = DecorationApplication.cpc.changeImageX(270);
//		 frameLp.topMargin = DecorationApplication.cpc.changeImageX(130);
//		 userIntro.setLayoutParams(frameLp);
		 mLikeNum = (TextView)findViewById(R.id.like_num);
	 }
	 
	 public void alert(){ 
//	         LayoutInflater inflater = getLayoutInflater(); 
//	         View view = inflater.inflate(R.layout.alert, null); 
//
//	         TextView text = (TextView)view.findViewById(R.id.text); 
//	         text.setText("自定义AlertDialog"); 

	         final AlertDialog alert = new AlertDialog.Builder(this).create(); 
	         alert.show(); 

	         alert.getWindow().setLayout(DecorationApplication.cpc.changeImageX(657), DecorationApplication.cpc.changeImageX(311)); 
	        // alert.setTitle("测试"); 
	         alert.getWindow().setContentView(R.layout.mydialog); 
	         Button okButton = (Button)alert.findViewById(R.id.ok);
	         okButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DecorationApplication d = (DecorationApplication)PersonalActivity.this.getApplication();
				    SharedPreferences mSpUserData = d.getPreference();
					Editor editor = mSpUserData.edit();
					editor.putBoolean("loginstatus", false);
					editor.putString("userid", "");
					editor.commit();
					DMenuFragment.getInstance().delAvatar();
					PersonalActivity.this.finish();
				}
			});
	         
	         Button cancelButton = (Button)alert.findViewById(R.id.cancel);
	         cancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					  alert.dismiss();
				}
			});
	         
	 }
	 /**
		 * 显示选择对话框
		 */
		private void showMyDialog() {

			new AlertDialog.Builder(this)
					.setTitle("设置头像")
					.setItems(mAvatarItems, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								Intent intentFromGallery = new Intent();
								intentFromGallery.setType("image/*"); // 设置文件类型
								intentFromGallery
										.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(intentFromGallery,
										IMAGE_REQUEST_CODE);
								break;
							case 1:

								Intent intentFromCapture = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								// 判断存储卡是否可以用，可用进行存储
								if (Constants.hasSdcard()) {

									intentFromCapture.putExtra(
											MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(Environment
													.getExternalStorageDirectory(),
													IMAGE_FILE_NAME)));
								}
							//	intentFromCapture.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								
								startActivityForResult(intentFromCapture,
										CAMERA_REQUEST_CODE);
								break;
							}
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();

		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			if(isDirectIn){
				 Intent intent = new Intent(PersonalActivity.this,MainActivity.class);
		 			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 			startActivity(intent);
		 			PersonalActivity.this.finish();
			}else{
				PersonalActivity.this.finish();
			}
		}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//结果码不等于取消时候
		if (resultCode != 0) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Constants.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+ File.separator+IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(PersonalActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
					 new Thread(imgUploadRun).start();
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	 
	Runnable imgUploadRun = new Runnable(){
		@Override
		public void run() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", UserInfo.getInstance().getUserId());
			
			BitmapDrawable bd =  (BitmapDrawable) faceImage.getDrawable();
			
			
			int retImg =0;
			try {

					retImg = post(DecorationApplication.SERVER_NAME+"index.php?m=setAvatar",
							map, bd.getBitmap());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(retImg==200){
				 Message msg=new Message();
                 msg.what=1;
                 imgUploadhandler.sendMessage(msg);
			}
				
		}
	};
	public  int post(String actionUrl, Map<String, String> params, 
		    Bitmap btm) throws IOException { 
		  StringBuilder sb2 = new StringBuilder(); 
		  String BOUNDARY = java.util.UUID.randomUUID().toString();
		  String PREFIX = "--" , LINEND = "\r\n";
		  String MULTIPART_FROM_DATA = "multipart/form-data"; 
		  String CHARSET = "UTF-8";

		  URL uri = new URL(actionUrl); 
		  HttpURLConnection conn = (HttpURLConnection) uri.openConnection(); 
		  conn.setReadTimeout(5 * 1000); 
		  conn.setDoInput(true);
		  conn.setDoOutput(true);
		  conn.setUseCaches(false); 
		  conn.setRequestMethod("POST"); 
		  conn.setRequestProperty("connection", "keep-alive"); 
		  conn.setRequestProperty("Charsert", "UTF-8"); 
		  conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY); 

		  StringBuilder sb = new StringBuilder(); 
		  for (Map.Entry<String, String> entry : params.entrySet()) { 
		    sb.append(PREFIX); 
		    sb.append(BOUNDARY); 
		    sb.append(LINEND); 
		    sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
		    sb.append("Content-Type: text/plain; charset=" + CHARSET+LINEND);
		    sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
		    sb.append(LINEND);
		    sb.append(entry.getValue()); 
		    sb.append(LINEND); 
		  } 

		  DataOutputStream outStream = new DataOutputStream(conn.getOutputStream()); 
		  outStream.write(sb.toString().getBytes()); 
		  if(btm!=null){
		      StringBuilder sb1 = new StringBuilder(); 
		      sb1.append(PREFIX); 
		      sb1.append(BOUNDARY); 
		      sb1.append(LINEND); 
		      //sb1.append("Content-Disposition: form-data; name=\"file"+(i++)+"\"; filename=\""+file.getKey()+"\""+LINEND);
		      sb1.append("Content-Disposition: form-data; name=\"Filedata[]\"; filename=\""+"inFile"+"\""+LINEND);
		      sb1.append("Content-Type: application/octet-stream; charset="+CHARSET+LINEND);
		      sb1.append(LINEND);
		      outStream.write(sb1.toString().getBytes()); 
		     
		      byte[] Filedata = readFileImage(btm);
		      outStream.write(Filedata, 0, Filedata.length);
		      
		      outStream.write(LINEND.getBytes()); 
		    } 
		  
		  byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes(); 
		  outStream.write(end_data); 
		  outStream.flush(); 

		  int res = conn.getResponseCode(); 
		  InputStream in = null;
		  if (res == 200) {
		    in = conn.getInputStream(); 
		    int ch; 
		    
		    while ((ch = in.read()) != -1) { 
		      sb2.append((char) ch); 
		    } 
		  }
		  
		  return res;
		}
	
	public byte[] readFileImage(Bitmap btm) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		btm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		return outputStream.toByteArray();

	}
	
	private Handler imgUploadhandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Toast.makeText(PersonalActivity.this, "头像修改成功！", Toast.LENGTH_SHORT).show();
			} 
		}
	};
	
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			//Drawable drawable = new BitmapDrawable(photo);
			//Bitmap bitmap = new Bitmap
		//	faceImage.setImageDrawable(drawable);
			faceImage.setImageBitmap(Constants.getRoundedCornerBitmap(photo,PIC_ROUND_RADIUS));
		}
	}
	
	 Runnable readInfoRun = new Runnable(){

			@Override

			public void run() {

			// TODO Auto-generated method stub

				    String urlStr = "" ;
				    
				    urlStr= DecorationApplication.SERVER_NAME+"index.php?m=collectLiGet&page="
				    	+curPage+"&totalCount="+DecorationApplication.pageNum;
					
	        	   
				    String response = CustomerHttpClient.post(urlStr,
				    		new BasicNameValuePair("member_id",UserInfo.getInstance().getUserId()));
				    Log.d("lyl","response is " + response);
				    if(response==null||response.equals("")){
				    	 Message m = new Message();
			        	  //  Bundle responseBundle = new Bundle();
			        	   // responseBundle.putString("response", response);
			        	  //  m.setData(responseBundle);
			        	    m.what = 2;
			        	    readInfoHandler.sendMessage(m);
				    }
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
									DecorationApplication.decorationCollectList.clear();
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
									+ decorationJson.getString(DecorationInfo.DECORATION_ID) + "/" + decorationJson.getString(DecorationInfo.IMG_URL)
									+ "&w=" + DecorationApplication.cpc.getCurWidth();
									tempInfo.setImgUrl(tempImgurl);
									
									//tempInfo.setImgWidth();
									
									
									 
									tempInfo.setImgHeight(
											(int)(decorationJson.getJSONObject("getimagesize").getInt("1")
											* DecorationApplication.cpc.getCurWidth()/2) /
											decorationJson.getJSONObject("getimagesize").getInt("0")
											);
									
									tempInfo.setIntroduce(decorationJson.getString(DecorationInfo.INTRODUCE));
									tempInfo.setPraise(decorationJson.getInt(DecorationInfo.PRAISE));
									tempInfo.setMerchantName(decorationJson.getString(DecorationInfo.MERCHANT_NAME));
									tempInfo.setMerchantAddress(decorationJson.getString(DecorationInfo.MERCHANT_ADDRESS));
									tempInfo.setMerchantTel(decorationJson.getString(DecorationInfo.MERCHANT_TEL));
									tempInfo.setMerchantIntro(decorationJson.getString(DecorationInfo.MERCHANT_INTRO));
								//	tempInfo.setTitle(decorationJson.getString(DecorationInfo.TITLE));
								//	tempInfo.setTitle(decorationJson.getString(DecorationInfo.TITLE));
									
										
									DecorationApplication.decorationCollectList.add(tempInfo);
									//mAdapter.addItem(tempInfo);
									
									
								
									
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
		            	 mLikeNum.setText(String.valueOf(totalDecorationNum)) ;
		            	isLoading = false;
		            	if(DecorationApplication.decorationCollectList.size()==0){
		            		
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
		            	
		            	break;
		            case 2:
		                //关闭
		            //	faceImage.setImageBitmap(bitmap);
		            //	Toast.makeText(mContainerActivity, "读取数据失败", Toast.LENGTH_SHORT).show();
		            	mLoadingImg.clearAnimation();
		            	mLoadingImg.setVisibility(View.GONE);
		            	mLoadingLinear.setVisibility(View.VISIBLE);
		            	
		            	 mBottomLoading.setVisibility(View.GONE);
		            	
		         //       mNetworkError.setVisibility(View.VISIBLE);
		            //	mAdapter.notifyDataSetChanged();
		            	isLoading = false;
		            	
		            	break;
		            }
		        }
		    };
		    Runnable readImgRun = new Runnable(){

				@Override

				public void run() {

				// TODO Auto-generated method stub
				 	 try{
				 		 URL  url=null;
				 	//	 if(User.type==LoginFragment.ACCOUNT_TYPE_SYS){
				 		      url = new URL(DecorationApplication.SERVER_NAME+"index.php?m=getPic&t=1&id="+
				 		    		  UserInfo.getInstance().getUserId() +"&w="+DecorationApplication.cpc.changeImageX(200));
				 		
				 	//	 }
				 	//	 else{
//				 			 if(User.isFirstOpenLogin){
//				 				 url = new URL(User.avatarUrl);
//				 				 new Thread(imgUploadRun).start();
//				 			 }else{
//				 				 url = new URL(HyFootballApplication.SERVER_NAME+"index.php?m=getPic&t=1&id="+User.id +"&w=200");
//				 			 }
//				 		
//				 		 }
	                  Log.d("lyl","url is " + url);
	                //   if(checkConnection(AccountFragment.this.getActivity())){
	                	   HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
	                       conn.setDoInput(true);
	                       conn.connect(); 
	                       InputStream inputStream=conn.getInputStream();
	                       bitmap = BitmapFactory.decodeStream(inputStream); 
	                       Message msg=new Message();
	                       msg.what=1;
	                       imghandler.sendMessage(msg);
	                 //  }else{
	                //	   Message msg=new Message();
	                //       msg.what=2;
	                //       imghandler.sendMessage(msg);
	                //   }
	                   
	                
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
			  
			            	faceImage.setImageBitmap(Constants.getRoundedCornerBitmap(bitmap,PIC_ROUND_RADIUS));
			            	
			                break;
			            
			            }
			        }
			    };
			    Runnable readUserInfoRun = new Runnable(){

					@Override

					public void run() {

					// TODO Auto-generated method stub

						    String urlStr = "" ;
//						    if(User.type == LoginFragment.ACCOUNT_TYPE_SYS){
						    	urlStr= DecorationApplication.SERVER_NAME+"index.php?m=tables&t=member&id=" + UserInfo.getInstance().getUserId();
//						    }
//						    else{
//						    	urlStr= "http://shouji400.com/fcliaoning/index.php?m=tables&t=open&id=" + User.id;
//						    }
			        	   
						    String response = CustomerHttpClient.post(urlStr);
			        	    Message m = new Message();
			        	    Bundle responseBundle = new Bundle();
			        	    responseBundle.putString("response", response);
			        	    m.setData(responseBundle);
			        	    m.what = 0;
			        	    readUserInfoHandler.sendMessage(m);

					}
					};
			    private Handler readUserInfoHandler = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.what == 0) {
							//  String urlStr = "http://shouji400.com/fcliaoning/index.php?m=tables&t=member&id=" + User.id;
				          	//  String response = CustomerHttpClient.post(urlStr);
							String response = msg.getData().getString("response");
							
							
							if(response.equals("\"-52\"")||response.equals("error")){
								
								//mSave.setClickable(false);
									 Toast.makeText(PersonalActivity.this, "读取用户数据出错,请稍后再试", Toast.LENGTH_SHORT).show();
								return;
							}
								 try {
					          	    	
					          			JSONObject jo = new JSONObject(response);
                                        if(jo.getString("nickname")==""){
                                        	username.setText("请设置昵称");
                                        }else{
                                        	username.setText(jo.getString("nickname"));
                                        }
                                        username.setClickable(true);
					          			

					          		//	mProvinceSpinner.setSelection(position);
					          		//	mCitySpinner.setSelection(position);
					          		//	mSexSpinner.setSelection(mSexItems.);
							}  catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							}
				          	   
					}
				}; 
	 
}
