package com.pyrolink.allbikes.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pyrolink.allbikes.Callback;
import com.pyrolink.allbikes.R;

public class Filters extends LinearLayout
{
    private TextView _title;
    private Stars _stars;
    private SeekBar _distance;

    public Filters(Context context)
    {
        super(context);
        initControl(context);
    }

    public Filters(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context);
    }

    private void initControl(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.filters, this);

        _title = findViewById(R.id.filterTitle);
        _stars = findViewById(R.id.stars);
        _distance = findViewById(R.id.distance);
    }

    public void setOnTitleClick(OnClickListener onClick) { _title.setOnClickListener(onClick); }

    public void setOnDistance(Callback<Integer> onProgress)
    {
        _distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (!fromUser)
                    return;

                onProgress.call(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void setOnNote(Callback<Integer> onNote)
    {
        _stars.setOnStarClick(index ->
        {
            _stars.setStars(index + 1);
            onNote.call(index);
        });
    }
}
