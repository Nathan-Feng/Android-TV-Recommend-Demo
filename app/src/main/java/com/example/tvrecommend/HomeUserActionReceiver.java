package com.example.tvrecommend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContract;
import android.util.Log;

import androidx.tvprovider.media.tv.TvContractCompat;

import com.example.tvrecommend.util.DvbChannelProviderUtil;


public class HomeUserActionReceiver extends BroadcastReceiver {
    private static final String TAG = "HomeUserActionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d("zyf", "onReceivexxxxxaaaaabbbb: " + intent.getAction());
        Log.d("zyf", "onReceive:data: " + intent.getData());
        Log.d("zyf", "onReceive:data: " + intent.getExtras());
        if (action == null) return;
        switch (action) {
            case TvContract.ACTION_INITIALIZE_PROGRAMS:
                final long next_id = intent.getLongExtra(TvContractCompat.EXTRA_WATCH_NEXT_PROGRAM_ID, -1L);
                Log.d(TAG, "zyf xxxxxxasdfasdf onReceive: ACTION_INITIALIZE_PROGRAMS in MyApplication" +  ", " + next_id);
                long mChannelId = DvbChannelProviderUtil.createOrUpdateChannel(context);
                DvbChannelProviderUtil.updatePrograms(context,mChannelId);
                break;
            case TvContract.ACTION_WATCH_NEXT_PROGRAM_BROWSABLE_DISABLED:
                final long preview_program_id = intent.getLongExtra(TvContractCompat.EXTRA_PREVIEW_PROGRAM_ID, -1L);
                final long preview_internal_id = intent.getLongExtra(TvContractCompat.EXTRA_WATCH_NEXT_PROGRAM_ID, -1L);
                Log.d(TAG, "zyf onReceive: ACTION_WATCH_NEXT_PROGRAM_BROWSABLE_DISABLED, " + preview_program_id + ", " + preview_internal_id);
                break;
            case TvContract.ACTION_PREVIEW_PROGRAM_BROWSABLE_DISABLED:
                final long watch_next_program_id = intent.getLongExtra(TvContractCompat.EXTRA_PREVIEW_PROGRAM_ID, -1L);
                final long watch_next_internal_id = intent.getLongExtra(TvContractCompat.EXTRA_WATCH_NEXT_PROGRAM_ID, -1L);
                Log.d(TAG, "zyf onReceive: ACTION_PREVIEW_PROGRAM_BROWSABLE_DISABLED, " + watch_next_program_id + ", " + watch_next_internal_id);
                break;
            default:
                break;
        }
    }
}
