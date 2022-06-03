package com.pyrolink.allbikes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.pyrolink.allbikes.R;
import com.pyrolink.allbikes.databinding.ActivityMainBinding;
import com.pyrolink.allbikes.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        _binding.login.setOnClickListener(this::onClickListener);
        _binding.createAccount.setOnClickListener(this::onClickListener);
    }

    private void onClickListener(View v)
    {
        Bundle extras = new Bundle();
        int id = AccountActivity.Mode.Error.id;
        if (v == _binding.login)
            id = AccountActivity.Mode.Login.id;
        else if (v == _binding.createAccount)
            id = AccountActivity.Mode.Create.id;
        extras.putInt(AccountActivity.Mode.KEY, id);
        ActivityUtils.launchActivity(this, AccountActivity.class, ActivityUtils.FinishMode.None, extras);
    }
}