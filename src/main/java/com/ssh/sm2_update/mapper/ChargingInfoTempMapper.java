package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.ChargingInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface ChargingInfoTempMapper {

    void batchInsert(List<ChargingInfo> chargingInfos);

    @Delete("delete from content_t_charginginfo_temp where id=#{id}")
    void delete(ChargingInfo chargingInfo);

    void createTable();

    void dropTable();

    void renameTable(String name);

}