package cn.adu.domain.trade.service.filter;

import cn.adu.domain.trade.adapter.repository.ITradeRepository;
import cn.adu.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.adu.domain.trade.model.entity.TradeRuleCommandEntity;
import cn.adu.domain.trade.model.entity.TradeRuleFilterBackEntity;
import cn.adu.domain.trade.service.factory.TradeRuleFilterFactory;
import cn.adu.types.design.framework.link.model2.handler.ILogicHandler;
import cn.adu.types.enums.ResponseCode;
import cn.adu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserTakeLimitRuleFilter implements ILogicHandler<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeRuleFilterBackEntity apply(TradeRuleCommandEntity requestParameter, TradeRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        GroupBuyActivityEntity groupBuyActivity=dynamicContext.getGroupBuyActivity();
        Integer count= repository.queryOrderCountByActivityId(requestParameter.getActivityId(),requestParameter.getUserId());
        if (groupBuyActivity.getTakeLimitCount()!=null&&count>=groupBuyActivity.getTakeLimitCount()){

            throw new AppException(ResponseCode.E0103);
        }

        dynamicContext.setGroupBuyActivity(groupBuyActivity);

        return next(requestParameter,dynamicContext);
    }
}
