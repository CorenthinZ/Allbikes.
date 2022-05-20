package com.pyrolink.allbikes.model;

import androidx.annotation.NonNull;

import com.pyrolink.allbikes.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;

public enum Accessibility
{
    Restaurant,
    Fontaine,
    Riviere,
    Toilettes;

    private static final Map<Accessibility, String> strings = new HashMap<Accessibility, String>()
    {{
        put(Restaurant, "Restaurant");
        put(Fontaine, "Fontaine");
        put(Riviere, "Rivière");
        put(Toilettes, "Toilettes publiques");
    }};


    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public String toString() { return strings.getOrDefault(this, "None"); }

    public static Accessibility get(String value) { return MapUtils.getKey(strings, value); }
}