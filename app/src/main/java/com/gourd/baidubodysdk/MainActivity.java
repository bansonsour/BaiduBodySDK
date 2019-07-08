package com.gourd.baidubodysdk;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.DocumentsContract.Root;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.facemerge.util.OpenCV_33_FaceSwap;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private static final long DEFAULT_DELAY_TIME = 1000L;// 1秒
    private static final int PERMISSIONS_REQUEST_CODE = 1234;

    private TextView mIv1;
    private TextView mIv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        mIv1 = findViewById(R.id.simple);
        mIv2 = findViewById(R.id.facemerge);
        mIv1.setOnClickListener(this);
        mIv2.setOnClickListener(this);
        Sample.init();
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
            Thread thread = new Thread(new Runnable(){

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
        } else if (v == mIv2) {
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        String src1 = Sample.Root + "FaceSrc1.jpg";
                        String src2 =  Sample.Root + "FaceSrc2.jpg";
                        src1 =  Sample.Root + "IMG1.jpg";
                        src2 =  Sample.Root + "ttpt1.jpg";
                        String output =  Sample.Root + "FaceMerge2.jpg";
                        OpenCV_33_FaceSwap.faceMerge(src1,src2,output);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

    }
}
