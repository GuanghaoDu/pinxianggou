package cn.adu.infrastructure.dao;

import cn.adu.infrastructure.dao.po.CrowdTagsDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description 人群标签明细
 * @create 2024-12-28 11:49
 */
@Mapper
public interface ICrowdTagsDetailDao {

    void addCrowdTagsUserId(CrowdTagsDetail crowdTagsDetailReq);

}
