package com.aiwue.update;

public class PushMessage {

	public static final String MESSAGE_TYPE_WEB      = "web";
	public static final String MESSAGE_TYPE_APP      = "app";
	public static final String MESSAGE_TYPE_ACTIVITY = "activity";
	public static final String MESSAGE_TYPE_WECHAT = "wechat";
	public static final String MESSAGE_TYPE_NONE      ="none";
	
	public static final String ACTIVITY_TYPE_ANTIVIRUS = "antivirus";
	public static final String ACTIVITY_TYPE_PRIVACY    ="privacy";
	public static final String ACTIVITY_TYPE_AD             ="ad";
	
	
	public String type;
	public String noti_title;
	public String noti_content;
	public String des_title;
	public String description;
	public String url;
	public String btn_positive;
	public String btn_negetive;
	public String activity;
	
}
