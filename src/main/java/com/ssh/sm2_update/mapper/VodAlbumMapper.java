package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.VodAlbum;
import com.ssh.sm2_update.bean.DBTable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface VodAlbumMapper {

    void insert(VodAlbum vodAlbum);

    @Delete("delete from content_t_vodAlbum where id=#{id}")
    void delete(VodAlbum vodAlbum);

    void batchInsert(List<VodAlbum> vodAlbums);

    void createTable();

    void dropTable();

    void renameTable(String name);
}