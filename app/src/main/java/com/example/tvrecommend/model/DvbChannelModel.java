package com.example.tvrecommend.model;

import java.util.List;

public class DvbChannelModel {
    private final String mName;
    private final String mDescription;
    private final String mMediaUri;
    private final String mBgImage;
    private final String mTitle;
    private final String mMediaChannelId;
    private List<DvbProgramModel> mPrograms;
    private boolean mChannelPublished;
    private long mChannelId;

    public DvbChannelModel(String name, List<DvbProgramModel> programs, String mediaChannelId) {
        mName = name;
        mTitle = "playlist title";
        mDescription = "playlist description";
        mMediaUri = "dsf";
        mBgImage = "asdf";
        mPrograms = programs;
        mMediaChannelId = mediaChannelId;
    }


    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getMediaUri() {
        return mMediaUri;
    }

    public String getBgImage() {
        return mBgImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMediaChannelId() {
        return mMediaChannelId;
    }

    public List<DvbProgramModel> getPrograms() {
        return mPrograms;
    }

    public void setPrograms(List<DvbProgramModel> programs) {
        mPrograms = programs;
    }

    public boolean isChannelPublished() {
        return mChannelPublished;
    }

    public void setChannelPublished(boolean channelPublished) {
        mChannelPublished = channelPublished;
    }

    public long getChannelId() {
        return mChannelId;
    }

    public void setChannelId(long channelId) {
        mChannelId = channelId;
    }


}
