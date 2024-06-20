package com.axteroid.ose.server.tools.util;

import java.util.Map;

public class MapUtil 
{
	private MapUtil()
    {

    }

    public static boolean isBlankOrNull_O(Map<String, Object> map)
    {
            if (map==null)
                    return true;
            else
                    return map.isEmpty();
    }
    
    public static boolean isNotBlankOrNull_O(Map<String, Object> map)
    {
            return !isBlankOrNull_O(map);
    }
    
    public static boolean isBlankOrNull(Map<String, String> map)
    {
            if (map==null)
                    return true;
            else
                    return map.isEmpty();
    }
    
    public static boolean isNotBlankOrNull(Map<String, String> map)
    {
            return !isBlankOrNull(map);
    }
}
