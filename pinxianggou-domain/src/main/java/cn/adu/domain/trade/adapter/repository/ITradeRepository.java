package cn.adu.domain.trade.adapter.repository;

import cn.adu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.adu.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.adu.domain.trade.model.entity.MarketPayOrderEntity;
import cn.adu.domain.trade.model.valobj.GroupBuyProgressVO;

public interface ITradeRepository {


    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo);

    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    Integer queryOrderCountByActivityId(Long activityId, String userId);

    GroupBuyActivityEntity queryGroupBuyActivityByActivityId(Long activityId);
}
