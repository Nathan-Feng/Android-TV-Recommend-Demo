package com.example.tvrecommend.util;

import android.net.Uri;
import android.util.Log;

import java.util.List;

/** Builds and parses uris for deep linking within the app. */
public class AppLinkHelper {

    private static final int URI_INDEX_OPTION = 0;
    private static final int URI_INDEX_CHANNEL = 1;
    private static final int URI_INDEX_MOVIE = 2;
    private static final int URI_INDEX_POSITION = 3;

    public static boolean isLivePlayUri(Uri uri) {
        if (uri.getPathSegments().isEmpty()) {
            return false;
        }
        String option0 = uri.getPathSegments().get(URI_INDEX_OPTION);
        String option1 = uri.getPathSegments().get(URI_INDEX_CHANNEL);
//        String option2 = uri.getPathSegments().get(URI_INDEX_MOVIE);
//        String option3 = uri.getPathSegments().get(URI_INDEX_POSITION);
        Log.d("zyf", "isPlaybackUri0: "+option0);
        Log.d("zyf", "isPlaybackUri1: "+option1);
        Log.d("zyf", "isPlaybackUri1-1: "+option1.replace("-","/"));
//        Log.d("zyf", "isPlaybackUri2: "+option2);
//        Log.d("zyf", "isPlaybackUri3: "+option3);
        return DvbTVProvider.LIVE_PLAY_ACTION_PATH.equals(option0);
    }


    public static boolean isFilePlayUri(Uri uri) {
        if (uri.getPathSegments().isEmpty()) {
            return false;
        }
        String option = uri.getPathSegments().get(URI_INDEX_OPTION);
        return DvbTVProvider.FILE_PLAY_ACTION_PATH.equals(option);
    }

    public static boolean isStartAppUri(Uri uri) {
        if (uri.getPathSegments().isEmpty()) {
            return false;
        }
        String option = uri.getPathSegments().get(URI_INDEX_OPTION);
        return DvbTVProvider.START_APP_ACTION_PATH.equals(option);
    }

    /**
     * Extracts the subscription name from the {@link Uri}.
     *
     * @param uri that contains a subscription name.
     * @return the subscription name.
     */
    private static String extractSubscriptionName(Uri uri) {
        return extract(uri, URI_INDEX_CHANNEL);
    }

    /**
     * Extracts the channel id from the {@link Uri}.
     *
     * @param uri that contains a channel id.
     * @return the channel id.
     */
    private static long extractChannelId(Uri uri) {
        return extractLong(uri, URI_INDEX_CHANNEL);
    }

    /**
     * Extracts the movie id from the {@link Uri}.
     *
     * @param uri that contains a movie id.
     * @return the movie id.
     */
    private static long extractMovieId(Uri uri) {
        return extractLong(uri, URI_INDEX_MOVIE);
    }

    /**
     * Extracts the playback mPosition from the {@link Uri}.
     *
     * @param uri that contains a playback mPosition.
     * @return the playback mPosition.
     */
    private static long extractPosition(Uri uri) {
        return extractLong(uri, URI_INDEX_POSITION);
    }

    private static long extractLong(Uri uri, int index) {
        return Long.valueOf(extract(uri, index));
    }

    private static String extract(Uri uri, int index) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.isEmpty() || pathSegments.size() < index) {
            return null;
        }
        return pathSegments.get(index);
    }
}
