package com.pyrolink.allbikes.utils;

import java.util.Map;

public final class MapUtils
{
    private MapUtils() { }

    public <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Map.Entry<K, V> entry : map.entrySet())
            if (entry.getValue().equals(value))
                return entry.getKey();

        return null;
    }
}
