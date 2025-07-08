package cn.adu.domain.activity.service;

import cn.adu.domain.activity.model.entity.MarketProductEntity;
import cn.adu.domain.activity.model.entity.TrialBalanceEntity;

/**
 * @description 首页营销服务接口
 * @create 2024-12-14 13:39
 */
public interface IIndexGroupBuyMarketService {

    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;

}
