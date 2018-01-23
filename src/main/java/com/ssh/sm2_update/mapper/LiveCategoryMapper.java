package com.ssh.sm2_update.mapper;

import com.sm2.bcl.content.entity.LiveCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//有Mapper，这个注解可以不要
public interface LiveCategoryMapper {


    @Select("SELECT id,categoryName, locatedInRegionCode, hasAnyContent, parentCategory FROM content_t_livecategory WHERE parentCategory = (SELECT id FROM content_t_livecategory WHERE content_t_livecategory.categoryName = '省市台')")
    List<LiveCategory> getLiveRegionalCates();

    @Select("SELECT id,categoryName, locatedInRegionCode, hasAnyContent, parentCategory FROM content_t_livecategory WHERE categoryName = '国家台'")
    LiveCategory getLiveNationalCate();

    @Select("SELECT id,categoryName, locatedInRegionCode, hasAnyContent, parentCategory FROM content_t_livecategory WHERE categoryName = '网络台'")
    LiveCategory getLiveNetworkCate();

    void renameTable(String name);
}
