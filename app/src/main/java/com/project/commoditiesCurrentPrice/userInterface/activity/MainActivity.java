package com.project.commoditiesCurrentPrice.userInterface.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.project.commoditiesCurrentPrice.R;
import com.project.commoditiesCurrentPrice.userInterface.base.BaseActivity;
import com.project.commoditiesCurrentPrice.userInterface.fragment.MainFragment;
import com.project.commoditiesCurrentPrice.utils.Util;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        if (isStoragePermissionGranted()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, MainFragment.getInstance()).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getSupportFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, MainFragment.getInstance()).commit();
        } else {
            finish();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}