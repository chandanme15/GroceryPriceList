package com.project.commoditiesCurrentPrice.userInterface.activity;

import android.os.Bundle;

import com.project.commoditiesCurrentPrice.R;
import com.project.commoditiesCurrentPrice.userInterface.base.BaseActivity;
import com.project.commoditiesCurrentPrice.userInterface.fragment.MainFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.sample_content_fragment, MainFragment.getInstance()).commit();
    }
}