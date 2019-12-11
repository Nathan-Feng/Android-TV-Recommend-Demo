/*
 * Copyright (c) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tvrecommend.provider;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.tvrecommend.R;
import com.example.tvrecommend.provider.data.VideoContract;


/*
 * Details activity class that loads VideoDetailsFragment class
 */
public class VideoDetailsActivity extends FragmentActivity {
    public static final String NOTIFICATION_ID = "NotificationId";
    private static final int NO_NOTIFICATION = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("zyf", "VideoDetailsFragment:onCreate:");
        setContentView(R.layout.fragment_details);
        if (!hasGlobalSearchIntent()) {
            removeNotification(getIntent()
                    .getIntExtra(VideoDetailsActivity.NOTIFICATION_ID, NO_NOTIFICATION));
        }

    }

    private void removeNotification(int notificationId) {
        if (notificationId != NO_NOTIFICATION) {
            NotificationManager notificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        }
    }

    /**
     * Check if there is a global search intent. If there is, load that video.
     */
    private boolean hasGlobalSearchIntent() {
        Intent intent = getIntent();
        String intentAction = intent.getAction();
        String globalSearch = getString(R.string.global_search);
        Log.d("zyf", "VideoDetailsFragment:hasGlobalSearchIntent: intentAction:"+intentAction);
        if (globalSearch.equalsIgnoreCase(intentAction)) {
            Uri intentData = intent.getData();
            String videoId = intentData.getLastPathSegment();
            Log.d("zyf", "VideoDetailsFragment:intentData:"+intentData.toString());
            Bundle args = new Bundle();
            args.putString(VideoContract.VideoEntry._ID, videoId);
            return true;
        }
        return false;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("zyf", "VideoDetailsFragment:onNewIntent:"+intent.getAction());
    }
}
