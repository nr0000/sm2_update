package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.ChargingInfo;
import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAlbumSaleInfo;
import com.ssh.sm2_update.bean.DBTable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface VodAlbumSaleInfoMapper {

    void batchInsert(List<VodAlbumSaleInfo> vodAlbumSaleInfos);

    @Delete("delete from content_t_vodalbumsaleinfo where id=#{id}")
    void delete(VodAlbumSaleInfo vodAlbumSaleInfo);

    void createTable();

    void renameTable(String name);
}