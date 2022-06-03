package com.pyrolink.allbikes.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.icu.number.NumberFormatter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.pyrolink.allbikes.R;

public class ReturnButton extends AppCompatButton
{
    public ReturnButton(@NonNull Context context)
    {
        super(context);
        init(context);
    }

    public ReturnButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        if (context instanceof Activity)
            setOnClickListener(b -> ((Activity) context).finish());

        setBackgroundColor(context.getColor(android.R.color.transparent));
        setPadding(0, 0, 0, 0);
        setGravity(Gravity.CENTER);
        setIncludeFontPadding(false);
        setText(context.getString(R.string.returnChar));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
    }
}
