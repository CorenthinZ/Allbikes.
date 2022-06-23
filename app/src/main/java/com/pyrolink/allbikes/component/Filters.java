package com.pyrolink.allbikes.component;

// region Imports

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;

import com.pyrolink.allbikes.interfaces.Callback;
import com.pyrolink.allbikes.interfaces.Callback2;
import com.pyrolink.allbikes.R;
import com.pyrolink.allbikes.model.Accessibility;

// endregion

public class Filters extends LinearLayout
{
    // region Variables

    private TextView _title;
    private Stars _stars;
    private SeekBar _distance;
    private GridLayout _accessibility;

    // endregion

    // region Constructors

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

    // endregion

    private void initControl(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.filters, this);

        _title = findViewById(R.id.filterTitle);
        _stars = findViewById(R.id.stars);
        _distance = findViewById(R.id.distance);

        _accessibility = findViewById(R.id.accessibility);

        // region Accessibility ToggleButtons

        for (Accessibility accessibility : Accessibility.class.getEnumConstants())
        {
            ToggleButton tb = new ToggleButton(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            tb.setLayoutParams(params);
            tb.setText(accessibility.toString());
            tb.setTextOn(accessibility.toString());
            tb.setTextOff(accessibility.toString());
            tb.setChecked(true);
            _accessibility.addView(tb);
        }

        // endregion
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

            // region Empty Overrides

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }

            // endregion
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

    public void setOnAccessibility(Callback2<Accessibility, Boolean> onAccessibility)
    {
        for (int i = 0; i < _accessibility.getChildCount(); i++)
        {
            ToggleButton tb = (ToggleButton) _accessibility.getChildAt(i);

            tb.setOnCheckedChangeListener(
                    (button, checked) -> onAccessibility.call(Accessibility.get(tb.getText().toString()), checked));
        }
    }
}
