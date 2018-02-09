package com.ssh.sm2_update.service;

import com.alibaba.fastjson.JSONObject;
import com.ssh.sm2_update.utils.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SolrService {
    private final static Logger logger = LoggerFactory.getLogger(SolrService.class);

    public void dataImport(){
        logger.info("更新solr索引");
        String url = "http://localhost:8983/solr/collectables/dataimport?command=full-import";
        JSONObject jsonObject = HttpRequestUtils.httpPost(url, new ArrayList<>());
        logger.info(jsonObject.toJSONString());
    }
}
