package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.VodAlbum;
import com.ssh.sm2_update.bean.DBTable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface VodAlbumTempMapper {

    void insert(VodAlbum vodAlbum);

    @Delete("delete from content_t_vodAlbum_temp where id=#{id}")
    void delete(VodAlbum vodAlbum);

    void batchInsert(List<VodAlbum> vodAlbums);

    void createTable();

    void dropTable();

    void renameTable(String name);

    void createTrigger(@Param("userDB") String userDB, @Param("triggerName") String triggerName);

}