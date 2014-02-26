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

package com.gren.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gren.decoration.DecorationApplication;

/**
 * 
 * Constants used by multiple classes in this package
 */
public final class Constants {

	public static final String SERVER_NAME = "http://www.00up.com/fcliaoning/";

	public static final boolean DEBUG = true;

	public static final String IMAGE_CACHE_DIR = "images";

	// sina appkey
	public static final String CONSUMER_KEY = "251027555";// 替换为开发者的appkey，例如"1646212860";
	public static final String REDIRECT_URL = "http://www.sina.com";

	// qq appkey
	public static final String QQ_SCOPE = "all";
	public static final String QQ_APP_ID = "100364254";

	private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天

	public final static String CLASS_NAME = "class_name";
	public final static String CATEGORY = "category";
	public final static String IS_SEARCH = "search";
	public static final String POSITION_DATA_EXTRA = "position";

	/**
	 * 返回文字描述的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeFormatText(Date now, Date update) {
		if (update == null) {
			return null;
		}
		// long diff = new Date().getTime() - date.getTime();
		long diff = now.getTime() - update.getTime();
		long r = 0;
		// if (diff > year) {
		// r = (diff / year);
		// return r + "年前";
		// }
		// if (diff > month) {
		// r = (diff / month);
		// return r + "个月前";
		// }
		// if (diff > day) {
		// r = (diff / day);
		// return r + "天前";
		// }
		if (diff > day) {
			// return "day";
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return format.format(update).substring(0, 10);

		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "个小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}

	/**
	 * 检查是否存在SDCard
	 * @return
	 */
	public static  boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
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
	/**
	 * * 转换图片成圆形          * @param bitmap 传入Bitmap对象          * @return
	 *          
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);

		return output;
	}
	private static FrameLayout.LayoutParams frameLp;
	
	
    public static FrameLayout.LayoutParams getFrameLp(int width,int height,int leftMargin,int topMargin){
    	 int lpWidth,lpHeight;
    	 if(width<0){
    		 lpWidth = width;
    	 }else{
    		 lpWidth = DecorationApplication.cpc.changeImageX(width);
    	 }
    	 if(height <0){
    		 lpHeight = height;
    	 }else{
    		 lpHeight= DecorationApplication.cpc.changeImageX(height);
    	 }
    	frameLp= new FrameLayout.LayoutParams(lpWidth,lpHeight);
		frameLp.gravity = Gravity.LEFT|Gravity.TOP;
		frameLp.leftMargin = DecorationApplication.cpc.changeImageX(leftMargin);
		frameLp.topMargin = DecorationApplication.cpc.changeImageX(topMargin);
		return frameLp;
    }

}
