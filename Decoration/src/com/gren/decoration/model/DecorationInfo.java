package com.gren.decoration.model;

import java.util.Date;

public class DecorationInfo {

	public static final String DECORATION_ID = "decoration_id";
	public static final String TITLE = "title";
	public static final String TAGS = "tags";
	public static final String CLASS_NAME = "class_name";
	public static final String PUBLIC_PRICE = "public_price";
	public static final String MY_PRICE = "my_price";
	public static final String IMG_URL = "show_img";
	public static final String INTRODUCE = "introduce";
	public static final String PRAISE = "praise";
	public static final String MERCHANT_NAME = "merchant_name";
	public static final String MERCHANT_ADDRESS = "merchant_address";
	public static final String MERCHANT_TEL = "merchant_tel";
	public static final String MERCHANT_INTRO = "merchant_intro";
	public static final String MERCHANT_LONGITUDE = "merchant_longitude";
	public static final String MERCHANT_LATITUDE = "merchant_latitude";

 
	
	
	private String title;                //商品名称
	private String decorationId;         //商品id
	private String className;            //所属大类名称
	private int publicPrice;             //商品原价
	private int myPrice;                 //商品优惠价
	private String imgUrl;               //图片地址
	private String introduce;            //商品简介
	private int praise;                  //赞的数量
    private Date upDate;                 //更新时间
    private String tags;                 //tag标签
    private String merchantName;         //商家名称
    private String merchantAddress;      //商家地址
    private String merchantTel;          //商家电话
    private String merchantIntro;        //商家简介
    private int merchantLongitude;    //商家经度
    private int merchantLatitude;     //商家纬度
    private int imgWidth;              //图片宽度
    private int imgHeight;            //图片高度
    

    public void setTitle(String title){
		 this.title = title;
	}
	public String getTitle(){
		return title;
	}
	
    public void setDecorationId(String decorationId){
		 this.decorationId = decorationId;
	}
	public String getDecorationId(){
		return decorationId;
	}
    public void setClassName(String className){
		 this.className = className;
	}
	public String getClassName(){
		return className;
	}
    public void setImgUrl(String imgUrl){
		 this.imgUrl = imgUrl;
	}
	public String getImgUrl(){
		return imgUrl;
	}
    public void setIntroduce(String introduce){
		 this.introduce = introduce;
	}
	public String getIntroduce(){
		return introduce;
	}
	
	public void setMerchantName(String merchantName){
		 this.merchantName = merchantName;
	}
	public String getMerchantName(){
		return merchantName;
	}
	public void setMerchantAddress(String merchantAddress){
		 this.merchantAddress = merchantAddress;
	}
	public String getMerchantAddress(){
		return merchantAddress;
	}
	public void setMerchantTel(String merchantTel){
		 this.merchantTel = merchantTel;
	}
	public String getMerchantTel(){
		return merchantTel;
	}
	public void setMerchantIntro(String merchantIntro){
		 this.merchantIntro = merchantIntro;
	}
	public String getMerchantIntro(){
		return merchantIntro;
	}
	public void setMerchantLongitude(int merchantLongitude){
		 this.merchantLongitude = merchantLongitude;
	}
	public int getMerchantLongitude(){
		return merchantLongitude;
	}
	public void setMerchantLatitude(int merchantLatitude){
		 this.merchantLatitude = merchantLatitude;
	}
	public int getMerchantLatitude(){
		return merchantLatitude;
	}
	public void setPublicPrice(int publicPrice){
		 this.publicPrice = publicPrice;
	}
	public int getPublicPrice(){
		return publicPrice;
	}
	public void setMyPrice(int myPrice){
		 this.myPrice = myPrice;
	}
	public int getMyPrice(){
		return myPrice;
	}
	public void setPraise(int praise){
		 this.praise = praise;
	}
	public int getPraise(){
		return praise;
	}
	
	public void setImgWidth(int imgWidth){
		 this.imgWidth = imgWidth;
	}
	public int getImgWidth(){
		return imgWidth;
	}
	
	public void setImgHeight(int imgHeight){
		 this.imgHeight = imgHeight;
	}
	public int getImgHeight(){
		return imgHeight;
	}
		

}
