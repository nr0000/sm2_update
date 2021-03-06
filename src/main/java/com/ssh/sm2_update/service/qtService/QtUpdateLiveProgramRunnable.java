package com.ssh.sm2_update.service.qtService;

import com.sm2.bcl.content.entity.LiveAudio;
import com.sm2.bcl.content.entity.LiveProgram;
import com.ssh.sm2_update.bean.qtBean.QtLProgramPage;
import com.ssh.sm2_update.mapper.*;
import com.ssh.sm2_update.utils.QtAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QtUpdateLiveProgramRunnable implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(QtUpdateLiveProgramRunnable.class);

    @Resource
    private LiveProgramTempMapper liveProgramTempMapper;
    @Resource
    private LiveProgramMapper liveProgramMapper;

    private boolean fastUpdate;
    @Resource
    private GetResourceFromQtService getResourceFromQtServiceImpl;


    @Override
    public void run() {
        logger.info("-----------------------------------------------------蜻蜓直播节目更新开始-----------------------------------------------------");

        QtTaskQueue.finishLiveProgam = new AtomicBoolean(false);
        QtTaskQueue.threadFinishLiveProgram.put(Thread.currentThread().getId(), false);

        while (!QtTaskQueue.liveAudioQueue.isEmpty() || !QtTaskQueue.liveCategoryIdQueue.isEmpty()|| !QtTaskQueue.finishLiveAudio.get()) {
            try {
                update();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        QtTaskQueue.threadFinishLiveProgram.put(Thread.currentThread().getId(), true);
        Boolean finishLiveProgam = true;
        for (Boolean o : QtTaskQueue.threadFinishLiveProgram.values()) {
            if (!o) {
                finishLiveProgam = o;
            }
        }
        if (finishLiveProgam) {
            QtTaskQueue.finishLiveProgam = new AtomicBoolean(true);
            logger.info("完成蜻蜓电台节目抓取任务");
        }

    }

    public void update() {
        LiveAudio liveAudio = QtTaskQueue.liveAudioQueue.poll();
        if (liveAudio == null) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        QtLProgramPage qtLProgramPage = getResourceFromQtServiceImpl.getLiveProgram(liveAudio.getIdFromProvider());
        if (qtLProgramPage == null || qtLProgramPage.getData() == null) {
            return;
        }
        List<LiveProgram> livePrograms = new ArrayList<>();
        if (qtLProgramPage.getErrorno() == 0) {
            livePrograms = QtAdapter.adapt(qtLProgramPage, liveAudio);
        }

        if (livePrograms.size() > 0) {
            logger.info("保存" + livePrograms.size() + "个节目");
            if (fastUpdate) {
                liveProgramMapper.batchInsert(livePrograms);
            } else {
                liveProgramTempMapper.batchInsert(livePrograms);
            }
        }
    }


    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }
}
