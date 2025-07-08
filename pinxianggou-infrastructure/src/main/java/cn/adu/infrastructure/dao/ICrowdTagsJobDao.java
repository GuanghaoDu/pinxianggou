package cn.adu.infrastructure.dao;

import cn.adu.infrastructure.dao.po.CrowdTagsJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description 人群标签任务
 * @create 2024-12-28 11:50
 */
@Mapper
public interface ICrowdTagsJobDao {

    CrowdTagsJob queryCrowdTagsJob(CrowdTagsJob crowdTagsJobReq);

}
