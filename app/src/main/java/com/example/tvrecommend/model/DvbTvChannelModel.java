package com.example.tvrecommend.model;


public class DvbTvChannelModel {
    private String mDescription;
    private String mDisplayName;
    private long mChannelId;

    public DvbTvChannelModel(String displayName, String description) {
        mDisplayName = displayName;
        mDescription = description;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public String getDescription() {
        return mDescription;
    }


    public long getChannelId() {
        return mChannelId;
    }

    public void setChannelId(long channelId) {
        mChannelId = channelId;
    }


}
