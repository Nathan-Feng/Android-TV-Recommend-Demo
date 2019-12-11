package com.example.tvrecommend.util;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.DrawableRes;
import androidx.annotation.WorkerThread;
import androidx.tvprovider.media.tv.Channel;
import androidx.tvprovider.media.tv.ChannelLogoUtils;
import androidx.tvprovider.media.tv.PreviewProgram;
import androidx.tvprovider.media.tv.TvContractCompat;
import com.example.tvrecommend.R;
import com.example.tvrecommend.model.DvbTvChannelModel;
import com.example.tvrecommend.model.DvbTvProgramModel;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhaoyufeng3
 */
public class DvbTVProvider {

    private static final String TAG = "zyfMediaTVProvider";

    private static final String SCHEME = "hisensedvb";
    private static final String APPS_LAUNCH_HOST = "com.hisense.androidtv.recommend";
    private static final String SCHEMA_URI_PREFIX = "hisensedvb://com.hisense.androidtv.recommend/";
    public static final String FILE_PLAY_ACTION_PATH = "FilePlay";
    public static final String LIVE_PLAY_ACTION_PATH = "LivePlay";
    public static final String START_APP_ACTION_PATH = "StartApp";
    public static final String URI_LIVE_PLAY = SCHEMA_URI_PREFIX + LIVE_PLAY_ACTION_PATH;
    public static final String URI_FILE_PLAY = SCHEMA_URI_PREFIX + FILE_PLAY_ACTION_PATH;

    private static final String[] CHANNELS_PROJECTION = {
            TvContractCompat.Channels._ID,
            TvContractCompat.Channels.COLUMN_DISPLAY_NAME,
            TvContractCompat.Channels.COLUMN_BROWSABLE
    };

    private static final Uri PREVIEW_PROGRAMS_CONTENT_URI =
            Uri.parse("content://android.media.tv/preview_program");

    static private String createInputId(Context context) {
        ComponentName cName = new ComponentName(context, DvbTVProvider.class);
        return TvContractCompat.buildInputId(cName);
    }

    @WorkerThread
    static long updateChannel(Context context, DvbTvChannelModel mediaChannel) {
        String channelInputId = createInputId(context);
        Channel channel = new Channel.Builder()
                .setDisplayName(mediaChannel.getDisplayName())
                .setDescription(mediaChannel.getDescription())
                .setType(TvContractCompat.Channels.TYPE_PREVIEW)
                .setInputId(channelInputId)
                .setAppLinkIntentUri(Uri.parse(SCHEME + "://" + APPS_LAUNCH_HOST
                        + "/" + START_APP_ACTION_PATH))
                .setInternalProviderId("3333")
                .setAppLinkColor(context.getResources().getColor(R.color.colorAccent))
                .build();

        int ret = context.getContentResolver().update(TvContractCompat.buildChannelUri(mediaChannel.getChannelId()),
                channel.toContentValues(),null,null);
        return ret;
    }

    @WorkerThread
    static long addChannel(Context context, DvbTvChannelModel mediaChannel) {
        String channelInputId = createInputId(context);
        Channel channel = new Channel.Builder()
                .setDisplayName(mediaChannel.getDisplayName())
                .setDescription(mediaChannel.getDescription())
                .setType(TvContractCompat.Channels.TYPE_PREVIEW)
                .setInputId(channelInputId)
                .setAppLinkIntentUri(Uri.parse(SCHEME + "://" + APPS_LAUNCH_HOST
                        + "/" + START_APP_ACTION_PATH))
                .setInternalProviderId("2222")
                .setAppLinkColor(context.getResources().getColor(R.color.colorAccent))
                .build();

        Uri channelUri = context.getContentResolver().insert(TvContractCompat.Channels.CONTENT_URI,
                channel.toContentValues());
        if (channelUri == null || channelUri.equals(Uri.EMPTY)) {
            Log.e(TAG, "addChannel Insert channel failed");
            return 0;
        }
        long channelId = ContentUris.parseId(channelUri);
        mediaChannel.setChannelId(channelId);

        writeChannelLogo(context, channelId, R.drawable.ic_launcher);

        return channelId;
    }

    @WorkerThread
    static void deleteChannel(Context context, long channelId) {
        int rowsDeleted = context.getContentResolver().delete(
                TvContractCompat.buildChannelUri(channelId), null, null);
        if (rowsDeleted < 1) {
            Log.e(TAG, "Delete channel failed");
        }
    }


    /**
     * Writes a drawable as the channel logo.
     *
     * @param channelId  identifies the channel to write the logo.
     * @param drawableId resource to write as the channel logo. This must be a bitmap and not, say
     *                   a vector drawable.
     */
    @WorkerThread
    static private void writeChannelLogo(Context context, long channelId,
                                         @DrawableRes int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ChannelLogoUtils.storeChannelLogo(context, channelId, bitmap);
    }



