package com.ssh.sm2_update.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sm2.bcl.common.util.FileUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class CategoryMappings {
    private static Map<String, Map<String, Long>> mappings;

    static {
        mappings = new HashMap<>();
        InputStream mappingsStream = CategoryMappings.class.getResourceAsStream("/cate-id-mappings.json");
        String jsonMappingsString = FileUtils.readResourceFile(mappingsStream);
        JSONObject jMappings = JSON.parseObject(jsonMappingsString);
        mappings.put("qingtingLive", jsonToMap(jMappings.getJSONObject("qingting").getJSONObject("live")));
        mappings.put("qingtingVod", jsonToMap(jMappings.getJSONObject("qingting").getJSONObject("vod")));
        mappings.put("kaolaLive", jsonToMap(jMappings.getJSONObject("kaola").getJSONObject("live")));
        mappings.put("kaolaVod", jsonToMap(jMappings.getJSONObject("kaola").getJSONObject("vod")));
        mappings.put("kaolaType", jsonToMap(jMappings.getJSONObject("kaola").getJSONObject("type")));
        mappings.put("tingtingLive", jsonToMap(jMappings.getJSONObject("tingting").getJSONObject("live")));
        mappings.put("tingtingVod", jsonToMap(jMappings.getJSONObject("tingting").getJSONObject("vod")));
        mappings.put("ifengVod", jsonToMap(jMappings.getJSONObject("ifeng").getJSONObject("vod")));
        mappings.put("kukeVod", jsonToMap(jMappings.getJSONObject("kuke").getJSONObject("vod")));
    }

    private static Map<String, Long> jsonToMap(JSONObject jObj) {
        Map<String, Long> map = new HashMap<>();
        for (String key : jObj.keySet()) {
            map.put(key, jObj.getLongValue(key));
        }
        return map;
    }

    public static Map<String, Long> getQingtingLiveMapping() {
        return mappings.get("qingtingLive");
    }

    public static Map<String, Long> getQingtingVodMapping() {
        return mappings.get("qingtingVod");
    }

    public static Map<String, Long> getKaolaLiveMapping() {
        return mappings.get("kaolaLive");
    }

    public static Map<String, Long> getKaolaVodMapping() {
        return mappings.get("kaolaVod");
    }

    public static Map<String, Long> getKaolaTypeMapping() {
        return mappings.get("kaolaType");
    }

    public static Map<String, Long> getTingtingLiveMapping() {
        return mappings.get("tingtingLive");
    }

    public static Map<String, Long> getTingtingVodMapping() {
        return mappings.get("tingtingVod");
    }

    public static Map<String, Long> getIfengVodMapping() {
        return mappings.get("ifengVod");
    }

    public static Map<String, Long> getKukeVodMapping() {
        return mappings.get("kukeVod");
    }
}
