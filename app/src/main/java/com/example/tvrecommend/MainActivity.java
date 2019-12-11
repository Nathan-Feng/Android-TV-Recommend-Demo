package com.example.tvrecommend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.tvprovider.media.tv.TvContractCompat;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tvrecommend.model.DvbChannelModel;
import com.example.tvrecommend.model.DvbProgramModel;
import com.example.tvrecommend.provider.data.VideoContract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "zyf";
    private long mChannelId;
    private DvbChannelModel mChannel;
    private Context mContext;
    private int id = 1;
    private String card0 = "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg";
    private String card1 = "https://storage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/card.jpg";
    private static final int MAKE_BROWSABLE_REQUEST_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        Button button = findViewById(R.id.button_rec);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "click start:");
                // Step1: create channel and program
                initChannel();
                //step2: insert channel and program
                mChannelId = MediaTVProvider.addChannel(MainActivity.this, mChannel);
                //step 3:show this channel now!
                TvContractCompat.requestChannelBrowsable(mContext, mChannelId);

                //step4: update channel
//                mChannelId= DvbChannelProviderUtil.createOrUpdateChannel(mContext);
                //step5: update program
//                DvbChannelProviderUtil.updatePrograms(mContext,mChannelId);
                 //step6: show channel by user
//                promptUserToDisplayChannel(mChannelId);
            }
        });


        Button button_copy = findViewById(R.id.button_copy);
        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "click start:");
                //step1:判断/data/data/package的文件夹下是否有图片
                String path = mContext.getFilesDir().getAbsolutePath() + "/card1.png"; // data/data目录
                File file = new File(path);
                if (!file.exists()) {
                    //step 2:不存在的话，就进行拷贝，应该放在线程中执行。把assets中card文件夹下所有文件拷贝到/data/data/package name/files下面
                    Log.v(TAG, "file not exist start copying:");
                    CopyUtil.copyFolderFromAssets(mContext,"card",getFilesDir().getAbsolutePath());
                }else {
                    Toast.makeText(mContext,"card1.png already exist",Toast.LENGTH_LONG).show();
                }
                Log.v(TAG, "click end:");
            }
        });


        Button button_search = findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "click start:");
                //step1 构建需要插入数据库的数据
                List<ContentValues> contentValuesList = buildMedia();
                Log.v(TAG, "click start:"+contentValuesList.size());
                //转成bulkInsert需要的格式。
                ContentValues[] downloadedVideoContentValues =
                        contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
                //调用接口完成插入数据库的动作。
                getApplicationContext().getContentResolver().bulkInsert(VideoContract.VideoEntry.CONTENT_URI,
                        downloadedVideoContentValues);
                Log.v(TAG, "click end:");
            }
        });
    }

    private void promptUserToDisplayChannel(long channelId) {
        Intent intent = new Intent(TvContractCompat.ACTION_REQUEST_CHANNEL_BROWSABLE);
        intent.putExtra(TvContractCompat.EXTRA_CHANNEL_ID, channelId);
        try {
            this.startActivityForResult(intent, MAKE_BROWSABLE_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Could not start activity: " + intent.getAction(), e);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume :");
        Log.v(TAG, "getFilesDir().getAbsolutePath():"+getFilesDir().getAbsolutePath());
    }

    private void initChannel() {
        Log.v(TAG, "initChannel start  :"+getExternalFilesDir(null));
        Uri oneUri = getOneCardImageFileUri();
//        Uri oneUri = Uri.parse(card0);
//        Uri twoUri = Uri.parse(card1);
        Uri twoUri = getTwoCardImageFileUri();
        //谷歌tv launcher的包名
        grantUriPermissionToApp("com.google.android.tvlauncher", oneUri);
        grantUriPermissionToApp("com.google.android.tvlauncher", twoUri);

        String oneCardImageUrl = oneUri.toString();
        String twoCardImageUrl = twoUri.toString();
        int mediaProgramId = id;
        int contentId = 0;

        DvbProgramModel Program1 = new DvbProgramModel("DisplayName 1", "Description 1", oneCardImageUrl, oneCardImageUrl,
                "category one", Integer.toString(mediaProgramId), Integer.toString(contentId ++));

        DvbProgramModel Program2 = new DvbProgramModel("DisplayName 2", "Description 2", twoCardImageUrl, twoCardImageUrl,
                "category two", Integer.toString(mediaProgramId), Integer.toString(contentId ++));
        List<DvbProgramModel> programs = new ArrayList<>();
        programs.add(Program1);
        programs.add(Program2);

        mChannelId = id++;
        mChannel = new DvbChannelModel("Channel Name", programs, Long.toString(mChannelId));
        Log.v(TAG, "initChannel end:"+getCacheDir());
    }

    private String[] images = {"card1.png", "card2.png", "card3.png",
            "card4.png", "card5.png", "card6.png"};

    private Uri getOneCardImageFileUri() {
        String sdPath = Environment.getExternalStorageDirectory().getPath();
        int index = CopyUtil.makeRadomInt(6);
        Log.v(TAG, "makeRadomInt index:"+index);
        File file = new File( getFilesDir(),images[index]);
        Uri uri = FileProvider.getUriForFile(this, "com.example.tvrecommend.fileprovider", file);
        grantUriPermissionToApp("com.google.android.tvlauncher", uri);
        Log.v(TAG, "uri:" + uri.toString());
        return uri;
    }

    private Uri getTwoCardImageFileUri() {
        String sdPath = Environment.getExternalStorageDirectory().getPath();
        int index = CopyUtil.makeRadomInt(6);
        Log.v(TAG, "makeRadomInt index:"+index);
        File file = new File( getFilesDir(),images[index]);
        Uri uri = FileProvider.getUriForFile(this, "com.example.tvrecommend.fileprovider", file);
        grantUriPermissionToApp("com.google.android.tvlauncher", uri);
        Log.v(TAG, "uri:" + uri.toString());
        return uri;
    }

    private void grantUriPermissionToApp(String packageName, Uri uri) {
        grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Success added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to add", Toast.LENGTH_LONG).show();
        }
    }




    public List<ContentValues> buildMedia(){

        List<ContentValues> videosToInsert = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Log.d(TAG, "buildMedia: %%%%%%%%%%%%% "+j);
                ContentValues videoValues = new ContentValues();
                videoValues.put(VideoContract.VideoEntry.COLUMN_CATEGORY, "category test"+j);
                videoValues.put(VideoContract.VideoEntry.COLUMN_CHANNEL_ID, "2019"+j);
                videoValues.put(VideoContract.VideoEntry.COLUMN_NAME, "0532aaa"+j);
                videoValues.put(VideoContract.VideoEntry.COLUMN_DESC, "1949bbb"+j);
                videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_URL, ""+j);
                videoValues.put(VideoContract.VideoEntry.COLUMN_CARD_IMG, ""+j);
                videoValues.put(VideoContract.VideoEntry.COLUMN_BG_IMAGE_URL, ""+j);
