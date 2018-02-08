package com.ssh.sm2_update.controler;

import com.ssh.sm2_update.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UpdateControler {
    private final static Logger logger = LoggerFactory.getLogger(UpdateControler.class);

    @Resource
    private UpdateService updateService;

    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

    @RequestMapping("/updateAll")
    @Scheduled(cron = "0 46 20 * * ?")
//    @Scheduled(cron = "0 0 10 ? * 6")
    public String updateAll() {
        logger.info("预备更新");
        updateService.updateAll(false);
        logger.info("开始更新");
        return "开始更新";
    }

    @RequestMapping("/fastUpdate")
//    @Scheduled(cron = "0 26 18 * * ?")
    public String fastUpdate() {
        logger.info("预备更新");
        updateService.updateAll(true);
        logger.info("开始更新");
        return "开始更新";
    }

    //content_t_internalslidesshow  content_t_recommend
    @RequestMapping("/renameTable")
    public String renameTable() {
        updateService.replaceTable();
        return "成功替换表";
    }

}
