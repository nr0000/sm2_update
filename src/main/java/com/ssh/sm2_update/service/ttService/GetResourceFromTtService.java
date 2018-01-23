package com.ssh.sm2_update.service.ttService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sm2.bcl.common.util.ColUtils;
import com.sm2.bcl.common.util.StringUtils;
import com.ssh.sm2_update.bean.klBean.KaolaLiveAudioPage;
import com.ssh.sm2_update.bean.klBean.KaolaVodAlbumPage;
import com.ssh.sm2_update.bean.klBean.KaolaVodAudioPage;
import com.ssh.sm2_update.bean.ttBean.TtCategoryPage;
import com.ssh.sm2_update.bean.ttBean.TtVodAlbumPage;
import com.ssh.sm2_update.bean.ttBean.TtVodAudioPage;
import com.ssh.sm2_update.utils.HttpRequestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GetResourceFromTtService {

    private String baseUrl = "http://open.api.tingtingfm.com";

    private final static Logger logger = LoggerFactory.getLogger(GetResourceFromTtService.class);

    private Map countSign(Map<String, String> paramMap, String apiUrl) {
        paramMap.put("key", "smaradio");
        List<String> keySortedList = paramMap.keySet().stream().sorted(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s + "=" + paramMap.get(s);
            }
        }).collect(Collectors.toList());
        String signSource = apiUrl + "_" + ColUtils.join(keySortedList, "&") + "_" + "ypcKopIvNoCTsQ6Nm7FbkEPjr4RGP1T0";
        keySortedList.add("sign=" + StringUtils.stringMd5(signSource).toLowerCase());
        paramMap.put("params", ColUtils.join(keySortedList, "&"));
        return paramMap;
    }


    public TtCategoryPage getSubCategory(String type) {
        String apiUrl = "/v2/vod/sub_catelist";
        Map<String, String> paramMap = new HashMap();
        paramMap.put("type", type);
        countSign(paramMap, apiUrl);
        String urlStr = baseUrl + apiUrl + "?" + paramMap.get("params");
        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 5 && jsonObject == null; i++) {
            try {
                Thread.sleep(3002);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            return null;
        }
        TtCategoryPage ttCategoryPage = null;
        try {
            ttCategoryPage = JSON.toJavaObject(jsonObject, TtCategoryPage.class);
        } catch (Exception ex) {
            logger.error(jsonObject.toString(), ex);
        }
        return ttCategoryPage;
    }


    public TtVodAlbumPage getVodAlbum(String type) {
        String apiUrl = "/v2/vod/cate_album_list";
        Map<String, String> paramMap = new HashMap();
        paramMap.put("type", type);
        countSign(paramMap, apiUrl);
        String urlStr = baseUrl + apiUrl + "?" + paramMap.get("params");
        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 5 && jsonObject == null; i++) {
            try {
                Thread.sleep(3003);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            return null;
        }
        TtVodAlbumPage ttVodAlbumPage = null;
        try {
            ttVodAlbumPage = JSON.toJavaObject(jsonObject, TtVodAlbumPage.class);
        } catch (Exception ex) {
            logger.error(jsonObject.toString(), ex);
        }
        return ttVodAlbumPage;
    }

    public TtVodAudioPage getVodAudio(String type, int page) {
        String apiUrl = "/v2/vod/album_audio_list";
        Map<String, String> paramMap = new HashMap();
        paramMap.put("type", type.toString());
        paramMap.put("page", page + "");
        countSign(paramMap, apiUrl);
        String urlStr = baseUrl + apiUrl + "?" + paramMap.get("params");
        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 5 && jsonObject == null; i++) {
            try {
                Thread.sleep(3004);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            return null;
        }
        TtVodAudioPage ttVodAudioPage = null;
        try {
            ttVodAudioPage = JSON.toJavaObject(jsonObject, TtVodAudioPage.class);
        } catch (Exception ex) {
            logger.error(jsonObject.toString(), ex);
        }
        return ttVodAudioPage;
    }


}
