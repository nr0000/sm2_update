package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.Collectable;
import com.sm2.bcl.content.entity.LiveAudio;
import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.bean.DBTable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface CollectableTempMapper {

    void insert(Collectable collectable);

    @Delete("delete from content_t_collectable_temp where id=#{id}")
    void delete(Collectable collectable);

    /**
     * 保存之前就已经设置好了id
     * @param vodAudios
     */
    void batchInsertVodAudioWithId(List<VodAudio> vodAudios);

    void batchInsertLiveAudioWithId(List<LiveAudio> liveAudios);

    /**
     * 保存之前没有设置id，使用数据库的自增长id，并且会返回id
     * @param vodAudios
     */
    void batchInsertVodAudioWithoutId(List<VodAudio> vodAudios);

    void batchInsertLiveAudioWithoutId(List<LiveAudio> liveAudios);

    void batchInsertVodAlbumWithoutId(List<VodAlbum> vodAlbums);

    void batchInsertVodAlbumWithId(List<VodAlbum> vodAlbums);

    @Select("SELECT Count(id) FROM content_t_collectable")
    int countId();

    List<Collectable> getIdBetween(@Param("start") int start, @Param("end") int end);

    void createTable(DBTable dbTable);

    void dropTable();

    void renameTable(String name);
}