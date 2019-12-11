
package com.example.tvrecommend.util;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesHelper {

    private static final String TAG = "SharedPreferencesHelper";

    private static final String PREFS_NAME = "hisense.dvb.recommendations";

    private static final String KEY_TV_CHANNEL_ID = "tv_channel_id";


    public static long getChannelId(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long channelId = sharedPreferences.getLong(KEY_TV_CHANNEL_ID, -1);
        if (channelId == -1) {
            // no channel in share preference;
            return channelId;
        }

        return channelId;
    }


    public static void setChannelId(Context context, long channelId) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_TV_CHANNEL_ID,channelId);
        editor.apply();
    }
}