    @WorkerThread
    public static void deleteProgram(Context context, DvbTvProgramModel program) {
        deleteProgram(context, program.getProgramId());
    }

    @WorkerThread
    static void deleteProgram(Context context, long programId) {
        int rowsDeleted = context.getContentResolver().delete(
                TvContractCompat.buildPreviewProgramUri(programId), null, null);
        if (rowsDeleted < 1) {
            Log.e(TAG, "Delete program failed");
        }
    }

    @WorkerThread
    static void updateDvbProgram(Context context, DvbTvProgramModel mediaProgram) {
        long programId = mediaProgram.getProgramId();
        Uri programUri = TvContractCompat.buildPreviewProgramUri(programId);
        Cursor cursor = context.getContentResolver().query(programUri, null, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()){
            PreviewProgram porgram = PreviewProgram.fromCursor(cursor);
            PreviewProgram.Builder builder = new PreviewProgram.Builder(porgram)
                    .setTitle(mediaProgram.getTitle());

            int rowsUpdated = context.getContentResolver().update(programUri,
                    builder.build().toContentValues(), null, null);
            if (rowsUpdated < 1) {
                Log.e(TAG, "Update program failed");
            }
        }
    }

    static void insertProgram(Context context, DvbTvProgramModel mediaProgram) {
        final String mediaProgramId = mediaProgram.getContentId()+"";

        PreviewProgram program = new PreviewProgram.Builder()
                .setChannelId(mediaProgram.getChannelId())
                .setTitle(mediaProgram.getTitle())
                .setDescription(mediaProgram.getDescription())
                .setPosterArtUri(Uri.parse(mediaProgram.getCardImageUrl()))
                .setIntentUri(Uri.parse(URI_LIVE_PLAY + "/" + "data-dtvdata-HSPVR-test.ts"))
                .setInternalProviderId(mediaProgramId)
                .setType(TvContractCompat.PreviewPrograms.TYPE_MOVIE)
                .build();

        Uri programUri = context.getContentResolver().insert(PREVIEW_PROGRAMS_CONTENT_URI,
                program.toContentValues());
        if (programUri == null || programUri.equals(Uri.EMPTY)) {
            Log.e(TAG, "Insert program failed");
            return;
        }
        mediaProgram.setProgramId(ContentUris.parseId(programUri));
    }



    public static int getNumberOfChannels(Context context) {
        int count = 0;
        Cursor cursor =
                context.getContentResolver()
                        .query(
                                TvContractCompat.Channels.CONTENT_URI,
                                CHANNELS_PROJECTION,
                                null,
                                null,
                                null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Channel channel = Channel.fromCursor(cursor);
                Log.d(TAG, "getNumberOfChannels: getId:"+channel.getId());

            } while (cursor.moveToNext());
        }
        if (cursor != null){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public static int getNumberOfChannels(Context context,long channelId) {
        int count = 0;
        Uri channelUri = TvContractCompat.buildChannelUri(channelId);
        Cursor cursor =
                context.getContentResolver()
                        .query(
                                channelUri,
                                CHANNELS_PROJECTION,
                                null,
                                null,
                                null);
        if (cursor != null){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public static int getNumberOfPrograms(Context context,long channelId) {
        int count = 0;
        Uri programUri = TvContractCompat.buildPreviewProgramsUriForChannel(channelId);
        Cursor cursor = context.getContentResolver().query(
                programUri,TV_PROGRAMS_MAP_PROJECTION,
                null,
                null,
                null);
        if (cursor != null){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public static List<Long> findDvbProgramsIdListFromProvider(Context context, long channelId){
        List<Long> programIdList = new ArrayList<>();
        Cursor cursor = null;
        try{
            Uri programUri = TvContractCompat.buildPreviewProgramsUriForChannel(channelId);
            cursor = context.getContentResolver().query(
                    programUri,TV_PROGRAMS_MAP_PROJECTION,null,
                    null,null);
            if (cursor != null){
                int count = cursor.getCount();
                Log.d(TAG, "findDvbProgramsFromProvider:count: "+count);
            }
            while (cursor !=null && cursor.moveToNext()){
                Log.d(TAG, "findDvbProgramsFromProvider:id: "+cursor.getLong(0));
                Log.d(TAG, "findDvbProgramsFromProvider:channelId: "+cursor.getLong(1));
                Log.d(TAG, "findDvbProgramsFromProvider:title: "+cursor.getString(2));
                programIdList.add(cursor.getLong(0));
            }
        } catch (Exception e){
            Log.e(TAG, "fail", e);
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return programIdList;
    }

    private static final String[]  TV_PROGRAMS_MAP_PROJECTION = {
            TvContractCompat.PreviewPrograms._ID,
            TvContractCompat.PreviewPrograms.COLUMN_CHANNEL_ID,
            TvContractCompat.PreviewPrograms.COLUMN_TITLE};

}