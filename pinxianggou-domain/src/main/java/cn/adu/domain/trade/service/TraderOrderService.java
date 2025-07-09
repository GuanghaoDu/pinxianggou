package cn.adu.domain.trade.service;


import cn.adu.domain.trade.adapter.repository.ITradeRepository;
import cn.adu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.adu.domain.trade.model.entity.*;
import cn.adu.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.adu.domain.trade.service.factory.TradeRuleFilterFactory;
import cn.adu.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TraderOrderService implements ITradeOrderService {

    @Resource
    private ITradeRepository repository;
    @Resource
    private BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> tradeRuleFilter;

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        return repository.queryNoPayMarketPayOrderByOutTradeNo(userId, outTradeNo);
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        return repository.queryGroupBuyProgress(teamId);
    }

    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivvityEntity payActivvityEntity, PayDiscountEntity payDiscountEntity) throws Exception {

        TradeRuleFilterBackEntity tradeRuleFilterBackEntity = tradeRuleFilter.apply(TradeRuleCommandEntity.builder()
                .activityId(payActivvityEntity.getActivityId())
                .userId(userEntity.getUserId())
                .build(), new TradeRuleFilterFactory.DynamicContext());
        Integer userTakeOrderCount = tradeRuleFilterBackEntity.getUserTakeOrderCount();
        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivvityEntity(payActivvityEntity)
                .payDiscountEntity(payDiscountEntity)
                .userTakeOrderCount(userTakeOrderCount)
                .build();


        return repository.lockMarketPayOrder(groupBuyOrderAggregate);
    }
}
