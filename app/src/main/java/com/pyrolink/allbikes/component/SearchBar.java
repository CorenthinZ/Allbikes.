package com.pyrolink.allbikes.component;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.pyrolink.allbikes.interfaces.Callback;
import com.pyrolink.allbikes.R;

public class SearchBar extends androidx.appcompat.widget.AppCompatEditText
{
    public SearchBar(Context context)
    {
        super(context);
        init(context);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public void init(Context context)
    {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
                ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_search, null), null, null, null);

        setGravity(Gravity.CENTER);
        setInputType(InputType.TYPE_CLASS_TEXT);
        setHint(context.getString(R.string.searchBarHint));

        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border_radius, context.getTheme()));
    }

    private TextWatcher _textWatcher;

    public void setOnSearch(Callback<String> onSearch)
    {
        if (_textWatcher != null)
            removeTextChangedListener(_textWatcher);

        _textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                onSearch.call(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        };

        addTextChangedListener(_textWatcher);
    }
}
