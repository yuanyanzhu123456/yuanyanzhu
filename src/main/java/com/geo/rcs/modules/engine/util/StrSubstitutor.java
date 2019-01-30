package com.geo.rcs.modules.engine.util;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Author:  yongmingz
 * Created on : 2018.1.08
 */

public class StrSubstitutor {
    private Map<String, String> map;
    private static final Pattern p = Pattern.compile("\\$\\{(.+?)\\}");

    public StrSubstitutor(Map<String, String> map) {
        this.map = map;
    }

    public String replace(String str) {
        StringBuilder sb = new StringBuilder();
        char[] strArray = str.toCharArray();
        int i = 0;
        while (i < strArray.length - 1) {
            if (strArray[i] == '$' && strArray[i + 1] == '{') {
                i = i + 2;
                int begin = i;
                while (strArray[i] != '}') ++i;
                sb.append(map.get(str.substring(begin, i++)));
            } else {
                sb.append(strArray[i]);
                ++i;
            }
        }
        if (i < strArray.length) sb.append(strArray[i]);
        return sb.toString();
    }

}

