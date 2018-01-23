package com.ssh.sm2_update.service.klService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssh.sm2_update.bean.klBean.KaolaLiveAudioPage;
import com.ssh.sm2_update.bean.klBean.KaolaVodAlbumPage;
import com.ssh.sm2_update.bean.klBean.KaolaVodAudioPage;
import com.ssh.sm2_update.utils.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetResourceFromKlService {

    private final static Logger logger = LoggerFactory.getLogger(GetResourceFromKlService.class);

    public KaolaVodAlbumPage getVodAlbum(String cateId, int pagenum,int pagesize) {
        String urlStr = "http://open.kaolafm.com/v2/album/list/?&appid=vt3981&deviceid=4b5a2a0c7e1857f91229c57cd9dafa41&" +
                "openid=vt39812016122010000985&packagename=com.edog.car.apiSMAradio&os=web&pagenum="+pagenum+"&pagesize="+pagesize+"&cid="+cateId+"&sign=87b329e11a4d1d0bf779bfeb8a5e7e80";
        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 5 && jsonObject == null; i++) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            return null;
        }
        KaolaVodAlbumPage kaolaVodAlbumPage = null;
        try {
            kaolaVodAlbumPage = JSON.toJavaObject(jsonObject, KaolaVodAlbumPage.class);
        } catch (Exception ex) {
            logger.error(jsonObject.toString(),ex);
        }
        return kaolaVodAlbumPage;
    }


    public KaolaVodAudioPage getVodAudio(String klVodAlbumId, int pagenum, int pagesize) {

        String urlStr = "http://open.kaolafm.com/v2/audio/list/?&appid=vt3981&deviceid=4b5a2a0c7e1857f91229c57cd9dafa41&" +
                "openid=vt39812016122010000985&packagename=com.edog.car.apiSMAradio&os=web&pagenum="+pagenum+"&pagesize="+pagesize+"&albumid="+klVodAlbumId+"&sign=87b329e11a4d1d0bf779bfeb8a5e7e80";

        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 5 && jsonObject == null; i++) {
            try {
                Thread.sleep(3100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            System.out.println("重试了多次还为空");
            return null;
        }
        KaolaVodAudioPage klAudiosPage = null;
        try {
            klAudiosPage = JSON.toJavaObject(jsonObject, KaolaVodAudioPage.class);
        } catch (Exception ex) {
            logger.error(jsonObject.toString(),ex);
        }
        return klAudiosPage;
    }

    public KaolaLiveAudioPage getLiveAudio(String liveCateId, Long type, int pagenum, int pagesize) {

        String urlStr = "http://open.kaolafm.com/v2/broadcast/list/?&appid=vt3981&deviceid=4b5a2a0c7e1857f91229c57cd9dafa41&" +
                "openid=vt39812016122010000985&packagename=com.edog.car.apiSMAradio&os=web&type="+type+"&classifyid="+liveCateId+"&pagesize="+pagesize+"&pagenum="+pagenum+"&sign=87b329e11a4d1d0bf779bfeb8a5e7e80";

        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        for (int i = 0; i < 5 && jsonObject == null; i++) {
            try {
                Thread.sleep(3120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonObject = HttpRequestUtils.httpPost(urlStr, null);
        }
        if (jsonObject == null) {
            System.out.println("重试了多次还为空");
            return null;
        }
        KaolaLiveAudioPage klAudiosPage = null;
        try {
            klAudiosPage = JSON.toJavaObject(jsonObject, KaolaLiveAudioPage.class);
        } catch (Exception ex) {
            logger.error(jsonObject.toString(),ex);
        }
        return klAudiosPage;
    }
}
