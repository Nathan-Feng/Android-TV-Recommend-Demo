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
package com.example.tvrecommend.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.core.content.FileProvider;
import androidx.tvprovider.media.tv.TvContractCompat;

import com.example.tvrecommend.model.DvbTvChannelModel;
import com.example.tvrecommend.model.DvbTvProgramModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/** Manages interactions with the TV Provider. */
public class DvbChannelProviderUtil {

    private static final String TAG = "zyfDvbChannelProvider";

    private static DvbTvChannelModel createDvbChannelModel(Context context){
//        Uri usbUri = getUSBCardImageFileUri(context);
//        Uri pvrUri = getPVRCardImageFileUri(context);
//        grantUriPermissionToApp(context,"com.google.android.tvlauncher", usbUri);
//        grantUriPermissionToApp(context,"com.google.android.tvlauncher", pvrUri);

        DvbTvChannelModel usbChannel = new DvbTvChannelModel("HisenseDvbxxx", "dvb description");
        return usbChannel;
    }

    private static long createChannel(Context context) {
        DvbTvChannelModel channelModel = createDvbChannelModel(context);
        long channelId = DvbTVProvider.addChannel(context,channelModel);
        if (channelId == 0){
            //TODO
            Log.v(TAG, "createChannel add channel error!");
        }
        return channelId;
    }

    private static long updateChannel(Context context) {
        DvbTvChannelModel channelModel = createDvbChannelModel(context);
        channelModel.setDescription("update!!");
        long ret = DvbTVProvider.updateChannel(context,channelModel);
        if (ret == -1){
            //TODO
            Log.v(TAG, "updateChannel add channel error!");
        }
        return ret;
    }

    private static void showChannelToLauncher(Context context,long channelId){
        //show this channel now!
        TvContractCompat.requestChannelBrowsable(context, channelId);
    }

    @WorkerThread
   public static long createOrUpdateChannel(Context context) {
        long channelId = SharedPreferencesHelper.getChannelId(context);
        if (channelId == -1){
            channelId = createChannel(context);
            SharedPreferencesHelper.setChannelId(context,channelId);
            Log.d(TAG, "createOrUpdateChannel: public once");
            showChannelToLauncher(context,channelId);
        } else {
            Log.d(TAG, "createOrUpdateChannel: public twice");
            showChannelToLauncher(context,channelId);
            updateChannel(context);
        }
        return channelId;
    }

    private static List<DvbTvProgramModel> createProgramsByChannelId(long channelId){
        DvbTvProgramModel usbProgram1 = new DvbTvProgramModel(channelId,0,"program title_1","program_1 description","1","1","1111");

        DvbTvProgramModel usbProgram2 = new DvbTvProgramModel(channelId,0,"program title1_2","program_2 description","2","2","222222");

        List<DvbTvProgramModel> programs = new ArrayList<>();
        programs.add(usbProgram1);
        programs.add(usbProgram2);
        return programs;
    }

    private static DvbTvProgramModel createProgramsByProgramId(long programId){
        DvbTvProgramModel usbProgram = new DvbTvProgramModel(0,
                programId,
                "program update title_"+programId,
                "program update description_"+programId,
                "",
                "",
                ""+programId);
        return usbProgram;
    }

    public static void updatePrograms(Context context,long channelId){
        Log.d(TAG, "getAll NumberOfChannels:"+DvbTVProvider.getNumberOfChannels(context));
        int count = DvbTVProvider.getNumberOfPrograms(context,channelId);
        List<Long> programsIdList  = DvbTVProvider.findDvbProgramsIdListFromProvider(context,channelId);
        if (count > 0){
            Log.d(TAG, "updatePrograms: need to update");
            //TODO
            for (Long id:programsIdList){
                DvbTvProgramModel programModel = createProgramsByProgramId(id);
                DvbTVProvider.updateDvbProgram(context,programModel);
            }
        } else {
            Log.d(TAG, "updatePrograms: need to create");
            List<DvbTvProgramModel> programModelList = createProgramsByChannelId(channelId);
            for (DvbTvProgramModel model:programModelList){
                DvbTVProvider.insertProgram(context,model);
            }
        }
    }



    private static Uri getUSBCardImageFileUri(Context context) {
        String sdPath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(context.getExternalFilesDir(null),"/1.jpg");
        Uri uri = FileProvider.getUriForFile(context, "com.example.tvrecommend.fileprovider", file);
        grantUriPermissionToApp(context,"com.google.android.tvlauncher", uri);
        Log.v(TAG, "uri:" + uri.toString());
        return uri;
    }

    private static Uri getPVRCardImageFileUri(Context context) {
        String sdPath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(context.getExternalFilesDir(null),"/2.jpg");
        Uri uri = FileProvider.getUriForFile(context, "com.example.tvrecommend.fileprovider", file);
        grantUriPermissionToApp(context,"com.google.android.tvlauncher", uri);
        Log.v(TAG, "uri:" + uri.toString());
        return uri;
    }

    private static void grantUriPermissionToApp(Context context,String packageName, Uri uri) {
        context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }


}
