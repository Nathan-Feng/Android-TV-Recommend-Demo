/*
 * Copyright (c) 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.example.tvrecommend;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.tvrecommend.util.AppLinkHelper;


/**
 * 用户在launcher点击推荐的内容后，会启动如下入口
 * 动作包括：1.启动app，2.播放直播，3.播放点播内容
 *
 */
public class AppLinkActivity extends Activity {

    private static final String TAG = "zyfAppLinkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        Log.v(TAG, uri.toString());

        if (uri.getPathSegments().isEmpty()) {
            Log.e(TAG, "Invalid uri " + uri);
            finish();
            return;
        }

        if (AppLinkHelper.isLivePlayUri(uri)){
            //TODO
            Log.e(TAG, "isLivePlayUri ");
        } else if (AppLinkHelper.isFilePlayUri(uri)){
            //TODO
            Log.e(TAG, "isFilePlayUri ");
        } else if (AppLinkHelper.isStartAppUri(uri)){
            //TODO
            Log.e(TAG, "isStartAppUri ");
        } else {
            throw new IllegalArgumentException("Invalid Action " + uri);
        }
    }

//    private void startLivePlaying(long progIndex) {
//        Intent playMovieIntent = new Intent(this, liveplay.class);
//        playMovieIntent.putExtra("LivePlay", progIndex);
//        startActivity(playMovieIntent);
//    }
//
//    private void startFilePlaying(String filePath) {
//        Intent playMovieIntent = new Intent(this, pvrActivity.class);
//        playMovieIntent.putExtra("FilePlay", filePath);
//        startActivity(playMovieIntent);
//    }

    private void startApp() {
        Intent startAppIntent = new Intent(this, MainActivity.class);
        startActivity(startAppIntent);
    }
}
