package com.pyrolink.allbikes.utils;

import java.util.HashMap;
import java.util.Map;

public final class MapUtils
{
    private MapUtils() { }

    public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Map.Entry<K, V> entry : map.entrySet())
            if (entry.getValue().equals(value))
                return entry.getKey();

        return null;
    }

    public static <K, V> Map<V, K> getInverse(Map<K, V> map)
    {
        Map<V, K> inverse = new HashMap<>(map.size());

        for (Map.Entry<K, V> entry : map.entrySet())
            inverse.put(entry.getValue(), entry.getKey());

        return inverse;
    }
}
