package com.gourd.baidubodysdk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facemerge.util.OpenCV_33_FaceSwap;
import com.facemerge.util.OpenCV_33_FaceSwap.MergeCallback;
import com.gourd.baidubodysdk.util.FileUtil;
import com.gourd.baidubodysdk.util.ImageSelectorLoader;
import com.gourd.baidubodysdk.util.UrlStringUtils;
import com.yy.bimodule.resourceselector.resource.ResourceSelectorAPI;
import com.yy.bimodule.resourceselector.resource.filter.FileTypeDisplayFilter;
import com.yy.bimodule.resourceselector.resource.filter.Op;
import com.yy.bimodule.resourceselector.resource.loader.LocalResource;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private static final long DEFAULT_DELAY_TIME = 1000L;// 1秒
    private static final int PERMISSIONS_REQUEST_CODE = 1234;
    private static final int REQ_CODE_SELECT_IMG = 10000;
    private static final int REQ_CODE_SELECT_MERGE_IMG = 10010;
    private static final int REQ_CODE_SELECT_TEMP_IMG = 10011;
    private static final int REQ_CODE_CROP_IMG = 10012;

    private TextView mTv1;
    private TextView mTv2;

    private SimpleDraweeView mIv1;
    private SimpleDraweeView mIv2;
    private SimpleDraweeView mResultIv;
    private ProgressBar mProgressBar;


    private String mCropFilePath; //裁剪临时文件
    private String mSelectFilePath;
    private String mSelectTempPath;
    private String mOutputPath;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else if (msg.what == 1) {
                mProgressBar.setVisibility(View.GONE);
                mResultIv.setImageURI("");
                mResultIv.setImageURI(Uri.fromFile(new File((String) msg.obj)));
                Toast.makeText(MainActivity.this, "Merge is success!!", Toast.LENGTH_SHORT).show();

            } else if (msg.what == 2) {
                mSelectTempPath = Sample.Root + "ttpt1.jpg";
                mIv2.setImageURI(Uri.fromFile(new File(mSelectTempPath)));
            } else if (msg.what == -1){
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        mTv1 = findViewById(R.id.simple);
        mTv2 = findViewById(R.id.facemerge);
        mIv1 = findViewById(R.id.img1);
        mIv2 = findViewById(R.id.img2);
        mResultIv = findViewById(R.id.resultImg);
        mProgressBar = findViewById(R.id.loading_pb);
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mIv1.setOnClickListener(this);
        mIv2.setOnClickListener(this);

        copyAsset();


    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> permissions = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permissions.size() > 0) {
                String[] permissionArray = new String[permissions.size()];
                for (int i = 0; i < permissions.size(); i++) {
                    permissionArray[i] = permissions.get(i);
                }
                ActivityCompat.requestPermissions(this, permissionArray, PERMISSIONS_REQUEST_CODE);
            } else {

            }
        } else {

        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (hasAllPermissionsGranted(grantResults)) {
            } else {
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mIv1) {
            selectImage();
        } else if (v == mIv2) {
            selectTempImage();
        } else if (v == mTv2) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String src1 = Sample.Root + "FaceSrc1.jpg";
                        String src2 = Sample.Root + "FaceSrc2.jpg";
                        src1 = Sample.Root + "IMG1.png";
                        src2 = Sample.Root + "ttpt1.jpg";
                        //final String output = Sample.Root + "FaceMerge2.jpg";

                        if (!TextUtils.isEmpty(mSelectFilePath)) {
                            src1 = mSelectFilePath;
                        }

                        if (!TextUtils.isEmpty(mSelectTempPath)) {
                            src2 = mSelectTempPath;
                        }

                        mOutputPath = Sample.Root
                                + "Merge_"
                                + UrlStringUtils.getNameFromUrl(mSelectTempPath)
                                + "_"
                                + UrlStringUtils.getFullNameFromUrl(mSelectFilePath);
                        if (!TextUtils.isEmpty(mOutputPath)) {
                            File outFile = new File(mOutputPath);
                            if (outFile != null && outFile.exists()) {
                                outFile.delete();
                            }
                        }

                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                        System.out.println("src1-->" + src1);
                        System.out.println("src2-->" + src2);
                        OpenCV_33_FaceSwap.faceMerge(src1, src2, mOutputPath, new MergeCallback() {
                            @Override
                            public void onMergeCallback(boolean status, String msgs) {
                                if (status) {
                                    System.out.println("output-->" + mOutputPath);
                                    Message msg = mHandler.obtainMessage();
                                    msg.obj = mOutputPath;
                                    msg.what = 1;
                                    mHandler.sendMessageDelayed(msg, 1000);
                                } else {
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = -1;
                                    msg.obj = msgs;
                                    mHandler.sendMessageDelayed(msg, 1000);
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else if (v == mIv1) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Sample.sample();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

    }


    /**
     * 选择图片
     */
    private void selectImage() {
        ResourceSelectorAPI.with(this)
                .setImageLoader(ImageSelectorLoader.class)
                .setType(ResourceSelectorAPI.TYPE_IMAGE)
                .setMultiSelect(false)
                .setRequestCode(REQ_CODE_SELECT_MERGE_IMG)
                .setDisplayFilters(new FileTypeDisplayFilter(Op.DIS_MATCH, "gif"))//不显示gif
                .open();
    }

    private void selectTempImage() {
        ResourceSelectorAPI.with(this)
                .setImageLoader(ImageSelectorLoader.class)
                .setType(ResourceSelectorAPI.TYPE_IMAGE)
                .setMultiSelect(false)
                .setRequestCode(REQ_CODE_SELECT_TEMP_IMG)
                .setDisplayFilters(new FileTypeDisplayFilter(Op.DIS_MATCH, "gif"))//不显示gif
                .open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_MERGE_IMG) {
            //选择图片
            List<LocalResource> results = ResourceSelectorAPI.resolveActivityResult(resultCode, data);
            if (results != null && results.size() > 0) {
                try {
                    Compressor compressedImage = new Compressor(this)
                            .setMaxWidth(720)
                            .setMaxHeight(480)
                            .setQuality(100)
                            //.setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Sample.TempPath);

                    if (results.get(0).path.contains("png") || results.get(0).path.contains(".PNG")) {
                        compressedImage .setCompressFormat(CompressFormat.PNG);
                    } else {
                        compressedImage.setCompressFormat(CompressFormat.JPEG);
                    }
                    File compFile = compressedImage.compressToFile(new File(results.get(0).path));
                    mSelectFilePath = compFile.getAbsolutePath();
                    mIv1.setImageURI(Uri.fromFile(compFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }  else if (requestCode == REQ_CODE_SELECT_TEMP_IMG) {
            //选择图片
            List<LocalResource> results = ResourceSelectorAPI.resolveActivityResult(resultCode, data);
            if (results != null && results.size() > 0) {
                mSelectTempPath = results.get(0).path;
                mIv2.setImageURI(Uri.fromFile(new File(results.get(0).path)));
            }
        }
        else if (requestCode == REQ_CODE_CROP_IMG && resultCode == Activity.RESULT_OK) {
            //裁剪结果
            if (!TextUtils.isEmpty(mCropFilePath) && new File(mCropFilePath).exists()) {
            } else {
                Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyAsset() {
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    File file = new File(Sample.Root, "ttpt1.jpg");
                    if (file != null && file.exists()) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = (Sample.Root + "ttpt1.jpg");
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                        return;
                    }
                    FileUtil.getFileFromAsset(MainActivity.this, "ttpt1.jpg", Sample.Root);
                    FileUtil.getFileFromAsset(MainActivity.this, "ttpt2.jpg", Sample.Root);
                    Message msg = mHandler.obtainMessage();
                    msg.obj = (Sample.Root + "ttpt1.jpg");
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}
