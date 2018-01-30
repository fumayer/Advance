package com.aiwue.model;

import com.aiwue.R;
import com.aiwue.base.AiwueApplication;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户model
 * Created by Yibao on 2017年4月13日10:44:39
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public class User implements Serializable {
    private Integer id = 0;
    private String nickName = "Unknown";
    private String realName;
    private String title;
    private Integer isCertified = 0;
    private String headPicName="";
    private String cover;
    private String slogen;
    private String introduction;
    private Integer baikeId;
    private Integer bbsPlateId;
    private String accessToken;
    private Long birthday;
    private Long pastday;
    private Integer liveCityId;
    private String liveCity;
    private Integer gendar = 0;
    private String birthPlace;
    private Integer height = 0;
    private Integer weight = 0;
    private Integer isMaster = 0;
    private Integer score = 0;
    private Integer balance = 0;
    private Integer grade = 0;
    private Integer memberLevel = 0;
    private Long memberStartTime;
    private String mobile;
    private String email;
    private String address;
    private String postcode;
    private String pushId;
    private String qq;
    private String weibo;
    private String weixin;
    private String regDevice;
    private Integer invitorId;
    private String inviteCode;
    private Integer praiseNum = 0;
    private Integer fansNum = 0;
    private Integer careNum = 0;
    private Integer articleNum = 0;
    private Integer statusNum = 0;
    private Integer questionNum = 0;
    private Integer answerNum = 0;
    private Integer signNum = 0;
    private Long lastSignTime;
    private Integer courseDoneNum = 0;
    private Integer coursePublishedNum = 0;
    private Integer illegalNum = 0;
    private Integer trainMinutes = 0;
    private Long createTime;
    private Long updateTime;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIsCertified() {
        return isCertified;
    }

    public void setIsCertified(Integer isCertified) {
        this.isCertified = isCertified;
    }

    public String getHeadPicName() {
        return headPicName;
    }

    public void setHeadPicName(String headPicName) {
        this.headPicName = headPicName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSlogen() {
        return slogen;
    }

    public void setSlogen(String slogen) {
        this.slogen = slogen;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getBaikeId() {
        return baikeId;
    }

    public void setBaikeId(Integer baikeId) {
        this.baikeId = baikeId;
    }

    public Integer getBbsPlateId() {
        return bbsPlateId;
    }

    public void setBbsPlateId(Integer bbsPlateId) {
        this.bbsPlateId = bbsPlateId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Long getPastday() {
        return pastday;
    }

    public void setPastday(Long pastday) {
        this.pastday = pastday;
    }

    public Integer getLiveCityId() {
        return liveCityId;
    }

    public void setLiveCityId(Integer liveCityId) {
        this.liveCityId = liveCityId;
    }

    public String getLiveCity() {
        return liveCity;
    }

    public void setLiveCity(String liveCity) {
        this.liveCity = liveCity;
    }

    public Integer getGendar() {
        return gendar;
    }

    public void setGendar(Integer gendar) {
        this.gendar = gendar;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Long getMemberStartTime() {
        return memberStartTime;
    }

    public void setMemberStartTime(Long memberStartTime) {
        this.memberStartTime = memberStartTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getRegDevice() {
        return regDevice;
    }

    public void setRegDevice(String regDevice) {
        this.regDevice = regDevice;
    }

    public Integer getInvitorId() {
        return invitorId;
    }

    public void setInvitorId(Integer invitorId) {
        this.invitorId = invitorId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(Integer praiseNum) {
        this.praiseNum = praiseNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getCareNum() {
        return careNum;
    }

    public void setCareNum(Integer careNum) {
        this.careNum = careNum;
    }

    public Integer getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }

    public Integer getStatusNum() {
        return statusNum;
    }

    public void setStatusNum(Integer statusNum) {
        this.statusNum = statusNum;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Integer getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(Integer answerNum) {
        this.answerNum = answerNum;
    }

    public Integer getSignNum() {
        return signNum;
    }

    public void setSignNum(Integer signNum) {
        this.signNum = signNum;
    }

    public Long getLastSignTime() {
        return lastSignTime;
    }

    public void setLastSignTime(Long lastSignTime) {
        this.lastSignTime = lastSignTime;
    }

    public Integer getCourseDoneNum() {
        return courseDoneNum;
    }

    public void setCourseDoneNum(Integer courseDoneNum) {
        this.courseDoneNum = courseDoneNum;
    }

    public Integer getCoursePublishedNum() {
        return coursePublishedNum;
    }

    public void setCoursePublishedNum(Integer coursePublishedNum) {
        this.coursePublishedNum = coursePublishedNum;
    }

    public Integer getIllegalNum() {
        return illegalNum;
    }

    public void setIllegalNum(Integer illegalNum) {
        this.illegalNum = illegalNum;
    }

    public Integer getTrainMinutes() {
        return trainMinutes;
    }

    public void setTrainMinutes(Integer trainMinutes) {
        this.trainMinutes = trainMinutes;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

//========================================
//    public String getGendarStr() {
//        if (gendar <0 || gendar > 2)
//            return AiwueApplication.getAppContext().getString(gendarEnum[2]);
//        return AiwueApplication.getAppContext().getString(gendarEnum[gendar]);
//    }

    public String getBirthdayStr() {
        if (birthday != null) {
            SimpleDateFormat my = new SimpleDateFormat("yyyy-mm-dd");
            return my.format(new Date(birthday));
        }
        return "";
    }

    public String getBalanceStr() {
        if (balance == null) return "0" + AiwueApplication.getAppContext().getString(R.string.personaldata_money_unit);
        float num= ((float)balance)/100;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String s = df.format(num);//返回的是String类型
        return num + AiwueApplication.getAppContext().getString(R.string.personaldata_money_unit);
    }

    public String getHeightStr() {
        if (height == null)
            return AiwueApplication.getAppContext().getString(R.string.personaldata_height_unit);
        return height + AiwueApplication.getAppContext().getString(R.string.personaldata_height_unit);
    }

    public String getWeightStr() {
        if (weight == null)
            return AiwueApplication.getAppContext().getString(R.string.personaldata_weight_unit);
        return weight + AiwueApplication.getAppContext().getString(R.string.personaldata_weight_unit);
    }
}
