package com.ssh.sm2_update;

import com.alibaba.fastjson.JSONObject;
import com.sm2.bcl.content.entity.Collectable;
import com.sm2.bcl.content.entity.enums.CollectableType;
import com.ssh.sm2_update.bean.DBTable;
import com.ssh.sm2_update.bean.qtBean.QtLProgramPage;
import com.ssh.sm2_update.config.MyConfig;
import com.ssh.sm2_update.mapper.ChargingInfoMapper;
import com.ssh.sm2_update.mapper.CollectableMapper;
import com.ssh.sm2_update.mapper.CollectableTempMapper;
import com.ssh.sm2_update.mapper.LiveCategoryMapper;
import com.ssh.sm2_update.service.klService.GetResourceFromKlService;
import com.ssh.sm2_update.service.qtService.GetResourceFromQtService;
import com.ssh.sm2_update.service.ttService.GetResourceFromTtService;
import com.ssh.sm2_update.utils.HttpRequestUtils;
import com.ssh.sm2_update.utils.QtAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Sm2UpdateApplicationTests {

    @Resource
    private CollectableMapper collectableMapper;

    @Autowired
    private CollectableTempMapper collectableTempMapper;

    @Resource
    private ChargingInfoMapper chargingInfoMapper;

    @Resource
    private GetResourceFromQtService getResourceFromQtServiceImpl;

    @Autowired
    private GetResourceFromTtService getResourceFromTtService;

    @Autowired
    private GetResourceFromKlService getResourceFromKlService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MyConfig myConfig;

    @Test
    public void ss() throws ExecutionException, InterruptedException {
        LocalDate localDate = LocalDate.now();
        localDate.getMonth();
    }


    @Test
    public void sdf(){

        DateUtil.dayOfWeekOf(new Date());
    }




    @Test
    public void vbss(){



    }


    @Test
    @Transactional
    @Rollback(value = false)
    public void contextLoads() {
        chargingInfoMapper.renameTable("qqqq");
    }

    @Test
    public void test() {
        List<Collectable> collectableList = collectableMapper.getIdBetween(0, 100);
        System.out.println(collectableList.size());
    }

    @Test
    public void create() {
        collectableMapper.getMaxId();
        DBTable dbTable = new DBTable("content_t_collectable_2017", 1);
        Map map = new HashMap();
        map.put("name", "content_t_collectable_2017");
        map.put("autoIncrement", 2);
        collectableMapper.createTable(dbTable);
    }

}
