package com.example.tvrecommend.model;


public class DvbProgramModel {

    private final String mMediaProgramId;
    private final String mContentId;
    private final String mTitle;
    private final String mDescription;
    private final String mBgImageUrl;
    private final String mCardImageUrl;
    private final String mMediaUrl;
    private final String mPreviewMediaUrl;
    private final String mCategory;
    private long mProgramId;
    private int mViewCount;

    public DvbProgramModel(String title, String description, String bgImageUrl, String cardImageUrl,
                           String category, String mediaProgramId, String contentId) {
        mMediaProgramId = mediaProgramId;
        mContentId = contentId;
        mTitle = title;
        mDescription = description;
        mBgImageUrl = bgImageUrl;
        mCardImageUrl = cardImageUrl;
        mMediaUrl = "";
        mPreviewMediaUrl = "";
        mCategory = category;
    }

    public String getMediaProgramId() {
        return mMediaProgramId;
    }

    public String getContentId() {
        return mContentId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getBgImageUrl() {
        return mBgImageUrl;
    }

    public String getCardImageUrl() {
        return mCardImageUrl;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public String getPreviewMediaUrl() {
        return mPreviewMediaUrl;
    }

    public String getCategory() {
        return mCategory;
    }

    public long getProgramId() {
        return mProgramId;
    }

    public void setProgramId(long programId) {
        mProgramId = programId;
    }

    public int getViewCount() {
        return mViewCount;
    }

    public void setViewCount(int viewCount) {
        mViewCount = viewCount;
    }
}
