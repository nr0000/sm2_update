package com.ssh.sm2_update.service.qtService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssh.sm2_update.bean.qtBean.*;
import com.ssh.sm2_update.utils.HttpRequestUtils;
import com.ssh.sm2_update.utils.MyCache;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetResourceFromQtServiceImpl implements GetResourceFromQtService {
    private final static Logger logger = LoggerFactory.getLogger(GetResourceFromQtServiceImpl.class);
    private String token;

    private void refreshPlayUrlPattern() {
        String urlStr = "http://api.open.qingting.fm/v6/media/mediacenterlist";
        try {
            JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, addToken());
            QtMediacenterPage qtMediacenterPage = JSON.toJavaObject(jsonObject, QtMediacenterPage.class);
            String liveHlsAccess = qtMediacenterPage.getData().getRadiostations_hls().getMediacenters().get(0).getAccess().replaceAll("\\?.*$", "");
            MyCache.liveHlsPlayUrlPattern = "http://"
                    + qtMediacenterPage.getData().getRadiostations_hls().getMediacenters().get(0).getDomain()
                    + liveHlsAccess;
            String liveHttpAccess = qtMediacenterPage.getData().getRadiostations_http().getMediacenters().get(0).getAccess().replaceAll("\\?.*$", "");
            MyCache.liveHttpPlayUrlPattern = "http://"
                    + qtMediacenterPage.getData().getRadiostations_http().getMediacenters().get(0).getDomain()
                    + liveHttpAccess;
            String vodM4aAccess = qtMediacenterPage.getData().getStoredaudio_m4a().getMediacenters().get(0).getAccess().replaceAll("\\?.*$", "");
            MyCache.vodM4aPlayUrlPattern = "http://"
                    + qtMediacenterPage.getData().getStoredaudio_m4a().getMediacenters().get(0).getDomain()
                    + vodM4aAccess;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private void getToken() {
        String urlStr = "http://api.open.qingting.fm/access";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id", "YTZlMzczYjQtZThhNi0xMWU0LTkyM2YtMDAxNjNlMDAyMGFk"));
        params.add(new BasicNameValuePair("client_secret", "MGFiODIwYjctYzU0Zi0zZDFlLWI3ZmUtY2IwOTM1ZTI1ZGE1"));
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        for (int i = 0; i < 10; i++) {
            try {
                JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, params);
                QtTokenPage qtTokenPage = JSON.toJavaObject(jsonObject, QtTokenPage.class);
                token = qtTokenPage.getAccess_token();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        refreshPlayUrlPattern();
    }

    @Override
    public QtVodCategoryPage getAllVodCategories() {
        getToken();
        String urlStr = "http://api.open.qingting.fm/v6/media/categories";
        List<NameValuePair> params = addToken();
        JSONObject jsonObject = HttpRequestUtils.httpPost(urlStr, params);
        QtVodCategoryPage qtVodCategoryPage = JSON.toJavaObject(jsonObject, QtVodCategoryPage.class);
        return qtVodCategoryPage;
    }


    @Override
    public QtVodAlbumPage getVodAlbum(String qtCategoryId, boolean order, int curPage, int pageSize) {
        String urlStr = "http://test-api.open.qingting.fm/v6/media/categories/" + qtCategoryId + "/channels/order/0/curpage/" + curPage + "/pagesize/" + pageSize;
        List<NameValuePair> params = addToken();
        JSONObject jsonObject = null;
        QtVodAlbumPage qtVodAlbumPage = null;
        for (int i = 0; i < 10; i++) {
            jsonObject = HttpRequestUtils.httpPost(urlStr, params);
            if (jsonObject != null) {
                try {
                    Integer errorno = jsonObject.getInteger("errorno");
                    switch (errorno) {
                        case 0:
                            qtVodAlbumPage = JSON.toJavaObject(jsonObject, QtVodAlbumPage.class);
                            break;
                        case 100:
                            i = 9;//如果错误代码为100，重试后获得的数据基本都是错误代码100，所以这种情况要减少重试的次数
                            break;
                        case 20001:
                            params = updateToken();
                            break;
                        case 20002:
                            params = updateToken();
                            break;
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            if (qtVodAlbumPage != null && qtVodAlbumPage.getErrorno() == 0) {
                return qtVodAlbumPage;
            }
            try {
                Thread.sleep(5000 + (i * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (qtVodAlbumPage == null || qtVodAlbumPage.getErrorno() == null || qtVodAlbumPage.getErrorno() != 0) {
            logger.info(jsonObject.toString());
            return null;
        }
        return qtVodAlbumPage;
    }

    @Override
    public QtVodAlbumDetailPage getVodAlbumDetail(String qtVodAlbumId) {
        String urlStr = "http://api.open.qingting.fm/v6/media/channelondemands/" + qtVodAlbumId;
        List<NameValuePair> params = addToken();
        JSONObject jsonObject = null;
        QtVodAlbumDetailPage qtVodAlbumDetailPage = null;
        for (int i = 0; i < 10; i++) {
            jsonObject = HttpRequestUtils.httpPost(urlStr, params);
            if (jsonObject != null) {
                try {
                    Integer errorno = jsonObject.getInteger("errorno");
                    switch (errorno) {
                        case 0:
                            qtVodAlbumDetailPage = JSON.toJavaObject(jsonObject, QtVodAlbumDetailPage.class);
                            break;
                        case 100:
                            i = 9;//如果错误代码为100，重试后获得的数据基本都是错误代码100，所以这种情况要减少重试的次数
                            break;
                        case 20001:
                            params = updateToken();
                            break;
                        case 20002:
                            params = updateToken();
                            break;
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            if (qtVodAlbumDetailPage != null && qtVodAlbumDetailPage.getErrorno() == 0) {
                return qtVodAlbumDetailPage;
            }
            try {
                Thread.sleep(5000 + (i * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (qtVodAlbumDetailPage == null || qtVodAlbumDetailPage.getErrorno() == null || qtVodAlbumDetailPage.getErrorno() != 0) {
            logger.info(jsonObject.toString());
            return null;
        }
        return qtVodAlbumDetailPage;
    }

    @Override
    public QtLiveAudioPage getLiveAudio(boolean order, int curPage, int pageSize) {
        String urlStr = "http://api.open.qingting.fm/v6/media/categories/5/channels/order/0/curpage/" + curPage + "/pagesize/" + pageSize;
        List<NameValuePair> params = addToken();
        JSONObject jsonObject = null;
        QtLiveAudioPage qtLiveAudioPage = null;
        for (int i = 0; i < 10; i++) {
            jsonObject = HttpRequestUtils.httpPost(urlStr, params);
            if (jsonObject != null) {
                try {
                    Integer errorno = jsonObject.getInteger("errorno");
                    switch (errorno) {
                        case 0:
                            qtLiveAudioPage = JSON.toJavaObject(jsonObject, QtLiveAudioPage.class);
                            break;
                        case 100:
                            i = 9;//如果错误代码为100，重试后获得的数据基本都是错误代码100，所以这种情况要减少重试的次数
                            break;
                        case 20001:
                            params = updateToken();
                            break;
                        case 20002:
                            params = updateToken();
                            break;
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            if (qtLiveAudioPage != null && qtLiveAudioPage.getErrorno() == 0) {
                return qtLiveAudioPage;
            }
            try {
                Thread.sleep(5000 + (i * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (qtLiveAudioPage == null || qtLiveAudioPage.getErrorno() == null || qtLiveAudioPage.getErrorno() != 0) {
            return null;
        }
        return qtLiveAudioPage;
    }

    @Override
    public QtLProgramPage getLiveProgram(String channelid) {
        String urlStr = "http://api.open.qingting.fm/v6/media/channellives/" + channelid + "/programs/day/1/2/3/4/5/6/7";
        List<NameValuePair> params = addToken();
        JSONObject jsonObject = null;
        QtLProgramPage qtLiveProgramPage = null;
        for (int i = 0; i < 10; i++) {
            jsonObject = HttpRequestUtils.httpPost(urlStr, params);
            if (jsonObject != null) {
                try {
                    Integer errorno = jsonObject.getInteger("errorno");
                    switch (errorno) {
                        case 0:
                            qtLiveProgramPage = JSON.toJavaObject(jsonObject, QtLProgramPage.class);
                            break;
                        case 100:
                            i = 9;//如果错误代码为100，重试后获得的数据基本都是错误代码100，所以这种情况要减少重试的次数
                            break;
                        case 20001:
                            params = updateToken();
                            break;
                        case 20002:
                            params = updateToken();
                            break;
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            if (qtLiveProgramPage != null && qtLiveProgramPage.getErrorno() == 0) {
                return qtLiveProgramPage;
            }
            try {
                Thread.sleep(5000 + (i * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (qtLiveProgramPage == null || qtLiveProgramPage.getErrorno() == null || qtLiveProgramPage.getErrorno() != 0) {
            return new QtLProgramPage();
        }
        return qtLiveProgramPage;
    }

    @Override
    public QtVodAudioPage getVodAudio(String qtVodAlbumId, boolean order, int curPage, int pageSize) {
        String urlStr = "http://test-api.open.qingting.fm/v6/media/channelondemands/" + qtVodAlbumId + "/programs/curpage/" + curPage + "/pagesize/" + pageSize;
        List<NameValuePair> params = addToken();
        JSONObject jsonObject = null;
        QtVodAudioPage qtVodAudioPage = null;
        for (int i = 0; i < 10; i++) {
            jsonObject = HttpRequestUtils.httpPost(urlStr, params);
            if (jsonObject != null) {
                try {
                    Integer errorno = jsonObject.getInteger("errorno");
                    switch (errorno) {
                        case 0:
                            qtVodAudioPage = JSON.toJavaObject(jsonObject, QtVodAudioPage.class);
                            break;
                        case 100:
                            i = 9;//如果错误代码为100，重试后获得的数据基本都是错误代码100，所以这种情况要减少重试的次数
                            break;
                        case 20001:
                            params = updateToken();
                            break;
                        case 20002:
                            params = updateToken();
                            break;
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            if (qtVodAudioPage != null && qtVodAudioPage.getErrorno() == 0) {
                return qtVodAudioPage;
            }
            try {
                Thread.sleep(5000 + (i * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (qtVodAudioPage == null || qtVodAudioPage.getErrorno() == null || qtVodAudioPage.getErrorno() != 0) {
            logger.info(jsonObject.toString());
            return null;
        }
        return qtVodAudioPage;
    }

    private List<NameValuePair> addToken() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (StringUtils.isEmpty(token)) {
            getToken();
        }
        params.add(new BasicNameValuePair("access_token", token));
        return params;
    }

    private List<NameValuePair> updateToken() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        getToken();
        params.add(new BasicNameValuePair("access_token", token));
        return params;
    }

}
