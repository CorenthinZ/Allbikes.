package com.pyrolink.allbikes.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.pyrolink.allbikes.R;

public class Stars extends LinearLayout
{
    private LinearLayout _layout;

    public Stars(Context context)
    {
        super(context);
        initControl(context);
    }

    public Stars(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context);
    }

    private void initControl(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.stars, this);

        _layout = (LinearLayout) getChildAt(0);
    }

    public void setStar(int index, boolean active)
    {
        ((ImageView) _layout.getChildAt(index)).setImageResource(
                active ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }
}
