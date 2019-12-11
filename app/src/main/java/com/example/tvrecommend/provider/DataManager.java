package com.example.tvrecommend.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.tvrecommend.provider.data.VideoContract;

public class DataManager {

    private Context mContext;
    private static DataManager mDataManager;

    public static DataManager getInstance(Context context){
        if (mDataManager == null){
            mDataManager = new DataManager(context);
        }
        return mDataManager;
    }

    public static DataManager getInstance(){
        return mDataManager;
    }

    private DataManager(Context context){
        mContext = context;
    }


    public synchronized void bulkInsertAllChannels(ContentValues[] values){
        ContentResolver resolver = mContext.getContentResolver();
        resolver.bulkInsert(VideoContract.VideoEntry.CONTENT_URI,values);
    }

    public void deleteAllChanelsFromDb(){
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(VideoContract.VideoEntry.CONTENT_URI,null,null);
    }

    public ContentValues createOneContentValue(){
        return null;
    }

    public ContentValues[]  createManyContentValue(){
        return null;
    }
}
