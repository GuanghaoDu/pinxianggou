package cn.adu.domain.trade.service;


import cn.adu.domain.trade.model.entity.MarketPayOrderEntity;
import cn.adu.domain.trade.model.entity.PayActivvityEntity;
import cn.adu.domain.trade.model.entity.PayDiscountEntity;
import cn.adu.domain.trade.model.entity.UserEntity;
import cn.adu.domain.trade.model.valobj.GroupBuyProgressVO;

public interface ITradeOrderService {

    MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId,String outTradeNo);

    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivvityEntity payActivvityEntity, PayDiscountEntity payDiscountEntity);

}
