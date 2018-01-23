package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.VodAlbumSaleInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface VodAlbumSaleInfoTempMapper {

    void batchInsert(List<VodAlbumSaleInfo> vodAlbumSaleInfos);

    @Delete("delete from content_t_vodalbumsaleinfo_temp where id=#{id}")
    void delete(VodAlbumSaleInfo vodAlbumSaleInfo);

    void createTable();

    void dropTable();

    void renameTable(String name);
}