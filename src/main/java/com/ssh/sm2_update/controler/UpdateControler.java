package com.ssh.sm2_update.controler;

import com.ssh.sm2_update.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public String updateAll(){
        updateService.updateAll(false);
        logger.info("开始更新");
        return "开始更新";
    }

    @RequestMapping("/fastUpdate")
    public String fastUpdate(){
        updateService.updateAll(true);
        logger.info("开始更新");
        return "开始更新";
    }

    //content_t_internalslidesshow  content_t_recommend
    @RequestMapping("/renameTable")
    public String renameTable(){
        updateService.replaceTable();
        return "成功替换表";
    }

}
