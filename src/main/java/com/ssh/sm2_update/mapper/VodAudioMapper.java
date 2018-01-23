package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.bean.DBTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface VodAudioMapper {

    void batchInsert(List<VodAudio> vodAudios);

    void insert(VodAudio vodAudio);

    void createTable();

    void dropTable();

    void renameTable(String name);
}