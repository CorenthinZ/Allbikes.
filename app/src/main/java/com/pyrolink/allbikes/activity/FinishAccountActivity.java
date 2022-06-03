package com.pyrolink.allbikes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.DatePicker;

import com.pyrolink.allbikes.R;
import com.pyrolink.allbikes.databinding.ActivityFinishAccountBinding;
import com.pyrolink.allbikes.utils.ActivityUtils;

public class FinishAccountActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityFinishAccountBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_finish_account);

        DatePickerDialog dpd = new DatePickerDialog(FinishAccountActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        dpd.setOnDateSetListener((datePicker, year, mounth, day) -> binding.birthdate.setText(
                getString(R.string.date, day, mounth + 1, year)));

        binding.birthdate.setOnClickListener(view -> dpd.show());

        binding.mainButton.setOnClickListener(
                view -> ActivityUtils.launchActivity(FinishAccountActivity.this, MapActivity.class,
                        ActivityUtils.FinishMode.All));
    }
}