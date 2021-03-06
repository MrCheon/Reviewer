package com.drw_eng.eng_reviewer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class SplashActivity extends Activity {
    private static final String TAG = "permission";
    private int SPLASH_DISPLAY_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.R.style.Theme_NoTitleBar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.acivity_splash);

        boolean isGrantStorage = grantExternalStoragePermission();
        Handler handler = new Handler();

        if(!isGrantStorage){
            SPLASH_DISPLAY_TIME = 120000;
            handler.postDelayed(new Runnable(){
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run(){
                    moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                    finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }, SPLASH_DISPLAY_TIME);
        }
        else {
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    startActivity(new Intent(getApplication(), MainActivity.class));
                    /* 스플래시 액티비티를 스택에서 제거. */
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_TIME);
        }
    }
    @Override
    public void onBackPressed() {
        /* 스플래시 화면에서 뒤로가기 기능 제거. */
    }
    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            }else{
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }else{
            Toast.makeText(this, "External Storage Permission is Grant", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "External Storage Permission is Grant ");
            return true;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
                startActivity(new Intent(getApplication(), MainActivity.class));
                /* 스플래시 액티비티를 스택에서 제거. */
                SplashActivity.this.finish();
            }
            if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                /* 앱 종료 */
                moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }
}


