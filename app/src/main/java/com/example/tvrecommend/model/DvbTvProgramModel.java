package com.example.tvrecommend.model;


public class DvbTvProgramModel {

    private long mChannelId;
    private String mContentId;
    private String mTitle;
    private String mDescription;
    private String mCardImageUrl;
    private String mCategory;
    private long mProgramId;
    private String mAuthor;
    private long mDurationMillis;

    public DvbTvProgramModel( long channelId,long programId,String title, String description,String cardImageUrl,
                             String category, String contentId) {
        mChannelId = channelId;
        mProgramId = programId;
        mContentId = contentId;
        mTitle = title;
        mDescription = description;
        mCardImageUrl = cardImageUrl;
        mCategory = category;
    }

    public long getChannelId() {
        return mChannelId;
    }

    public void setChannelId(long channelId) {
        mChannelId = channelId;
    }

    public String getContentId() {
        return mContentId;
    }

    public void setContentId(String contentId) {
        mContentId = contentId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCardImageUrl() {
        return mCardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        mCardImageUrl = cardImageUrl;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public long getProgramId() {
        return mProgramId;
    }

    public void setProgramId(long programId) {
        mProgramId = programId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public long getDurationMillis() {
        return mDurationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        mDurationMillis = durationMillis;
    }
}
