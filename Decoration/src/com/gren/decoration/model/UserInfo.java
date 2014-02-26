package com.gren.decoration.model;

//用户单例类，为当前登录用户的信息类
public class UserInfo {

	private static UserInfo mUserInfo= new UserInfo();
	private String mUserId;
	private String mUserName;
	private String mUserSelfIntro;
	
	private UserInfo(){
		
	}
	public static UserInfo getInstance(){
		return mUserInfo;
	}
	public void setUserId(String userId){
		mUserId = userId;
	}
	public String getUserId(){
		return mUserId;
	}
	
	public void setUserName(String userName){
		mUserName = userName;
	}
	public String getUserName(){
		return mUserName;
	}
	
	public void setUserSelfIntro(String userSelfIntro){
		mUserSelfIntro = userSelfIntro;
	}
	public String getUserSelfIntro(){
		return mUserSelfIntro;
	}
	
	
}
