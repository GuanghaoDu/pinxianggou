package cn.adu.domain.trade.service;


import cn.adu.domain.trade.adapter.repository.ITradeRepository;
import cn.adu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.adu.domain.trade.model.entity.MarketPayOrderEntity;
import cn.adu.domain.trade.model.entity.PayActivvityEntity;
import cn.adu.domain.trade.model.entity.PayDiscountEntity;
import cn.adu.domain.trade.model.entity.UserEntity;
import cn.adu.domain.trade.model.valobj.GroupBuyProgressVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TraderOrderService implements ITradeOrderService{

   @Resource
    private ITradeRepository repository;

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        return repository.queryNoPayMarketPayOrderByOutTradeNo(userId,outTradeNo);
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        return repository.queryGroupBuyProgress(teamId);
    }

    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivvityEntity payActivvityEntity, PayDiscountEntity payDiscountEntity) {

        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivvityEntity(payActivvityEntity)
                .payDiscountEntity(payDiscountEntity)
                .build();


        return repository.lockMarketPayOrder(groupBuyOrderAggregate);
    }
}
