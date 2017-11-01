package com.asia.yongyou.yongyouagent.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 身份证信息类
 *
 * @author Created by liuwei
 * @time on 2017/9/2
 */
public class IDCardInfo implements Parcelable {
    private String partyName;
    private String gender;
    private String nation;
    private String bornDay;
    private String certAddress;
    private String certNumber;
    private String certOrg;//签发机关
    private String validDate;
    private Bitmap picBitmap;
    private String englishName;
    private String effDate;//开始日期
    private String expDate;//终止日期


    public IDCardInfo() {
    }


    protected IDCardInfo(Parcel in) {
        partyName = in.readString();
        gender = in.readString();
        nation = in.readString();
        bornDay = in.readString();
        certAddress = in.readString();
        certNumber = in.readString();
        certOrg = in.readString();
        validDate = in.readString();
        picBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        englishName = in.readString();
        effDate = in.readString();
        expDate = in.readString();
    }

    public static final Creator<IDCardInfo> CREATOR = new Creator<IDCardInfo>() {
        @Override
        public IDCardInfo createFromParcel(Parcel in) {
            return new IDCardInfo(in);
        }

        @Override
        public IDCardInfo[] newArray(int size) {
            return new IDCardInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(partyName);
        parcel.writeString(gender);
        parcel.writeString(nation);
        parcel.writeString(bornDay);
        parcel.writeString(certAddress);
        parcel.writeString(certNumber);
        parcel.writeString(certOrg);
        parcel.writeString(validDate);
        parcel.writeParcelable(picBitmap, i);
        parcel.writeString(englishName);
        parcel.writeString(effDate);
        parcel.writeString(expDate);
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBornDay() {
        return bornDay;
    }

    public void setBornDay(String bornDay) {
        this.bornDay = bornDay;
    }

    public String getCertAddress() {
        return certAddress;
    }

    public void setCertAddress(String certAddress) {
        this.certAddress = certAddress;
    }

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public String getCertOrg() {
        return certOrg;
    }

    public void setCertOrg(String certOrg) {
        this.certOrg = certOrg;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public Bitmap getPicBitmap() {
        return picBitmap;
    }

    public void setPicBitmap(Bitmap picBitmap) {
        this.picBitmap = picBitmap;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    @Override
    public String toString() {
        return "IDCardInfo{" +
                "partyName='" + partyName + '\'' +
                ", gender='" + gender + '\'' +
                ", nation='" + nation + '\'' +
                ", bornDay='" + bornDay + '\'' +
                ", certAddress='" + certAddress + '\'' +
                ", certNumber='" + certNumber + '\'' +
                ", certOrg='" + certOrg + '\'' +
                ", validDate='" + validDate + '\'' +
                ", picBitmap=" + picBitmap +
                ", englishName='" + englishName + '\'' +
                ", effDate='" + effDate + '\'' +
                ", expDate='" + expDate + '\'' +
                '}';
    }
}
