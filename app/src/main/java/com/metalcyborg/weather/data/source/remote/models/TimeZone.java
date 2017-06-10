package com.metalcyborg.weather.data.source.remote.models;

import com.google.gson.annotations.SerializedName;

public class TimeZone {

    @SerializedName("dstOffset")
    private int mDstOffset;

    @SerializedName("rawOffset")
    private int mRawOffset;

    @SerializedName("status")
    private String mStatus;

    @SerializedName("timeZoneId")
    private String mTimeZoneId;

    @SerializedName("timeZoneName")
    private String mTimeZoneName;

    public int getDstOffset() {
        return mDstOffset;
    }

    public int getRawOffset() {
        return mRawOffset;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getTimeZoneId() {
        return mTimeZoneId;
    }

    public String getTimeZoneName() {
        return mTimeZoneName;
    }
}
