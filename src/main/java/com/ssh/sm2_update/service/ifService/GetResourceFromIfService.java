package com.ssh.sm2_update.service.ifService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssh.sm2_update.bean.ifBean.IfVodAlbumsPage;
import com.ssh.sm2_update.bean.ifBean.IfVodAudiosPage;
import com.ssh.sm2_update.utils.HttpRequestUtils;
import com.ssh.sm2_update.utils.MD5Util;
import org.springframework.stereotype.Service;

@Service
public class GetResourceFromIfService {

    static String token = "HRP4hT2bjhwDnkKz";
    static String appid = "DT39L";

    public IfVodAlbumsPage getVodAlbum(String cateId, int pagenum) {
        String sign = MD5Util.get32Md5("appid=" + appid + "&cid=" + cateId + "&pagenum=" + pagenum + "&token=" + token + "&m6wnnx8cA565xcT7");
        String urlStr = "http://fm.ifeng.com/fm/read/fmd/api/" + appid + "/" + token + "/" + sign + "/" + pagenum + "/" + cateId + "/getProgramByCid_210.html";
        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 10 && jsonObject == null; i++) {
            try {
                Thread.sleep(2200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            System.out.println("重试了多次还为空");
            return null;
        }
        IfVodAlbumsPage ifAlbumsPage = null;
        try {
            ifAlbumsPage = JSON.toJavaObject(jsonObject, IfVodAlbumsPage.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(jsonObject);
        }
        return ifAlbumsPage;
    }

    public IfVodAudiosPage getVodAudio(String ifVodAlbumId, int pagenum) {
        String sign = MD5Util.get32Md5("appid=" + appid + "&pagenum=" + pagenum + "&pid=" + ifVodAlbumId + "&token=" + token + "&m6wnnx8cA565xcT7");
        String urlStr = "http://fm.ifeng.com/fm/read/fmd/api/" + appid + "/" + token + "/sign/" + pagenum + "/" + ifVodAlbumId + "/getResourceByPid_210.html";

        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 10 && jsonObject == null; i++) {
            try {
                Thread.sleep(2100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            System.out.println("重试了多次还为空");
            return null;
        }
        IfVodAudiosPage ifAudiosPage = null;
        try {
            ifAudiosPage = JSON.toJavaObject(jsonObject, IfVodAudiosPage.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(jsonObject);
            return null;
        }
        return ifAudiosPage;
    }
}
