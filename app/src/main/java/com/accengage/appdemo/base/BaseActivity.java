package com.accengage.appdemo.base;

import android.content.Intent;

import com.ad4screen.sdk.A4S;
import com.ad4screen.sdk.activities.A4SActivity;

/**
 * Created by acadet on 22/03/2018.
 */

public abstract class BaseActivity extends A4SActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        A4S.get(this).setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        A4S.get(this).startActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        A4S.get(this).stopActivity(this);
    }

}
