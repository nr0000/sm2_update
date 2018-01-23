package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.CollectableLabel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface LabelTempMapper {

    void batchInsert(List<CollectableLabel> collectableLabels);

    void createTable();

    void dropTable();

    void renameTable(String name);
}