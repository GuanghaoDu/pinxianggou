package cn.adu.domain.trade.service.filter;

import cn.adu.domain.trade.adapter.repository.ITradeRepository;
import cn.adu.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.adu.domain.trade.model.entity.TradeRuleCommandEntity;
import cn.adu.domain.trade.model.entity.TradeRuleFilterBackEntity;
import cn.adu.domain.trade.service.factory.TradeRuleFilterFactory;
import cn.adu.types.design.framework.link.model2.handler.ILogicHandler;
import cn.adu.types.enums.ActivityStatusEnumVO;
import cn.adu.types.enums.ResponseCode;
import cn.adu.types.exception.AppException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.util.Date;


@Slf4j
@Service
public class ActivityUsabilityFilter implements ILogicHandler<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;
    @Override
    public TradeRuleFilterBackEntity apply(TradeRuleCommandEntity requestParameter, TradeRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        GroupBuyActivityEntity groupBuyActivity=repository.queryGroupBuyActivityByActivityId(requestParameter.getActivityId());

        if (!ActivityStatusEnumVO.EFFECTIVE.equals(groupBuyActivity.getStatus())){
            throw new AppException(ResponseCode.E0101);
        }


        Date currentTime=new Date();
        if (currentTime.before(groupBuyActivity.getStartTime())||currentTime.after(groupBuyActivity.getEndTime())){
            throw new AppException(ResponseCode.E0102);
        }

        dynamicContext.setGroupBuyActivity(groupBuyActivity);

        return next(requestParameter,dynamicContext);
    }
}