//                if (j==0){
//                    videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_URL, sources0);
//                    videoValues.put(VideoContract.VideoEntry.COLUMN_CARD_IMG, card0);
//                    videoValues.put(VideoContract.VideoEntry.COLUMN_BG_IMAGE_URL, bg0);
//                } else {
//                    videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_URL, sources1);
//                    videoValues.put(VideoContract.VideoEntry.COLUMN_CARD_IMG, card1);
//                    videoValues.put(VideoContract.VideoEntry.COLUMN_BG_IMAGE_URL, bg1);
//                }
                videoValues.put(VideoContract.VideoEntry.COLUMN_STUDIO, "studio");

                // Fixed defaults.
                videoValues.put(VideoContract.VideoEntry.COLUMN_CONTENT_TYPE, "video/mp4");
                videoValues.put(VideoContract.VideoEntry.COLUMN_IS_LIVE, false);
                videoValues.put(VideoContract.VideoEntry.COLUMN_AUDIO_CHANNEL_CONFIG, "2.0");
                videoValues.put(VideoContract.VideoEntry.COLUMN_PRODUCTION_YEAR, 2014);
                videoValues.put(VideoContract.VideoEntry.COLUMN_DURATION, 0);
                videoValues.put(VideoContract.VideoEntry.COLUMN_RATING_STYLE,
                        Rating.RATING_5_STARS);
                videoValues.put(VideoContract.VideoEntry.COLUMN_RATING_SCORE, 3.5f);
                if (mContext != null) {
                    videoValues.put(VideoContract.VideoEntry.COLUMN_PURCHASE_PRICE,
                            mContext.getResources().getString(R.string.buy_2));
                    videoValues.put(VideoContract.VideoEntry.COLUMN_RENTAL_PRICE,
                            mContext.getResources().getString(R.string.rent_2));
                    videoValues.put(VideoContract.VideoEntry.COLUMN_ACTION,
                            mContext.getResources().getString(R.string.global_search));
                }

                // TODO: Get these dimensions.
                videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_WIDTH, 1280);
                videoValues.put(VideoContract.VideoEntry.COLUMN_VIDEO_HEIGHT, 720);

                videosToInsert.add(videoValues);
            }
        return videosToInsert;
    }


}
