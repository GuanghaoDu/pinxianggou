package cn.adu.domain.activity.adapter.repository;

import cn.adu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.adu.domain.activity.model.valobj.SCSkuActivityVO;
import cn.adu.domain.activity.model.valobj.SkuVO;

/**
 * @description 活动仓储
 * @create 2024-12-21 10:06
 */
public interface IActivityRepository {

    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId);

    SkuVO querySkuByGoodsId(String goodsId);

    SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId);

    boolean isTagCrowdRange(String tagId, String userId);

    boolean downgradeSwitch();

    boolean cutRange(String userId);

}
