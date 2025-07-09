package cn.adu.domain.trade.service.factory;

import cn.adu.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.adu.domain.trade.model.entity.TradeRuleCommandEntity;
import cn.adu.domain.trade.model.entity.TradeRuleFilterBackEntity;
import cn.adu.domain.trade.service.filter.ActivityUsabilityFilter;
import cn.adu.domain.trade.service.filter.UserTakeLimitRuleFilter;
import cn.adu.types.design.framework.link.model2.LinkArmory;
import cn.adu.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TradeRuleFilterFactory {


    @Bean
    public BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity>
    tradeRuleFilter(ActivityUsabilityFilter activityUsabilityFilter, UserTakeLimitRuleFilter userTakeLimitRuleFilter)
    {
        LinkArmory<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> linkArmory=
                new LinkArmory<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity>
                        ("交易规则过滤链",activityUsabilityFilter,userTakeLimitRuleFilter);
        return linkArmory.getLogicLink();


    }
    //上下文
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext{

        private GroupBuyActivityEntity groupBuyActivity;

    }
}
