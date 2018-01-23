package com.ssh.sm2_update.bean.qtBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/6/5
 * Version：
 */
public class QtLProgramPageDate {
    @JSONField(name="1")
    private List<QtLProgramPageDataInfo> day_1;

    @JSONField(name="2")
    private List<QtLProgramPageDataInfo> day_2;

    @JSONField(name="3")
    private List<QtLProgramPageDataInfo> day_3;

    @JSONField(name="4")
    private List<QtLProgramPageDataInfo> day_4;

    @JSONField(name="5")
    private List<QtLProgramPageDataInfo> day_5;

    @JSONField(name="6")
    private List<QtLProgramPageDataInfo> day_6;

    @JSONField(name="7")
    private List<QtLProgramPageDataInfo> day_7;

    public List<QtLProgramPageDataInfo> getDay_1() {
        return day_1;
    }

    public void setDay_1(List<QtLProgramPageDataInfo> day_1) {
        this.day_1 = day_1;
    }

    public List<QtLProgramPageDataInfo> getDay_2() {
        return day_2;
    }

    public void setDay_2(List<QtLProgramPageDataInfo> day_2) {
        this.day_2 = day_2;
    }

    public List<QtLProgramPageDataInfo> getDay_3() {
        return day_3;
    }

    public void setDay_3(List<QtLProgramPageDataInfo> day_3) {
        this.day_3 = day_3;
    }

    public List<QtLProgramPageDataInfo> getDay_4() {
        return day_4;
    }

    public void setDay_4(List<QtLProgramPageDataInfo> day_4) {
        this.day_4 = day_4;
    }

    public List<QtLProgramPageDataInfo> getDay_5() {
        return day_5;
    }

    public void setDay_5(List<QtLProgramPageDataInfo> day_5) {
        this.day_5 = day_5;
    }

    public List<QtLProgramPageDataInfo> getDay_6() {
        return day_6;
    }

    public void setDay_6(List<QtLProgramPageDataInfo> day_6) {
        this.day_6 = day_6;
    }

    public List<QtLProgramPageDataInfo> getDay_7() {
        return day_7;
    }

    public void setDay_7(List<QtLProgramPageDataInfo> day_7) {
        this.day_7 = day_7;
    }
}
