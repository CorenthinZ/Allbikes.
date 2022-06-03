package com.pyrolink.allbikes.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pyrolink.allbikes.R;

public final class ActivityUtils
{
    public enum FinishMode
    {
        None,
        This,
        All;

        private void finish(AppCompatActivity activity)
        {
            switch (this)
            {
                case This:
                    activity.finish();
                    break;
                case All:
                    activity.finishAffinity();
                    break;
            }
        }
    }

    private ActivityUtils() { }

    public static void launchActivity(@NonNull AppCompatActivity currentActivity,
                                      @NonNull Class<? extends AppCompatActivity> nextActivity, FinishMode finishMode,
                                      Bundle extras)
    {
        Intent intent = new Intent(currentActivity, nextActivity);
        intent.putExtras(extras);

        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        finishMode.finish(currentActivity);
    }

    public static void launchActivity(@NonNull AppCompatActivity currentActivity,
                                      @NonNull Class<? extends AppCompatActivity> nextActivity)
    { launchActivity(currentActivity, nextActivity, FinishMode.None); }

    public static void launchActivity(@NonNull AppCompatActivity currentActivity,
                                      @NonNull Class<? extends AppCompatActivity> nextActivity, FinishMode finishMode)
    {
        currentActivity.startActivity(new Intent(currentActivity, nextActivity));
        currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        finishMode.finish(currentActivity);
    }
}
