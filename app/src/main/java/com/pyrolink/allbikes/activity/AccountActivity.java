package com.pyrolink.allbikes.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.pyrolink.allbikes.R;
import com.pyrolink.allbikes.databinding.ActivityAccountBinding;
import com.pyrolink.allbikes.utils.ActivityUtils;

public class AccountActivity extends AppCompatActivity
{
    private ActivityAccountBinding _binding;

    public enum Mode
    {
        Login(1),
        Create(2),

        Error(999);

        public static String KEY = "AccountActivity";

        public static Mode fromInt(int i)
        {
            if (i == Login.id)
                return Login;
            if (i == Create.id)
                return Create;

            return Error;
        }

        public int id;

        Mode(int id) { this.id = id; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        update(Mode.fromInt(getIntent().getExtras().getInt(Mode.KEY)));
    }

    private void update(Mode mode)
    {
        int title = 999;
        int pwdForbiddenVisibility = 999;
        int mainButton = 999;
        int msg = 999;
        int msgButton = 999;
        Mode nextMode = Mode.Error;
        Class<? extends AppCompatActivity> nextActivity = null;
        ActivityUtils.FinishMode finishMode = null;

        switch (mode)
        {
            case Login:
                title = R.string.login;
                pwdForbiddenVisibility = View.VISIBLE;
                mainButton = R.string.login;
                msg = R.string.noAccount;
                msgButton = R.string.createOne;
                nextMode = Mode.Create;
                nextActivity = MapActivity.class;
                finishMode = ActivityUtils.FinishMode.All;
                break;
            case Create:
                title = R.string.createAccount;
                pwdForbiddenVisibility = View.INVISIBLE;
                mainButton = R.string.createAccount;
                msg = R.string.account;
                msgButton = R.string.goLogin;
                nextMode = Mode.Login;
                nextActivity = FinishAccountActivity.class;
                finishMode = ActivityUtils.FinishMode.None;
                break;
        }

        _binding.title.setText(title);
        _binding.pwdForbidden.setVisibility(pwdForbiddenVisibility);
        _binding.mainButton.setText(mainButton);
        _binding.msg.setText(msg);
        _binding.msgButton.setText(msgButton);
        final Mode finalNextMode = nextMode;
        _binding.msgButton.setOnClickListener(v -> update(finalNextMode));

        final Class<? extends AppCompatActivity> finalNextActivity = nextActivity;
        final ActivityUtils.FinishMode finalFinishMode = finishMode;
        _binding.mainButton.setOnClickListener(
                b -> ActivityUtils.launchActivity(AccountActivity.this, finalNextActivity, finalFinishMode));
    }
}